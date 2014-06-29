package utc.bsfile.gui.widget.browser;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.berkelium.java.api.Buffer;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowDelegate;
import org.mt4j.AbstractMTApplication;

import processing.core.PConstants;
import processing.core.PImage;

public class PImageAdapter extends BrowserDelegate {
	private AbstractMTApplication					mtApp;
	private PImage									img;
	private boolean									needsFullRefresh	= true;
	private Rect									updatedRect			= new Rect(0, 0, 0, 0);
	private final HashMap<Window, OnCallPaintDone>	onCallPaints		= new HashMap<Window, PImageAdapter.OnCallPaintDone>();

	public PImageAdapter(AbstractMTApplication mtApp) {
		this.mtApp = mtApp;
	}

	public void resize(int width, int height) {
		setImage(mtApp.createImage(width, height, PConstants.ARGB));
		needsFullRefresh = true;
	}

	public PImage getImage() {
		return img;
	}

	public void setImage(PImage img) {
		this.img = img;
	}

	private class OnCallPaintDone implements Runnable {
		private final AtomicBoolean	waiting	= new AtomicBoolean(false);
		private final Window		window;

		private OnCallPaintDone(Window window) {
			this.window = window;
		}

		@Override
		public void run() {
			synchronized (onCallPaints) {
				if (!waiting.getAndSet(false))
					return;
				WindowDelegate delegate = window.getDelegate();
				if (delegate != null) {
					delegate.onPaintDone(window, getUpdatedRect());
				}
			}
		}

		private void schedule() {
			if (!waiting.getAndSet(true)) {
				window.getBerkelium().execute(this);
			}
		}
	};

	private void scheduleOnPaintDone(Window window) {
		synchronized (onCallPaints) {
			OnCallPaintDone cpd = onCallPaints.get(window);
			if (cpd == null) {
				cpd = new OnCallPaintDone(window);
				onCallPaints.put(window, cpd);
			}
			cpd.schedule();
		}
	}

	@Override
	public synchronized void onPaint(Window wini, Buffer bitmap_in, Rect bitmap_rect, Rect[] copy_rects, int dx, int dy, Rect scroll_rect) {
		// we have no image yet
		if (img == null)
			return;

		if (needsFullRefresh) {
			// If we've reloaded the page and need a full update, ignore updates until a full one comes in. 
			// This handles out of date updates due to delays in event processing.
			if (handleFullUpdate(bitmap_in, bitmap_rect)) {
				updatedRect = updatedRect.grow(bitmap_rect);
				needsFullRefresh = false;
			}
			else {
				return;
			}
		}
		else {
			// Now, we first handle scrolling. We need to do this first since it requires shifting existing data, some
			// of which will be overwritten by the regular dirty rect update.
			if (dx != 0 || dy != 0) {
				handleScroll(dx, dy, scroll_rect);
			}
			handleCopyRects(bitmap_in, bitmap_rect, copy_rects);
			updatedRect = updatedRect.grow(bitmap_rect);
		}
		scheduleOnPaintDone(wini);
	}

	/**
	 * 
	 * @param buf
	 * @param rect
	 * @return
	 */
	protected boolean handleFullUpdate(Buffer buf, Rect rect) {
		final int w = img.width;
		final int h = img.height;

		if (rect.left() == 0 || rect.top() == 0 || rect.right() == w || rect.bottom() == h) {
			img.pixels = buf.getIntArray();
			return true;
		}
		return false;
	}

	private void handleScroll(int dx, int dy, Rect scroll_rect) {
		// scroll_rect contains the Rect we need to move
		// First we figure out where the data is moved to by translating it
		Rect scrolled_rect = scroll_rect.translate(-dx, -dy);
		// Next we figure out where they intersect, giving the scrolled region
		Rect scrolled_shared_rect = scroll_rect.intersect(scrolled_rect);
		// Only do scrolling if they have non-zero intersection
		if (scrolled_shared_rect.width() > 0 && scrolled_shared_rect.height() > 0) {
			PImage tempRect = img.get(scroll_rect.x, scroll_rect.y, scroll_rect.width(), scroll_rect.height());
			img.set(scrolled_rect.x + dx, scroll_rect.y + dy, tempRect);
		}
	}

	private void handleCopyRects(Buffer bitmap_in, Rect bitmap_rect, Rect[] copy_rects) {
		PImage updatedImage = mtApp.createImage(bitmap_rect.w, bitmap_rect.h, PConstants.ARGB);
		updatedImage.pixels = bitmap_in.getIntArray();

		for (int i = 0; i < copy_rects.length; i++) {
			Rect copy_rect = copy_rects[i];
			PImage tempRect = updatedImage.get(copy_rect.left() - bitmap_rect.left(), copy_rect.top() - bitmap_rect.top(), copy_rect.width(), copy_rect.height());
			img.set(copy_rect.left(), copy_rect.top(), tempRect);
		}
	}

	private Rect getUpdatedRect() {
		Rect ret = updatedRect;
		updatedRect = new Rect(0, 0, 0, 0);
		return ret;
	}
}
