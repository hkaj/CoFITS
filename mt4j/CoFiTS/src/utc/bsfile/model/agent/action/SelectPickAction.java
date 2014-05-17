package utc.bsfile.model.agent.action;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.mt4j.IPAppletBoth;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.opengl.GLCommon;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PStyle;
import utc.bsfile.model.Constants;

public class SelectPickAction implements IPreDrawAction {
	PropertyChangeSupport pcs;

	public SelectPickAction(PropertyChangeSupport pcs) {
		super();
		this.pcs = pcs;
	}

	@Override
	public void processAction() {
		pcs.firePropertyChange(Constants.OPEN_PICK, null,"pick");
	}

	@Override
	public boolean isLoop() {
		return false;
	}

}
