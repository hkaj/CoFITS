package mcb.mcbandroid.utils;

import java.util.ArrayList;
import java.util.Collection;

import mcb.debug.Logger;
import mcb.mcbandroid.Application;
import mcb.model.BaseObject;
import mcb.model.Brainstorming;
import mcb.model.Memorae.Concept;
import mcb.model.Memorae.Memoire;
import mcb.model.Memorae.MemoraeConnecter;
import mcb.model.Memorae.MemoraeConnecterInterface;
import mcb.model.Memorae.MemoraeConverter;
import mcb.model.Memorae.Ressource;
import android.os.AsyncTask;

public class MemoraeHandler extends LocalHandler implements HandlerInterface {
	
	public static final String INTENT = "memorae";
	public static final String EVENT = "e";
	public static final String PARAM = "p";
	public static final String EVENT_ERROR = "error";
	
	MemoraeConnecterInterface connecter;
	MemoraeConverter converter;
	
	final Memoire memoire;
	final Concept selectedConcept;
	
	public MemoraeHandler(Application app, Memoire memoire, Concept selectedConcept ) {
		super(app);
		
		model = new Brainstorming();
		connecter = new MemoraeConnecter();
		converter = new MemoraeConverter(new MemoraeConverter.MapIdentityProvider());
		
		this.selectedConcept = selectedConcept;
		this.memoire = memoire;
		
		new AsyncTask<Object, Void, Brainstorming>(){

			@Override
			protected Brainstorming doInBackground(Object... params) {
				Memoire m = (Memoire) params[0];
				Concept c = (Concept) params[1];
				Brainstorming out = new Brainstorming();
				Collection<Ressource> ressources = connecter.getAllRessources(m, c);
				for(Ressource ressource : ressources)
					out.add(converter.convert(ressource));
					
				return out;
			}
			
			protected void onPostExecute(Brainstorming result) {
				model.merge(result);
				MemoraeHandler.this.app.onModelLoaded();
				Logger.debug("MemoraeHandler", "model now have ", model.getChildren().size(), "children");
				Logger.printTree(model);
				
			};
			
		}.execute(memoire, selectedConcept);
		
	}
	
	@Override
	protected void saveModelData() {
		
		Collection<Ressource> ressources = new ArrayList<Ressource>();
		
		for(BaseObject bo : model.getChildren())
			ressources.add(converter.convert(bo));
		
		connecter.saveRessources(ressources, memoire, selectedConcept);
		
	}
	
	@Override
	protected boolean loadModelData() {
		return true;
	}
}
