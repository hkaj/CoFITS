package utc.tatinpic.gui.widgets.browser;
import org.berkelium.java.api.Buffer;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowDelegate;

public class BrowserDelegate implements WindowDelegate {

	private Browser	browser;
	private BrowserComponent browserComp;
	
	public void initializeBrowserDelegate(Browser browser) {
		this.browser = browser;
	}

	@Override
	public void freeLastScriptAlert(String arg0) {
	}

	@Override
	public void onAddressBarChanged(Window arg0, String arg1) {
		if (null != browser) {
			browser.changeURLValue(arg1);
			//BrowserComponent.refreshComponent();
			browserComp.refreshComponent();
		}
	}

	public void setBrowserComponent(BrowserComponent bComp){
		browserComp = bComp;
	}
	
	@Override
	public void onConsoleMessage(Window arg0, String arg1, String arg2, int arg3) {
		System.err.println("browser console message : "+arg1);
	}

	@Override
	public void onCrashed(Window arg0) {
		System.err.println("brower crashed");
	}

	@Override
	public void onCrashedPlugin(Window arg0, String arg1) {
		System.err.println("browser plugin crashed :"+arg1);
	}

	@Override
	public void onCrashedWorker(Window arg0) 
	{
		System.err.println("browser worker crashed");
	}

	@Override
	public void onCreatedWindow(Window arg0, Window arg1, Rect arg2) {
	}

	@Override
	public void onExternalHost(Window arg0, String arg1, String arg2, String arg3) {
	}

	@Override
	public void onJavascriptCallback(Window arg0, String arg1, String arg2) {
		System.err.println("browser javascript callback : "+arg1+" "+arg2);
	}

	@Override
	public void onLoad(Window arg0) {
	}

	@Override
	public void onLoadingStateChanged(Window arg0, boolean arg1) {
	}

	@Override
	public boolean onNavigationRequested(Window arg0, String arg1, String arg2, boolean arg3, boolean[] arg4) {
		return false;
	}

	@Override
	public void onPaint(Window arg0, Buffer arg1, Rect arg2, Rect[] arg3, int arg4, int arg5, Rect arg6) {
	}

	@Override
	public void onPaintDone(Window arg0, Rect arg1) {
	}

	@Override
	public void onProvisionalLoadError(Window arg0, String arg1, int arg2, boolean arg3) {
	}

	@Override
	public void onResponsive(Window arg0) {
		
	}

	@Override
	public void onRunFileChooser(Window arg0, int arg1, String arg2, String arg3) {
	}

	@Override
	public void onScriptAlert(Window arg0, String arg1, String arg2, String arg3, int arg4, boolean[] arg5, String[] arg6) {
		System.err.println("browser scipt alert : "+arg1);
	}

	@Override
	public void onStartLoading(Window arg0, String arg1) {
	}

	@Override
	public void onTitleChanged(Window arg0, String arg1) {
	}

	@Override
	public void onTooltipChanged(Window arg0, String arg1) {
	}

	@Override
	public void onUnresponsive(Window arg0) {
		System.err.println("browser unresponsive");
	}
	

}