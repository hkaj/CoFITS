package utc.tatinpic.gui.widgets.browser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class BrowserScene extends AbstractScene
{

	private Browser browser;
	private MTKeyboard keyboard;
	private MTTextField navbar;
	private BrowserScene.NavBarInputKeyboardListener navInput;

	public BrowserScene(AbstractMTApplication mtApp, String title, String url, int width, int height, boolean showUrlPanel)
	{

		super(mtApp, title);
		float zoom = Math.min(((float) mtApp.width) / (float) width, ((float) mtApp.height) / (float) height);

		MTRoundRectangle coque = new MTRoundRectangle(mtApp, 200, 150, 0, width + 50, height + 100, 25, 25);
		coque.setFillColor(MTColor.GREY);

		MTComponent clippedChildContainer = new MTComponent(mtApp);
		MTRoundRectangle clipShape = new MTRoundRectangle(mtApp, 225, 250, 0, width, height, 1, 1);
		clipShape.setNoStroke(true);
		clippedChildContainer.setChildClip(new Clip(clipShape));
		coque.addChild(clippedChildContainer);

		browser = new Browser(mtApp, width, height, url, zoom);
		browser.setFillColor(new MTColor(255, 255, 255));
		browser.setStrokeColor(MTColor.WHITE);
		browser.setStrokeWeight(5);

		clippedChildContainer.addChild(browser);
		browser.translate(new Vector3D(225, 250));

		coque.addChild(clippedChildContainer);

		/*
		 * BOUTON AjoutFavoris
		 */
		MTImageButton addFavButton = new MTImageButton(mtApp, mtApp.loadImage(getClass().getClassLoader().getResource("favoris.png").toString()));
		addFavButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					PrintWriter fav;
					int n = 5;

					try
					{
						String adressedufichier = System.getProperty("user.dir") +"\\src\\"+ "favoris.txt";
						fav = new PrintWriter(new BufferedWriter(new FileWriter(adressedufichier, true)));
						String txt = navbar.getText();
						System.out.println("Ecriture dans "+ adressedufichier +" : " + txt);
						fav.println(txt);
						fav.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				return false;
			}
		});
		addFavButton.setNoStroke(true);
		coque.addChild(addFavButton);
		addFavButton.translate(new Vector3D(1100, 200, 0));

		/*
		 * BOUTON GO BACK
		 */
		MTImageButton leftButton = new MTImageButton(mtApp, mtApp.loadImage(getClass().getClassLoader().getResource("arrow_left_32x32.png").toString()));
		leftButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					browser.getWindow().goBack();
				}
				return false;
			}
		});
		leftButton.setNoStroke(true);
		coque.addChild(leftButton);
		leftButton.translate(new Vector3D(225, 200, 0));

		/*
		 * BOUTON GO FORWARD
		 */
		MTImageButton rightButton = new MTImageButton(mtApp, mtApp.loadImage(getClass().getClassLoader().getResource("arrow_right_32x32.png").toString()));
		rightButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					browser.getWindow().goForward();
				}
				return false;
			}
		});
		rightButton.setNoStroke(true);
		coque.addChild(rightButton);
		rightButton.translate(new Vector3D(300, 200, 0));

		/*
		 * BOUTON REFRESH
		 */
		MTImageButton reloadButton = new MTImageButton(mtApp, mtApp.loadImage(getClass().getClassLoader().getResource("reload_24x28.png").toString()));
		reloadButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					browser.getWindow().refresh();
				}
				return false;
			}
		});
		reloadButton.setNoStroke(true);
		coque.addChild(reloadButton);
		reloadButton.translate(new Vector3D(375, 200, 0));

		/*
		 * CHAMP TEXTE URL
		 */
		navbar = new MTTextField(mtApp, 0, 0, 400, 30, FontManager.getInstance().getDefaultFont(mtApp));
		browser.setUrlField(navbar);
		navbar.unregisterAllInputProcessors();
		navbar.removeAllGestureEventListeners();
		navbar.setText("http://www.google.com");
		navbar.setFillColor(MTColor.WHITE);
		navbar.setNoStroke(true);
		navbar.translate(new Vector3D(450, 200));
		navInput = new NavBarInputKeyboardListener();

		//TODO cangoback, home, changement url
		// TODO Integrer clavier de tatin DefaultKeyboard quand ce sera sur le
		// projet tatin
		keyboard = new BKeyboard(mtApp);
		keyboard.setVisible(false);
		keyboard.translate(new Vector3D(500, 500));
		keyboard.setFillColor(MTColor.BLUE);
		keyboard.setPositionRelativeToParent(coque.getCenterPointLocal());
		coque.addChild(keyboard);
		navbar.addInputListener(new IMTInputEventListener()
		{
			public boolean processInputEvent(MTInputEvent inEvt)
			{
				inEvt.stopPropagation();
				keyboard.setVisible(true);
				navbar.setEnableCaret(true);
				keyboard.addTextInputListener(navInput);
				return false;
			}
		});
		coque.addChild(navbar);
		getCanvas().addChild(coque);
	}

	private class NavBarInputKeyboardListener implements ITextInputListener
	{
		@Override
		public void setText(String text)
		{
			navbar.setText(text);
		}

		@Override
		public void removeLastCharacter()
		{
			navbar.removeLastCharacter();
		}

		@Override
		public void clear()
		{
			navbar.clear();
		}

		@Override
		public void appendText(String text)
		{
			navbar.appendText(text);
		}

		@Override
		public void appendCharByUnicode(String unicode)
		{
			if (unicode.equals("\n"))
			{
				navbar.setEnableCaret(false);
				browser.getWindow().navigateTo(navbar.getText());
				keyboard.setVisible(false);
			}
			else
			{
				navbar.appendCharByUnicode(unicode);
			}
		}
	}

	private class BKeyboard extends MTKeyboard
	{
		public BKeyboard(PApplet pApplet)
		{
			super(pApplet);
		}

		@Override
		protected void onCloseButtonClicked()
		{
			this.setVisible(false);
		}
	}

}
