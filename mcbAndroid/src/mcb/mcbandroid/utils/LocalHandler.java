package mcb.mcbandroid.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.UUID;

import mcb.debug.Logger;
import mcb.mcbandroid.Application;
import mcb.model.BaseObject;
import mcb.model.Brainstorming;
import mcb.model.Cluster;
import mcb.model.ParentInterface;
import mcb.model.PostIt;
import mcb.model.PostItWithTags;
import mcb.model.User;
import mcb.util.JSONMapper;
import mcb.util.i18nString;
import mcb.util.i18nString.Language;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


public class LocalHandler implements HandlerInterface {
	protected Brainstorming model = null;
	protected Application app = null;
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public static final String EVENT_UPDATE = "update";
	
	public LocalHandler(Application application) {
		app = application;
		
		if ( loadModelData() == false ) {
			model = new Brainstorming();
			saveModelData();
		}
	}	
	
	public void newModel() {
		model.clean();
		saveModelData();
	}
	
	//
	//
	// Handler interface (update to the data)
	//
	//
	
	// @TODO remove
	public final Brainstorming getModel() {
		return model;
	}
	
	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
		model.addListener(pcl);
	}
	
	public void addObject(mcb.model.BaseObject o) {
		model.add(o);
		saveModelData();
	}
	
	public UUID createPostIt(User author, i18nString title, i18nString content, String tags) {
		PostItWithTags o = new PostItWithTags(author);
		o.setContent(content);
		o.setTitle(title);
		o.setTags(Tagger.toTags(tags));
		model.add(o);
		saveModelData();
		
		return o.getId();
	}
	
	public void updatePostIt(User user, UUID id, Language language, String title, String content, String tags) {
		PostIt o = (PostIt) model.get(id);
		
		if ( o != null ) {
			if ( o instanceof PostItWithTags )
				((PostItWithTags)o).setTags(Tagger.toTags(tags));
			
			o.getContent().set(language, content);
			o.getTitle().set(language, title);
			saveModelData();
		}
		else {
			Logger.error("model", "Action updateCluster tried on model failed (null object)");
		}
	}
	
	public UUID createCluster(User author, i18nString title) {
		Cluster o = new Cluster(author);
		o.setContent(title);
		model.add(o);
		saveModelData();
		return o.getId();
	}
	
	public void updateCluster(User user, UUID id, Language language, String content) {
		Cluster o = (Cluster) model.get(id);
		if ( o != null ) {
			o.getContent().set(language, content);
			saveModelData();
		}
		else {
			Logger.error("model", "Action updateCluster tried on model failed (null object)");
		}
	}

	public void removeObject(UUID modelId) {
		model.destroy(modelId);
		saveModelData();
	}

	public void unselect(User user, UUID id) {
		BaseObject o = model.get(id);
		if ( o != null ) {
			o.unselectedBy(user);
			saveModelData();
			pcs.firePropertyChange(EVENT_UPDATE, null, null);
		}
		else {
			Logger.error("model", "Action unselect tried on model failed (null object)");
		}
	}

	public void select(User user, UUID id) {
		BaseObject o = model.get(id);
		if ( o != null ) {
			o.selectedBy(user);
			saveModelData();
			pcs.firePropertyChange(EVENT_UPDATE, null, null);
		}
		else {
			Logger.error("model", "Action select tried on model failed (null object)");
		}
	}

	public void affiliate(UUID id, UUID parentId) {		
		ParentInterface pa = (ParentInterface) model.get(parentId);
		BaseObject o = model.get(id);
		if ( pa != null && o != null ) {
			pa.add(o);
			saveModelData();
		}
		else {
			Logger.error("model", "Action affiliate tried on model failed (null object)");
		}
	}

	public void disaffiliate(UUID id, UUID parentId) {
		ParentInterface pa = (ParentInterface) model.get(parentId);
		if ( pa != null ) {
			BaseObject o = pa.get(id);
			pa.remove(id);
			
			// affiliate to root if necessary
			if ( model.get(id) == null )
				model.add(o);
			
			saveModelData();
		}
		else {
			Logger.error("model", "Action disaffiliate tried on model failed (null object)");
		}
	}

	
	//
	//
	// others interactions
	//
	//
		
	/**
	 * Save model object to file
	 */
	protected void saveModelData() {
		String data;
		try {
			data = JSONMapper.toJson(model);
			app.write(Application.FILENAME_MODEL, data);
		} catch (JsonParseException e) {
			Logger.error("model", "loadModelData", e);
		} catch (JsonMappingException e) {
			Logger.error("model", "loadModelData", e);
		} catch (IOException e) {
			Logger.error("model", "loadModelData", e);
		}
	}
	
	/**
	 * Load model object from file
	 * @return true on success
	 * @return false otherwise
	 */
	protected boolean loadModelData() {
		try {
			String json = app.read(Application.FILENAME_MODEL);
			if(json == null) return false;
			model = JSONMapper.getInstance().readValue(json, Brainstorming.class);
			if ( model != null ) {
				// do some tricks to have only one object (same ref) for every object sharing the same id
				for ( int i = 0 ; i < model.getChildren().size() ; i++ ) {
					makeSingleRef(model.getChildren().get(i), model);
				}
				return true;
			}
		} catch (JsonParseException e) {
			Logger.error("model", "loadModelData", e);
		} catch (JsonMappingException e) {
			Logger.error("model", "loadModelData", e);
		} catch (IOException e) {
			Logger.error("model", "loadModelData", e);
		}
		
		return false;
	}
	
	protected void makeSingleRef(BaseObject one, ParentInterface tree) {
		for ( int i = 0 ; i < tree.getChildren().size() ; i++ ) {
			BaseObject bo = tree.getChildren().get(i);
			if ( one.getId().equals(bo.getId()) && bo != one ) {
				tree.getChildren().set(i, one);
			}
			if ( one != bo && bo instanceof ParentInterface ) {
				makeSingleRef(one, (ParentInterface) bo);
			}
		}
	}

	public void lock(UUID modelId) {
		// NOP
	}

	public void unlock(UUID modelId) {
		// NOP
	}

}
