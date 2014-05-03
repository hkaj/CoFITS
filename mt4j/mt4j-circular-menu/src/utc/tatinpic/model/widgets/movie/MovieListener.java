package utc.tatinpic.model.widgets.movie;

import java.util.EventListener;

import codeanticode.gsvideo.GSMovie;

public interface MovieListener extends EventListener {

	public void onPlay();

	public void onPause();

	public void onStop();

	public void onNewFrame(GSMovie m);

	public void onFirstFrame(GSMovie m);

	public void onLoop();

	public void onJump(GSMovie m);
}
