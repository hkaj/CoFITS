package utc.tatinpic.gui.widgets.browser;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.gui.widgets.menu.circularmenu.WorkbenchHandler;
import utc.tatinpic.model.MenuApplication;
import utc.tatinpic.model.Workbench;

public class BrowserApplication extends MenuApplication {

	@Override
	public void play() {

		System.out.println("Lancement Browser");
		
		Workbench workbench = ((WorkbenchHandler) this.getParentComponent())
				.getWorkbench();
		
		HomeManager hm;
		try {
			hm = new HomeManager(workbench.getTeamMember(),
					System.getProperty("user.dir") + "\\rsc\\browser\\"
							+ "home.xml");
			AbstractShape component;

			component = new BrowserComponent(
					this.getScene().getMTApplication(), "Browser",
					hm.getHome(), 900, 450, true, workbench);
			IBoundingShape bounds = component.getBounds();
			System.out.println("globalMatrix à la création : "+getParentComponent().getGlobalMatrix());
			component.transform(getParentComponent().getGlobalMatrix());
			System.out.println("current vector : "+new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
							-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
			component.setPositionRelativeToOther(getParentComponent(),
					new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
							-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
			getParentComponent().getParent().addChild(component);
			
			WorkbenchHandler[] test = getScene().getMenuHandlers();
			for (int i = 0; i < test.length; i++) {
				System.out.println("TEAM MEMBER : "
						+ test[i].getWorkbench().getTeamMember());
				System.out.println("	" + test[i].getWorkbench().getRow());
				System.out.println("    " + test[i].getCenterPointGlobal());
				System.out.println("    " + test[i].getGlobalMatrix());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
