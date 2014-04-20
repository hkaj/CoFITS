package utc.tatinpic.gui.widgets.browser;

import java.awt.event.MouseEvent;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Window;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.input.IKeyListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLTexture;

import processing.core.PConstants;
import processing.core.PImage;
import utc.tatinpic.gui.widgets.keyboard.DefaultKeyboard;

public class Browser extends MTRectangle implements IKeyListener {

	private static final Berkelium RUNTIME = Berkelium
			.createMultiThreadInstance();
	public static final float URL_PANEL_HEIGHT = 30f; // Float.parseFloat(SizeManager.getInstance().getProperty("browser.url_panel_height"));
	public static final float ICON_SIZE = 80f; // Float.parseFloat(SizeManager.getInstance().getProperty("browser.icon_size"));

	private final Window win = RUNTIME.createWindow();
	private final PImageAdapter pia;
	private String url;
	private AbstractMTApplication mtApp;
	private MTTextField urlField;
	private float initialHeight;

	private BrowserKeyboard keyboard;
	private NavBarInputKeyboardListener navInput;

	public Browser(AbstractMTApplication mtApp, int width, int height,
			String url, final float scaleFactor) {
		super(mtApp, mtApp.createImage(width, height, PConstants.ARGB));
		this.url = url;
		this.mtApp = mtApp;
		initialHeight = this.getBounds().getHeightXY(TransformSpace.GLOBAL);

		this.pia = new PImageAdapter(mtApp);
		this.pia.resize(width, height);

		synchronized (RUNTIME) {
			this.win.setDelegate(pia);
			this.win.resize(width, height);
			this.win.navigateTo(this.url);

			this.mtApp.addKeyListener(this);
			this.setAnchor(PositionAnchor.UPPER_LEFT);
			this.removeAllGestureEventListeners();
			installListeners(mtApp, scaleFactor);
			this.pia.initializeBrowserDelegate(this);
		}
	}



	public PImageAdapter getPia() {
		return pia;
	}

	public void setKeyboard(BrowserKeyboard keyboard) {
		this.keyboard = keyboard;
		navInput = new NavBarInputKeyboardListener();
		this.keyboard.addTextInputListener(navInput);
	}

	public void setUrlField(MTTextField mttext) {
		urlField = mttext;
	}

	public void installListeners(final AbstractMTApplication mtApp,
			final float scaleFactor) {
		float currentHeight = this.getBounds().getHeightXY(TransformSpace.GLOBAL);
		float scale = (float) currentHeight / initialHeight;

		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class,
				new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				final DragEvent de = (DragEvent) ge;
				Matrix m = getGlobalInverseMatrix();
				final Vector3D vect = de.getTranslationVect();
				m.rotateVect(vect);
				if (de.getId() == MTGestureEvent.GESTURE_UPDATED) {
					RUNTIME.execute(new Runnable() {
						@Override
						public void run() {
							if (Math.abs(vect.x) > Math.abs(vect.y)) {
								win.mouseWheel((int) vect.x,0);
							} else {
								win.mouseWheel(0,(int)vect.y);
							}
						}
					});

				}
				return false;
			}
		});
		this.registerInputProcessor(new TapProcessor(mtApp));

		//		this.addGestureListener(TapAndHoldProcessor.class,
		//				new IGestureEventListener() {
		//
		//					@Override
		//					public boolean processGestureEvent(MTGestureEvent ge) {
		//						final TapAndHoldEvent tahe = (TapAndHoldEvent) ge;
		//
		//						if (tahe.getId() == MTGestureEvent.GESTURE_ENDED) {
		//							if (tahe.getElapsedTime() >= 1000) {
		//								RUNTIME.execute(new Runnable() {
		//									@Override
		//									public void run() {
		//										win.getBerkelium().execute(
		//												new Runnable() {
		//													public void run() {
		//														keyboard.setVisible(true);
		//														win.focus();
		//													}
		//												});
		//									}
		//								});
		//							}
		//						}
		//						return false;
		//					}
		//				});
		//		this.registerInputProcessor(new TapAndHoldProcessor(mtApp));

		this.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				final TapEvent te = (TapEvent) ge;
				Matrix m = getGlobalInverseMatrix();
				final Vector3D vect = te.getCursor().getPosition();
				vect.transform(m);

				if (te.getId() == MTGestureEvent.GESTURE_STARTED) {
					RUNTIME.execute(new Runnable() {
						@Override
						public void run() {
							win.mouseMoved((int) vect.x, (int) vect.y);
							win.mouseButton(MouseEvent.BUTTON1, true);
						}
					});
				} else if (te.getId() == MTGestureEvent.GESTURE_ENDED) {
					RUNTIME.execute(new Runnable() {
						@Override
						public void run() {
							win.mouseMoved((int) vect.x, (int) vect.y);
							win.mouseButton(MouseEvent.BUTTON1, false);
						}
					});
				}
				return false;
			}
		});


		this.registerInputProcessor(new TapAndHoldProcessor(mtApp, 1000));
		this.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		this.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent te = (TapAndHoldEvent) ge;
				if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
					if (te.isHoldComplete()) {
						keyboard.setVisible(true);
					}
				return false;
			}
		});

		this.registerInputProcessor(new FlickProcessor());
		this.addGestureListener(FlickProcessor.class,
				new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent) ge;

				if (e.getId() == MTGestureEvent.GESTURE_ENDED
						&& e.isFlick()) {
					if (e.getDirection() == FlickDirection.EAST) {
						win.goForward();
					} else if (e.getDirection() == FlickDirection.WEST) {
						win.goBack();
					}
				}
				return false;
			}
		});

	}

	@Override
	public void updateComponent(long timeDelta) {
		super.updateComponent(timeDelta);
		PImage img = pia.getImage();
		if (img != null) {
			// do not allow updates to the image while we draw it
			synchronized (pia) {
				((GLTexture) this.getTexture()).updateGLTexture(img.pixels);
			}
		}
	}

	public void changeURLValue(final String newUrl) {
		mtApp.invokeLater(new Runnable() {
			@Override
			public void run() {
				urlField.setText(newUrl);
			}
		});
	}

	public Window getWindow() {
		return win;
	}

	public DefaultKeyboard getKeyboard(){
		return keyboard;
	}

	private class NavBarInputKeyboardListener implements ITextInputListener {
		@Override
		public void setText(final String text) {
			win.getBerkelium().execute(new Runnable() {
				@Override
				public void run() {
					win.textEvent(text);
				}
			});
		}

		@Override
		public void removeLastCharacter() {
			win.getBerkelium().execute(new Runnable() {
				@Override
				public void run() {
					win.keyEvent(true, 0, 8, 8);
				}
			});
		}

		@Override
		public void clear() {
			System.out.println("clear");
		}

		@Override
		public void appendText(final String text) {
			win.getBerkelium().execute(new Runnable() {
				@Override
				public void run() {
					win.textEvent(text);
				}
			});
		}

		@Override
		public void appendCharByUnicode(final String unicode) {
			win.getBerkelium().execute(new Runnable() {
				@Override
				public void run() {
					win.textEvent(unicode);
				}
			});
		}
	}

	@Override
	public void keyPressed(final char key, final int keyCode) {
		System.out.println("Press Key : " + key + " keyCode : " + keyCode);
		// textBuffer.append(key);
		if (keyCode >= 30 && keyCode <= 126) {
			RUNTIME.execute(new Runnable() {
				@Override
				public void run() {
					win.textEvent(String.valueOf(key));
					// win.keyEvent(true, 0, keyCode, key);
					// textBuffer.delete(0, textBuffer.length());
				}
			});
		} else {
			RUNTIME.execute(new Runnable() {
				@Override
				public void run() {
					// win.textEvent(String.valueOf(key));
					win.keyEvent(true, 0, keyCode, key);
					// textBuffer.delete(0, textBuffer.length());
				}
			});
		}

	}

	@Override
	public void keyRleased(final char key, final int keyCode) {
	}
}
