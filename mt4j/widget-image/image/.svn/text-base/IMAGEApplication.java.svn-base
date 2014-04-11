package utc.tatinpic.gui.widgets.image;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.util.math.Vector3D;

import utc.tatinpic.gui.widgets.menu.circularmenu.WorkbenchHandler;
import utc.tatinpic.model.MenuApplication;
import utc.tatinpic.model.Workbench;

public class IMAGEApplication extends MenuApplication {

	@Override
	public void play() {
		Workbench workbench = ((WorkbenchHandler) this.getParentComponent())
				.getWorkbench();
		AbstractShape component = new IMAGEChooser(getParentComponent()
				.getRenderer(), workbench);
		IBoundingShape bounds = component.getBounds();
		component.transform(getParentComponent().getGlobalMatrix());
		component.setPositionRelativeToOther(getParentComponent(),
				new Vector3D(bounds.getWidthXY(TransformSpace.GLOBAL) / 2,
						-bounds.getHeightXY(TransformSpace.GLOBAL) / 2));
		System.out.println(bounds.getHeightXY(TransformSpace.GLOBAL));

		getParentComponent().getParent().addChild(component);
	}

}
