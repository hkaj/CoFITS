package utc.bsfile.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CofitsModel {

	public CofitsModel() {
		
	}

	
	//Property Change methods
	public void addPropertyChangeListener(PropertyChangeListener listener){
		m_pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		m_pcs.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		m_pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	
	//Members
	private PropertyChangeSupport m_pcs = new PropertyChangeSupport(this);
}
