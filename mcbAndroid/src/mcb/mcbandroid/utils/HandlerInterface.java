package mcb.mcbandroid.utils;

import java.beans.PropertyChangeListener;
import java.util.UUID;

import mcb.model.Brainstorming;
import mcb.model.User;
import mcb.util.i18nString;
import mcb.util.i18nString.Language;


/**
 * The Android application has three working mode:
 * - local mode (@see LocalHandler)
 * - meeting mode (@see MeetingHandler)
 * - memorae mode (@see MemoraeHandler)
 * Each mode is represented by an handler, which implements this interface, so 
 * it's not necessary to discriminate between working modes in most cases
 */
public interface HandlerInterface {
	
	/**
	 * The application must be able to listen to change from the handler
	 * @param pcl The listener
	 */
	public void addListener(PropertyChangeListener pcl);
	
	/**
	 * Create object
	 * @param object 		Object to add to the model
	 */
	public void addObject(mcb.model.BaseObject object);
	
	/**
	 * Create post-it
	 * @param author 		User who created the post-it
	 * @param title			Internationalized Title (optional, can be null)
	 * @param content		Internationalized Content (must not be null)
	 * @param tags			List of tags (space separated)
	 * @return				Id of the newly created post-it
	 */
	public UUID createPostIt(User author, i18nString title, i18nString content, String tags);
	
	/**
	 * Update a post-it
	 * @param user			User who updated the post-it
	 * @param id			Id of the post-it
	 * @param language		Language of the new title/content
	 * @param title			New title (optional)
	 * @param content		New content
	 * @param tags			New list of tags
	 */
	public void updatePostIt(User user, UUID id, Language language, String title, String content, String tags);
	
	/**
	 * Create a new cluster
	 * @param author		User who created the cluster
	 * @param title			Internationalized Title of the cluster
	 * @return				Id of the newly created cluster
	 */
	public UUID createCluster(User author, i18nString title);
	
	/**
	 * Update a cluster
	 * @param user			User who updated the cluster
	 * @param id			Id of the cluster
	 * @param language		Language of the new content
	 * @param content		New content for the cluster
	 */
	public void updateCluster(User user, UUID id, Language language, String content);
	
	/** 
	 * remove object from model
	 * @param modelId id of the object to remove
	 */
	public void removeObject(UUID modelId);
	
	/**
	 * Unselect an object
	 * @param user			User who unselect the object
	 * @param id			Id of the object
	 */
	public void unselect(User user, UUID id);

	/**
	 * Select an object
	 * @param user			User who select the object
	 * @param id			Id of the object
	 */
	public void select(User user, UUID id);

	/**
	 * Affiliate an object (id) to another one (parentId)
	 * @param id			Id of the object to affiliate
	 * @param parentId		Id of the new parent
	 */
	public void affiliate(UUID id, UUID parentId);

	/**
	 * Disaffiliate an object (id) from another one (parentId)
	 * @param id			Id of the object to disaffiliate
	 * @param parentId		Id of the soon to be ex parent
	 */
	public void disaffiliate(UUID id, UUID parentId);

	// @TODO remove
	public Brainstorming getModel();

	/**
	 * Ask for a lock
	 * @param modelId	Id of the object to lock
	 */
	public void lock(UUID modelId);
	
	/**
	 * Unlock an object
	 * @param modelId	Id of the object to unlock
	 */
	public void unlock(UUID modelId);
}
