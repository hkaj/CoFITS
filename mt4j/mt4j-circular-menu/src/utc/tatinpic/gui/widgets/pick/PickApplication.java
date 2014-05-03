package utc.tatinpic.gui.widgets.pick;

import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.ProjectBoot;
import utc.tatinpic.WhiteboardBoot;
import utc.tatinpic.gui.widgets.menu.circularmenu.WorkbenchHandler;
import utc.tatinpic.model.MenuApplication;
import utc.tatinpic.model.Workbench;

public class PickApplication extends MenuApplication {

	@Override
	public void play() {
		Workbench workbench = ((WorkbenchHandler) this.getParentComponent())
				.getWorkbench();
		List<Workbench> workbenchs = null;
		AbstractMTApplication mtApplication = getScene().getMTApplication();
		if (mtApplication instanceof ProjectBoot) {
			workbenchs = ((ProjectBoot) mtApplication)
					.getProject().getWorkingposts();
		} else if (mtApplication instanceof WhiteboardBoot) {
			workbenchs = ((WhiteboardBoot) mtApplication)
					.getProject().getWorkingPosts();
		} else {
			System.err.println("WORKBENCHS NOT FOUND");
		}
		AbstractShape component = new PickFileChooser(getParentComponent()
				.getRenderer(), workbench, workbenchs);
		IBoundingShape bounds = component.getBounds();
		component.transform(getParentComponent().getGlobalMatrix());
		component.setPositionRelativeToOther(getParentComponent(),
				new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
						-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
		System.out.println(bounds.getHeightXY(TransformSpace.GLOBAL));

		getParentComponent().getParent().addChild(component);
	}

}
