package utc.tatinpic.gui.widgets.menu.circularmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import utc.tatinpic.gui.phase.TatinScene;
import utc.tatinpic.model.ApplicationDescription;
import utc.tatinpic.model.MenuApplication;
import utc.tatinpic.model.Workbench;

public class ApplicationActionListener implements ActionListener {
	WorkbenchHandler menu;
	MenuApplication menuapp;
	Workbench workbench;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		menu.closeSubmenus();
		menuapp.play();
	}

	public ApplicationActionListener(WorkbenchHandler menu, TatinScene scene,
			ApplicationDescription apdes, Workbench workbench) {
		this.menu = menu;
		this.workbench = workbench;
		Class<?> appclass;
		try {
			appclass = Class.forName(apdes.getClassName());
			Constructor<?> constructor = appclass.getConstructor();
			menuapp = (MenuApplication) constructor.newInstance();
			menuapp.setParentComponent(menu);
			menuapp.setScene(scene);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
