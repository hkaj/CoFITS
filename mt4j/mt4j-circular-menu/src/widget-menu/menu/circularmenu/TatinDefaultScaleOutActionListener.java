package utc.tatinpic.gui.widgets.menu.circularmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4jx.components.visibleComponents.widgets.circularmenu.MTCircularMenuSegment;

public class TatinDefaultScaleOutActionListener implements ActionListener  {
	private TatinCircularMenuSegment segment;
	public TatinDefaultScaleOutActionListener(TatinCircularMenuSegment segment){
		//System.out.println("DefaultScaleOutActionListener created");
		this.segment = segment;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("DefaultScaleOutActionListener.actionPerformed called.");
		//System.out.println("segment.hasChildren(): " + this.segment.hasChildren() );
		if(!this.segment.hasChildren()){
			//AnimationUtil.scaleOut(this.segment.getParentMenu(), false);
		}
	}
}
