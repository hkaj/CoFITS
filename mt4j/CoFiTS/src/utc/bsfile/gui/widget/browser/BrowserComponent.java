package utc.bsfile.gui.widget.browser;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import utc.bsfile.gui.widget.keyboard.BrowserKeyboard;
import utc.bsfile.util.PropertyManager;

public class BrowserComponent extends MTRoundRectangle {
	private Browser browser;
	private BrowserKeyboard keyboard;
	public MTTextField navbar;
	private BrowserComponent.NavBarInputKeyboardListener navInput;
	private AbstractMTApplication mtApp;
	private MTList listFav;
	private MTList listHistory;
	private MTList listShare;
	private MTImageButton leftButton;
	private MTImageButton rightButton;

	private MTRoundRectangle infoScreen;
	private IFont font;
	private MTColor color;
	private float cellWidth = 250;
	private float cellHeight = 25;
	private String msgEraseHistory = "SUPPRIMER L'HISTORIQUE";
	private float nbCell = 20;
	private MTColor cellFillColor;
	private MTColor cellPressedFillColor;
	private PImageAdapter pia;

	public BrowserComponent(final AbstractMTApplication mtApp, String title,
			String url, int width, int height, boolean showUrlPanel) {
		super(mtApp, 200, 150, 0, width + 50, height + 100, 25, 25);
		float zoom = Math.min(((float) mtApp.width) / (float) width,
				((float) mtApp.height) / (float) height);
		this.mtApp = mtApp;
		color = new MTColor(219, 108, 86, 255);
		cellFillColor = new MTColor(color);
		cellPressedFillColor = new MTColor(new MTColor(105, 105, 105));
		font = FontManager.getInstance().createFont(mtApp, "SansSerif", 16,
				MTColor.BLACK, false);
		this.setFillColor(color);
		MTComponent clippedChildContainer = new MTComponent(mtApp);
		MTRoundRectangle clipShape = new MTRoundRectangle(mtApp, 225, 250, 0,
				width, height, 1, 1);
		clipShape.setNoStroke(true);
		clippedChildContainer.setChildClip(new Clip(clipShape));

		browser = new Browser(mtApp, width, height, url, zoom);
		this.pia = browser.getPia();
		pia.setBrowserComponent(this);

		browser.setFillColor(new MTColor(255, 255, 255));
		browser.setStrokeColor(MTColor.WHITE);
		browser.setStrokeWeight(5);
		browser.removeAllGestureEventListeners();
		browser.installListeners(mtApp, 1);

		clippedChildContainer.addChild(browser);
		browser.translate(new Vector3D(225, 250));

		this.addChild(clippedChildContainer);

		// information window
		this.infoScreen = getRoundRectWithText(0, 0, 130, 45, "  Loading...",
				font);
		this.infoScreen.setFillColor(new MTColor(0, 0, 0, 200));
		this.infoScreen.setStrokeColor(new MTColor(0, 0, 0, 200));
		this.infoScreen.setPickable(false);
		this.infoScreen.setPositionGlobal(MT4jSettings.getInstance()
				.getWindowCenter());
		this.infoScreen.setVisible(false);
		this.addChild(infoScreen);

		/****************************************************************************
		 * *********PARTIE BOUTON GO BACK **********************************
		 ****************************************************************************/
		leftButton = new MTImageButton(mtApp, mtApp.loadImage(PropertyManager
				.getInstance().getProperty(PropertyManager.ICONE_DIR)
				+ "browser/back_alt.png"));
		leftButton.setVisible(false);
		leftButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							browser.getWindow().goBack();
							refreshComponent();
						}
						return false;
					}
				});
		leftButton.setNoStroke(true);
		this.addChild(leftButton);
		leftButton.translate(new Vector3D(225, 200, 0));

		/****************************************************************************
		 * *********PARTIE BOUTON GO FORWARD **********************************
		 ****************************************************************************/
		rightButton = new MTImageButton(mtApp, mtApp.loadImage(PropertyManager
				.getInstance().getProperty(PropertyManager.ICONE_DIR)
				+ "browser/forward_alt.png"));
		rightButton.setVisible(false);

		rightButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							browser.getWindow().goForward();
							refreshComponent();
						}
						return false;
					}
				});
		rightButton.setNoStroke(true);
		this.addChild(rightButton);
		rightButton.translate(new Vector3D(275, 200, 0));

		/****************************************************************************
		 * *********PARTIE BOUTON REFRESH **********************************
		 ****************************************************************************/
		MTImageButton reloadButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/refresh.png"));
		reloadButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							browser.getWindow().refresh();
						}
						return false;
					}
				});
		reloadButton.setNoStroke(true);
		this.addChild(reloadButton);
		reloadButton.translate(new Vector3D(325, 200, 0));

		/****************************************************************************
		 * *********PARTIE PAGE D'ACCUEIL **********************************
		 ****************************************************************************/
		final MTImageButton homeButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/home.png"));

		homeButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
						}
						return false;
					}
				});

		homeButton.registerInputProcessor(new TapAndHoldProcessor(mtApp, 1000));
		homeButton.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		homeButton.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {

							}
						return false;
					}
				});

		homeButton.setNoStroke(true);
		this.addChild(homeButton);
		homeButton.translate(new Vector3D(375, 200, 0));

		/****************************************************************************
		 * *********PARTIE BARRE DE NAVIGATION
		 * **********************************
		 ****************************************************************************/
		navbar = new MTTextField(mtApp, 0, 0, 400, 30, FontManager
				.getInstance().getDefaultFont(mtApp));
		browser.setUrlField(navbar);
		navbar.unregisterAllInputProcessors();
		navbar.removeAllGestureEventListeners();
		navbar.setText("http://www.google.com");
		navbar.setFillColor(MTColor.WHITE);
		navbar.setNoStroke(true);
		navbar.translate(new Vector3D(450, 200));
		navInput = new NavBarInputKeyboardListener();

		keyboard = new BrowserKeyboard(mtApp);
		keyboard.setVisible(false);
		keyboard.translate(new Vector3D(500, 500));
		keyboard.setFillColor(new MTColor(80, 80, 80, 130));
		keyboard.setPositionRelativeToParent(this.getCenterPointLocal());
		this.addChild(keyboard);

		navbar.addInputListener(new IMTInputEventListener() {
			public boolean processInputEvent(MTInputEvent inEvt) {
				inEvt.stopPropagation();
				keyboard.setVisible(true);
				navbar.setEnableCaret(true);
				keyboard.addTextInputListener(navInput);
				return false;
			}
		});
		this.addChild(navbar);

		/****************************************************************************
		 * *********PARTIE PARTAGE **********************************
		 ****************************************************************************/

		listShare = new MTList(mtApp, 200 + 5, 0, cellWidth + 2, nbCell
				* cellHeight + nbCell * 3);
		listShare.setNoFill(true);
		listShare.setNoStroke(true);
		listShare.unregisterAllInputProcessors();
		listShare.setAnchor(PositionAnchor.LOWER_LEFT);
		listShare.setVisible(false);

		genererListeItemPartage(font, cellWidth, cellHeight, cellFillColor,
				cellPressedFillColor);

		listShare.translate(new Vector3D(950, 150, 0));
		this.addChild(listShare);

		MTImageButton shareButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/sharethis.png"));
		shareButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							try {
								if (listShare.isVisible()) {
									listShare.setVisible(false);
								} else {
									genererListeItemPartage(font, cellWidth,
											cellHeight, cellFillColor,
											cellPressedFillColor);
									listHistory.setVisible(false);
									listFav.setVisible(false);
									listShare.setVisible(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return false;
					}
				});
		shareButton.setNoStroke(true);
		this.addChild(shareButton);
		shareButton.translate(new Vector3D(990, 200, 0));

		shareButton
				.registerInputProcessor(new TapAndHoldProcessor(mtApp, 1000));
		shareButton.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		shareButton.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {

							}
						return false;
					}
				});

		/****************************************************************************
		 * *********PARTIE FAVORIS**********************************
		 ****************************************************************************/

		MTImageButton listFavButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/star.png"));
		listFavButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {

						}
						return false;
					}
				});
		listFavButton.setNoStroke(true);
		this.addChild(listFavButton);
		listFavButton.translate(new Vector3D(1070, 200, 0));

		listFavButton.registerInputProcessor(new TapAndHoldProcessor(mtApp,
				1000));
		listFavButton.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		listFavButton.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {

							}
						return false;
					}
				});

		listFav = new MTList(mtApp, 200 + 5, 0, cellWidth + 2, nbCell
				* cellHeight + nbCell * 3);
		listFav.setNoFill(true);
		listFav.setNoStroke(true);
		listFav.unregisterAllInputProcessors();
		listFav.setAnchor(PositionAnchor.UPPER_LEFT);
		listFav.setVisible(false);

		listFav.translate(new Vector3D(950, 150, 0));
		this.addChild(listFav);

		/****************************************************************************
		 * *********PARTIE Historique**********************************
		 ****************************************************************************/

		MTImageButton historyButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/library_bookmarked.png"));

		historyButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							try {
								if (listHistory.isVisible()) {
									listHistory.setVisible(false);
								} else {
									listFav.setVisible(false);
									listShare.setVisible(false);
									listHistory.setVisible(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return false;
					}
				});

		historyButton.registerInputProcessor(new TapAndHoldProcessor(mtApp,
				1000));
		historyButton.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		historyButton.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {
							}
						return false;
					}
				});
		historyButton.setNoStroke(true);
		this.addChild(historyButton);
		historyButton.translate(new Vector3D(1030, 200, 0));

		/****************************************************************************
		 * *********PARTIE CLAVIER **********************************
		 ****************************************************************************/
		MTImageButton keyboardButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "browser/keyboard.png"));
		keyboardButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							System.out.println("point : "
									+ browser.getCenterPointLocal());
							browser.getKeyboard().setPositionRelativeToParent(
									browser.getCenterPointLocal());
							if (browser.getKeyboard().isVisible())
								browser.getKeyboard().setVisible(false);
							else
								browser.getKeyboard().setVisible(true);
						}
						return false;
					}
				});

		keyboardButton.registerInputProcessor(new TapAndHoldProcessor(mtApp,
				1000));
		keyboardButton.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		keyboardButton.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {
								navbar.setText("http://");
							}
						return false;
					}
				});

		keyboardButton.setNoStroke(true);
		this.addChild(keyboardButton);
		keyboardButton.translate(new Vector3D(870, 200, 0));

		BrowserKeyboard keyboardBrowser = new BrowserKeyboard(mtApp);
		keyboardBrowser.setVisible(false);
		keyboardBrowser.setStrokeColor(MTColor.GRAY);
		keyboardBrowser.translate(new Vector3D(500, 500));
		keyboardBrowser.setPositionRelativeToParent(this.getCenterPointLocal());
		this.addChild(keyboardBrowser);
		browser.setKeyboard(keyboardBrowser);

		/****************************************************************************
		 * *********PARTIE BOUTON CLOSE **********************************
		 ****************************************************************************/
		MTImageButton closeButton = new MTImageButton(mtApp,
				mtApp.loadImage(PropertyManager.getInstance().getProperty(
						PropertyManager.ICONE_DIR)
						+ "close.png"));
		closeButton.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.isTapped()) {
							browser.destroy();
							destroy();
						}
						return false;
					}
				});
		closeButton.setNoStroke(true);
		this.addChild(closeButton);
		closeButton.translate(new Vector3D(1127, 135, 0));
	}

	private class NavBarInputKeyboardListener implements ITextInputListener {
		@Override
		public void setText(String text) {
			navbar.setText(text);
		}

		@Override
		public void removeLastCharacter() {
			navbar.removeLastCharacter();
		}

		@Override
		public void clear() {
			navbar.clear();
		}

		@Override
		public void appendText(String text) {
			navbar.appendText(text);
		}

		@Override
		public void appendCharByUnicode(String unicode) {
			if (unicode.equals("\n")) {
				navbar.setEnableCaret(false);
				browser.getWindow().navigateTo(navbar.getText());
				keyboard.setVisible(false);
				refreshComponent();
			} else {
				navbar.appendCharByUnicode(unicode);
			}
		}
	}

	private MTListCell createListCell(final String bookmark, IFont font,
			float cellWidth, float cellHeight, final MTColor cellFillColor,
			final MTColor cellPressedFillColor) {
		final MTListCell cell = new MTListCell(mtApp, cellWidth, cellHeight);
		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(mtApp, font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		listLabel.setText(bookmark);
		cell.addChild(listLabel);
		if (bookmark == msgEraseHistory)
			listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(mtApp, 15));
		cell.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						switch (te.getTapID()) {
						case TapEvent.TAP_DOWN:
							cell.setFillColor(cellPressedFillColor);
							break;
						case TapEvent.TAP_UP:
							cell.setFillColor(cellFillColor);
							break;
						case TapEvent.TAPPED:
							// System.out.println("Button clicked: " + label);
							cell.setFillColor(cellFillColor);
							listFav.setVisible(false);
							listHistory.setVisible(false);
							infoScreen.setVisible(true);
							mtApp.getCurrentScene().registerPreDrawAction(
									new IPreDrawAction() {
										public void processAction() {
											mtApp.invokeLater(new Runnable() {
												public void run() {
													System.out
															.println("Chargement page : "
																	+ bookmark);
													navbar.setText(bookmark);
													browser.getWindow()
															.navigateTo(
																	navbar.getText());
													infoScreen
															.setVisible(false);
												}
											});
										}

										public boolean isLoop() {
											return false;
										}
									});
						}
						return false;
					}
				});

		cell.registerInputProcessor(new TapProcessor(mtApp, 15));
		cell.registerInputProcessor(new TapAndHoldProcessor(mtApp, 1000));
		cell.addGestureListener(TapAndHoldProcessor.class,
				new TapAndHoldVisualizer(mtApp, this));
		cell.addGestureListener(TapAndHoldProcessor.class,
				new IGestureEventListener() {
					@Override
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapAndHoldEvent te = (TapAndHoldEvent) ge;
						if (te.getId() == TapAndHoldEvent.GESTURE_ENDED)
							if (te.isHoldComplete()) {
								infoScreen.setVisible(false);
							}
						return false;
					}
				});
		return cell;
	}

	private MTListCell createListCellPartage(final String sendTo, IFont font,
			float cellWidth, float cellHeight, final MTColor cellFillColor,
			final MTColor cellPressedFillColor) {
		final MTListCell cell = new MTListCell(mtApp, cellWidth, cellHeight);
		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(mtApp, font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		listLabel.setText(sendTo);
		cell.addChild(listLabel);
		listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(mtApp, 15));
		cell.addGestureListener(TapProcessor.class,
				new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						switch (te.getTapID()) {
						case TapEvent.TAP_DOWN:
							cell.setFillColor(cellPressedFillColor);
							break;
						case TapEvent.TAP_UP:
							cell.setFillColor(cellFillColor);
							break;
						case TapEvent.TAPPED:
							// System.out.println("Button clicked: " + label);
							cell.setFillColor(cellFillColor);
							listFav.setVisible(false);
							infoScreen.setVisible(true);
							mtApp.getCurrentScene().registerPreDrawAction(
									new IPreDrawAction() {
										public void processAction() {
											mtApp.invokeLater(new Runnable() {
												public void run() {

													try {
														AbstractShape component;

														component = new BrowserComponent(
																mtApp,
																"Browser",
																navbar.getText(),
																900, 450, true);
														IBoundingShape bounds = component
																.getBounds();
													} catch (Exception e) {
														e.printStackTrace();
													}

													infoScreen
															.setVisible(false);
												}
											});
										}

										public boolean isLoop() {
											return false;
										}
									});
							break;
						}
						return false;
					}
				});
		return cell;
	}

	private MTRoundRectangle getRoundRectWithText(float x, float y,
			float width, float height, String text, IFont font) {
		MTRoundRectangle r = new MTRoundRectangle(mtApp, x, y, 0, width,
				height, 12, 12);
		r.unregisterAllInputProcessors();
		r.setFillColor(color);
		r.setStrokeColor(MTColor.BLACK);
		MTTextArea rText = new MTTextArea(mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(text);
		r.addChild(rText);
		rText.setPositionRelativeToParent(r.getCenterPointLocal());
		return r;
	}

	public void genererListeItemPartage(IFont font, float cellWidth,
			float cellHeight, final MTColor cellFillColor,
			final MTColor cellPressedFillColor) {
		listShare.removeAllListElements();
	}

	public void refreshComponent() {
		if (navbar != null)

			if (browser.getWindow().canGoBack()) {
				leftButton.setVisible(true);
			} else {
				leftButton.setVisible(false);
			}

		if (browser.getWindow().canGoForward()) {
			rightButton.setVisible(true);
		} else {
			rightButton.setVisible(false);
		}
	}
}
