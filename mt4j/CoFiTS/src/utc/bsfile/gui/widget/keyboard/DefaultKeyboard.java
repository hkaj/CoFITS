/**
 * 
 */
package utc.bsfile.gui.widget.keyboard;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.mt4j.IMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.BoundsArbitraryPlanarPolygon;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.font.VectorFontCharacter;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKey;
import org.mt4j.input.IKeyListener;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import utc.bsfile.gui.widget.controlorb.ControlOrb;
import utc.bsfile.gui.widget.keyboard.KeyInfoFactory.KeyInfo;

/**
 * @author Claude Moulin
 * 
 */
public class DefaultKeyboard extends MTRoundRectangle implements IKeyListener
{
	public static String NAME = "default mt-keyboard";	
	/**
	 * @uml.property  name="keybCloseSvg"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MTSvgButton keybCloseSvg;

	/**
	 * The pa.
	 * @uml.property  name="pa"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private PApplet pa;

	/**
	 * The key font.
	 * @uml.property  name="keyFont"
	 * @uml.associationEnd  
	 */
	private IFont keyFont;

	/**
	 * The key list.
	 * @uml.property  name="keyList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.mt4j.components.visibleComponents.widgets.keyboard.MTKey"
	 */
	private ArrayList<MTKey> keyList;

	/**
	 * The shift changers.
	 * @uml.property  name="shiftChangers"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.mt4j.components.visibleComponents.widgets.keyboard.MTKey"
	 */
	private ArrayList<MTKey> shiftChangers;

	/**
	 * The shift pressed.
	 * @uml.property  name="shiftPressed"
	 */
	private boolean shiftPressed;

	/**
	 * The key click action.
	 * @uml.property  name="keyClickAction"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:utc.tatinpic.gui.widgets.keyboard.Keyboard$KeyClickAction"
	 */
	private KeyClickAction keyClickAction;

	/**
	 * The text input acceptors.
	 * @uml.property  name="textInputAcceptors"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener"
	 */
	private List<ITextInputListener> textInputAcceptors;
	
	
	int lock = 0;
	private class StoredClick
	{
		public Vector3D pos;
		public MTKey key;
		
		public StoredClick(InputCursor c,MTKey key)
		{
			pos = c.getPosition();
			this.key = key;
		}
	}
	private List<StoredClick> waitingCursor;

	/**
	 * @uml.property  name="hardwareInput"
	 */
	private boolean hardwareInput;
	/**
	 * list of listener for keyboard close button events
	 */
	private HashSet<CloseKBListener> cllisteners;
	
	private long lastPressed = -1;

	/**
	 * Creates a new keyboard without an text input acceptor.
	 * 
	 * @param pApplet the applet
	 */
	
	public DefaultKeyboard(PApplet pApplet) {
		this(pApplet, 0, 0, 0, 700, 245, 30, 30);
	}
		
	public DefaultKeyboard(PApplet pApplet,float x, float y, float z, float width, float height, float arcWidth, float arcHeight)
	{
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
		this.pa = pApplet;
		// Set drawing mode
		this.setDrawSmooth(true);

		this.setHardwareInputEnabled(true);

		this.setName(NAME);
		this.textInputAcceptors = new ArrayList<ITextInputListener>();
		
		this.cllisteners  = new HashSet<CloseKBListener>();
		
		waitingCursor = new ArrayList<StoredClick>();

		//if (MT4jSettings.getInstance().isOpenGlMode())
		// 	this.setUseDirectGL(true);

		// TODO button textarea clear
		// TODO keyboard animated creation

		MTColor keyColor = new MTColor(0, 0, 0, 255);

		// INIT FIELDS
		// Load the Key font
		keyFont = FontManager.getInstance().createFont(pa, "keys.svg", 30, keyColor);

		keyList = new ArrayList<MTKey>();
		shiftChangers = new ArrayList<MTKey>();
		shiftPressed = false;
		keyClickAction = new KeyClickAction();

		// /*
		// TODO load button only once!
		keybCloseSvg = new MTSvgButton(pa, MT4jSettings.getInstance().getDefaultSVGPath() + "keybClose.svg");
		// Transform
		//this.addChild(keybCloseSvg);
		keybCloseSvg.scale(0.8f*(width/700), 0.8f*(width/700), 1, new Vector3D(0, 0, 0));
		keybCloseSvg.translate(new Vector3D(640*(width/700), 5*(width/700), 0));
		keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		
		/* Problem with close button:
		 * on tap down, the button shrinks in size
		 * on tap up, the finger can possible by off target now, because the button is smaller
		 * therefor no "tapped" event is triggered
		 * Don't know this is all of a sudden happening... -AJ
		 */
		keybCloseSvg.removeAllGestureEventListeners();
		keybCloseSvg.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent) ge;
				if (te.isTapped())
				{
					onCloseButtonClicked();
				}
				return false;
			}
		});
		this.addChild(keybCloseSvg);
		// */

		/*
		 * We get the key's shapes from the keys.svg svg font. To save memory, we share the font's characters geometryinfo with the MTKey shapes.
		 */

		// INITIALIZE SPACE-Button "by hand"
		VectorFontCharacter SpaceF = (VectorFontCharacter) keyFont.getFontCharacterByUnicode("k");
		MTKey space = new MTKey(/* spaceOutlines, */pa, SpaceF.getGeometryInfo(), " ", " ");
		this.addChild(space);
		space.setName(SpaceF.getName());
		// Set the contours to draw the outline - do this after settings the geominfo
		// so the outline displaylist will be overridden with the new contours list
		space.setOutlineContours(SpaceF.getContours()); // FIXME do we have to in opengl mode? -> we will use displayLists anyway..
		if (MT4jSettings.getInstance().isOpenGlMode())
		{
			space.setUseDirectGL(true);
			// System.out.println(SpaceF.getDisplayListIDs()[0] + "  - " + SpaceF.getDisplayListIDs()[1]);
			space.getGeometryInfo().setDisplayListIDs(SpaceF.getGeometryInfo().getDisplayListIDs()); // Wenn nicht displaylisten, m�ssen wir geometry auch �bernehmen!
			space.setUseDisplayList(true);
		}
		space.setFillColor(keyColor);
		space.setNoStroke(false);
		space.setDrawSmooth(true);
		space.setPickable(true);
		space.setGestureAllowance(DragProcessor.class, false);
		space.setGestureAllowance(RotateProcessor.class, false);
		space.setGestureAllowance(ScaleProcessor.class, false);
		space.unregisterAllInputProcessors();
		scaleKey(space, 40* (height/245));
		space.setPositionRelativeToParent(new Vector3D(350*(width/700), 210*(height/245), 0));
		space.setGestureAllowance(TapProcessor.class, true);
		space.registerInputProcessor(new TapProcessor(pa));
		space.addGestureListener(TapProcessor.class, keyClickAction);
		SpaceF = null;
		keyList.add(space);
		//this.addChild(space);

		KeyInfo[] keyInfos = this.getKeysLayout(width, height);

		// CREATE THE KEYS \\
		for (KeyInfo keyInfo : keyInfos) {
			VectorFontCharacter fontChar = (VectorFontCharacter) keyFont.getFontCharacterByUnicode(keyInfo.keyfontUnicode);
			MTKey key = new MTKey(pa, fontChar.getGeometryInfo(), keyInfo.charUnicodeToWrite, keyInfo.charUnicodeToWriteShifted);
			this.addChild(key);
			key.setName(fontChar.getName());
			key.setPickable(true);
			key.setFillColor(keyColor);
			//key.scale(2f, 2f, 2f, key.getCenterPointLocal());
			key.unregisterAllInputProcessors();

			key.setOutlineContours(fontChar.getContours());
			if (MT4jSettings.getInstance().isOpenGlMode())
			{
				key.setUseDirectGL(true);
				// Use the display lists already created for the font characters of the key-font
				key.getGeometryInfo().setDisplayListIDs(fontChar.getGeometryInfo().getDisplayListIDs());
				key.setUseDisplayList(true);
			}

			scaleKey(key, (43* (height/245)));

			// Scale ENTER and BACKSPACE
			if (key.getCharacterToWrite().equals("\n"))
			{
				key.scale(1.70f, 1.70f, 1, key.getCenterPointLocal(), TransformSpace.LOCAL);
			}

			key.setPositionRelativeToParent(keyInfo.position);

			// this is a hack to fit the bounding shape of the "enter" key to its non-rectangular shape
			if (key.getCharacterToWrite().equals("\n"))
			{
				Vector3D[] v = key.getBounds().getVectorsLocal();
				float indent = (v[1].getX() - v[0].getX()) / 2f;
				Vertex[] vNew = new Vertex[] { new Vertex(v[0].getX(), v[0].getY() + indent, 0), new Vertex(v[0].getX() + indent - indent / 8f, v[0].getY() + indent, 0) //
				        , new Vertex(v[0].getX() + indent - indent / 8f, v[0].getY(), 0) //
				        , new Vertex(v[1]), new Vertex(v[2]), new Vertex(v[3]), new Vertex(v[0].getX(), v[0].getY() + indent, 0) };
				BoundsArbitraryPlanarPolygon newBounds = new BoundsArbitraryPlanarPolygon(key, vNew); // Expensive..
				key.setBoundsBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
				key.setBounds(newBounds);
			}

			keyList.add(key);
			key.setGestureAllowance(TapProcessor.class, true);
			TapProcessor tp = new TapProcessor(pa);
			// tp.setLockPriority(1.5f);
			tp.setLockPriority(5f); // FIXME TEST
			tp.setStopPropagation(false);
			key.registerInputProcessor(tp);
			key.addGestureListener(TapProcessor.class, keyClickAction);

			// Add keys that change during SHIFT to a list
			if (keyInfo.visibilityInfo == KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED)
			{
				shiftChangers.add(key);
			}
			else if (keyInfo.visibilityInfo == KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED)
			{
				key.setVisible(false);
				shiftChangers.add(key);
			}

			fontChar = null;
			//this.addChild(key);
		}

		// Draw this component and its children above
		// everything previously drawn and avoid z-fighting
		this.setDepthBufferDisabled(true);
	}

	private KeyInfo[] getKeysLayout(float width, float height)
	{
		Locale l = Locale.getDefault();
		/*
		 * System.out.println("   Language, Country, Variant, Name"); System.out.println(""); System.out.println("Default locale: "); System.out.println("   "+l.getLanguage()+", "+l.getCountry()+", "
		 * +", "+l.getVariant()+", "+l.getDisplayName());
		 */
		if (l.getLanguage().equalsIgnoreCase(Locale.GERMANY.getLanguage()))
		{
			return KeyInfoFactory.getInstance().getGermanLayout();
		}
		else
		{
			//return KeyInfoFactory.getInstance().getUSLayout();
			return KeyInfoFactory.getInstance().getFrenchLayout(width, height);
		}
	}

	

	
	private void scaleKey(MTKey key, float scale)
	{
		Vector3D scalingPoint = key.getCenterPointLocal();
		key.scale(scale * (1 / key.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)), 
				  scale * (1 / key.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)), 1,
				  scalingPoint);
		//key.scale(scale, scale, 1, scalingPoint);
	}

	private boolean setWidthRelativeToParent(float width)
	{
		if (width > 0)
		{
			Vector3D centerPoint;
			if (this.hasBounds())
			{
				centerPoint = this.getBounds().getCenterPointLocal();
				centerPoint.transform(this.getLocalMatrix());
			}
			else
			{
				centerPoint = this.getCenterPointGlobal();
				centerPoint.transform(this.getGlobalInverseMatrix());
			}
			this.scale(width * (1 / this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)), width * (1 / this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)), 1, centerPoint);
			//this.scale(width, width, 1, centerPoint);
			return true;
		}
		else
			return false;
	}

	
	
	/**
	 * The Class KeyClickAction.
	 * 
	 * @author C.Ruff
	 */
	private class KeyClickAction implements IGestureEventListener
	{
		/** The key press indent. */
		private float keyPressIndent;

		/**
		 * Instantiates a new key click action.
		 */
		public KeyClickAction()
		{
			keyPressIndent = 0.2f;//3;
		}

		public boolean processGestureEvent(MTGestureEvent g)
		{
			if (g instanceof TapEvent)
			{
				TapEvent clickEvent = (TapEvent) g;
				IMTComponent3D clicked = clickEvent.getTarget();
				if (clicked != null && clicked instanceof MTKey)
				{
					MTKey clickedKey = (MTKey) clicked;
					switch (clickEvent.getTapID())
					{
					case TapEvent.TAP_DOWN:			
						//onKeyboardButtonDown(clickedKey, shiftPressed);
						waitingCursor.add(new StoredClick(clickEvent.getCursor(),clickedKey));
						pressKey(clickedKey);
						lock++;
						break;
					case TapEvent.TAP_UP:					
						onKeyboardButtonUp(clickedKey, shiftPressed);
						unpressKey(clickedKey);
						lock--;
						break;
					case TapEvent.TAPPED:					
						onKeyboardButtonClicked(clickedKey, shiftPressed);
						unpressKey(clickedKey);
						lock--;
						if(lock < 0 )
							lock = 0;
						break;
					default:
						break;
					}// switch
					if(lock == 0)
						updateKey();
					
				}// instance of key
			}// instanceof clickevent
			return false;
		}// method

		private void pressKey(MTKey clickedKey)
		{
			clickedKey.setPressed(true);
			float keyHeight = clickedKey.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			float keyWidth = clickedKey.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

			setSizeXYRelativeToParent(clickedKey, keyWidth - keyPressIndent, keyHeight - keyPressIndent);

			if (clickedKey.getCharacterToWrite().equals("shift"))
			{
				shiftPressed = true;
				// Make certain keys visible / not visible when shift pressed!
				for (MTKey key : shiftChangers)
				{
					key.setVisible(!key.isVisible());
				}
			}
			else if(shiftPressed) 
			{
				shiftPressed = false;
				// Make certain keys visible / not visible when shift pressed!
				for (MTKey key : shiftChangers)
				{
					key.setVisible(!key.isVisible());
				}
			}
		}

		private void unpressKey(MTKey clickedKey)
		{
			clickedKey.setPressed(false);
			float kHeight = clickedKey.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			float kWidth = clickedKey.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			setSizeXYRelativeToParent(clickedKey, kWidth + keyPressIndent, kHeight + keyPressIndent);

			// System.out.println("Button CLICKED: " + clickedKey.getCharacterToWrite());
			/*if (clickedKey.getCharacterToWrite().equals("shift"))
			{
				shiftPressed = false;
				// Set shift visible keys visible/not visible
				for (MTKey key : shiftChangers)
				{
					key.setVisible(!key.isVisible());
				}
			}*/
		}

		/**
		 * Sets the size xy relative to parent.
		 * 
		 * @param shape the shape
		 * @param width the width
		 * @param height the height
		 * 
		 * @return true, if successful
		 */
		private boolean setSizeXYRelativeToParent(AbstractShape shape, float width, float height)
		{
			if (width > 0 && height > 0)
			{
				Vector3D centerPoint;
				if (shape.hasBounds())
				{
					centerPoint = shape.getBounds().getCenterPointLocal();
					centerPoint.transform(shape.getLocalMatrix()); // TODO neccessary?
				}
				else
				{
					centerPoint = shape.getCenterPointGlobal();
					centerPoint.transform(shape.getGlobalInverseMatrix());
				}
				shape.scale(width * (1 / shape.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)), height * (1 / shape.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)), 1, centerPoint);
				return true;
			}
			else
				return false;
		}
	}// class

	/**
	 * Called after keyboard button pressed.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonDown(MTKey clickedKey, boolean shiftPressed)
	{
		//System.out.println("Key keyboard");
		/*Date currentPressed = new Date();
		if( lastPressed != -1 && (currentPressed.getTime() - lastPressed ) < 300 )
			return;
		
		lastPressed = currentPressed.getTime();*/

		
		ITextInputListener[] listeners = this.getTextInputListeners();
		for (ITextInputListener textInputListener : listeners)
		{
			if (clickedKey.getCharacterToWrite().equals("back"))
			{
				textInputListener.removeLastCharacter();
			}
			else if (clickedKey.getCharacterToWrite().equals("shift"))
			{
				// no nothing
			}
			else
			{
				String charToAdd = shiftPressed ? clickedKey.getCharacterToWriteShifted() : clickedKey.getCharacterToWrite();
				textInputListener.appendCharByUnicode(charToAdd);
			}
		}
	}

	/**
	 * Keyboard button up.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonUp(MTKey clickedKey, boolean shiftPressed)
	{

	}

	/**
	 * Keyboard button clicked.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonClicked(MTKey clickedKey, boolean shiftPressed)
	{

	}

	public synchronized void addTextInputListener(ITextInputListener textListener)
	{
		if (!this.textInputAcceptors.contains(textListener))
		{
			this.textInputAcceptors.add(textListener);
		}
	}

	public synchronized ITextInputListener[] getTextInputListeners()
	{
		return this.textInputAcceptors.toArray(new ITextInputListener[this.textInputAcceptors.size()]);
	}

	public synchronized void removeTextInputListener(ITextInputListener textListener)
	{
		if (this.textInputAcceptors.contains(textListener))
		{
			this.textInputAcceptors.remove(textListener);
		}
	}

	public void close()
	{
		this.closeKeyboard();
	}

	protected void closeKeyboard()
	{
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation keybCloseAnim = new Animation("Keyboard Fade", new MultiPurposeInterpolator(width, 1, 300, 0.2f, 0.5f, 1), this);
		keybCloseAnim.addAnimationListener(new IAnimationListener()
		{
			public void processAnimationEvent(AnimationEvent ae)
			{
				switch (ae.getId())
				{
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					setWidthRelativeToParent(currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;
				default:
					break;
				}// switch
			}// processanimation
		});
		
		//Detach the orb
		if (m_controlOrb != null){
			m_controlOrb.setKeyboard(null);
		}
		
		keybCloseAnim.start();
	}

	protected void onCloseButtonClicked()
	{
		System.out.print("keyboard closed");
		CloseKBEvent evt = new CloseKBEvent(DefaultKeyboard.this);
		for (CloseKBListener listener : cllisteners)
		  listener.close(evt);
		this.close();
	}

	public void setHardwareInputEnabled(boolean hardwareInput)
	{
		try
		{
			IMTApplication app = (IMTApplication) getRenderer();
			// if (hardwareInput) {
			// app.registerKeyEvent(this);
			// }else{
			// app.unregisterKeyEvent(this);
			// }
			if (hardwareInput)
			{
				app.addKeyListener(this);
			}
			else
			{
				app.removeKeyListener(this);
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		this.hardwareInput = hardwareInput;
	}

	public boolean isHardwareInputEnabled()
	{
		return this.hardwareInput;
	}

	@Override
	public void keyPressed(char key, int keyCode)
	{
		// TODO
		// //System.out.println("Key input: " + keyCode);
		// ITextInputListener[] listeners = this.getTextInputListeners();
		// for (ITextInputListener textInputListener : listeners) {
		// if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
		// textInputListener.removeLastCharacter();
		// } else if (e.getKeyCode() == KeyEvent.VK_SHIFT
		// || e.getKeyCode() == KeyEvent.VK_ALT
		// || e.getKeyCode() == KeyEvent.VK_ALT_GRAPH
		// || e.getKeyCode() == KeyEvent.VK_CONTROL
		// ) {
		// //do nothing
		// } else {
		// textInputListener.appendCharByUnicode(keyCode);
		// }
		// }
	}

	@Override
	public void keyRleased(char key, int keyCode)
	{
	}

	public void keyEvent(KeyEvent e)
	{
		if (this.isEnabled())
		{
			if (e.getID() != KeyEvent.KEY_PRESSED)
				return;

			String keyCharString = String.valueOf(e.getKeyChar());
			// System.out.println("Key input: " + keyCode);
			ITextInputListener[] listeners = this.getTextInputListeners();
			for (ITextInputListener textInputListener : listeners)
			{
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					textInputListener.removeLastCharacter();
				}
				else if (e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_ALT || e.getKeyCode() == KeyEvent.VK_ALT_GRAPH || e.getKeyCode() == KeyEvent.VK_CONTROL)
				{
					// do nothing
				}
				else
				{
					textInputListener.appendCharByUnicode(keyCharString);
				}
			}
		}
	}

	@Override
	protected void destroyComponent()
	{
		super.destroyComponent();
		keyFont = null;
		keyList.clear();
		shiftChangers.clear();
		textInputAcceptors.clear();

		if (this.isHardwareInputEnabled())
		{
			try
			{
				((IMTApplication) getRenderer()).removeKeyListener(this);
				getRenderer().unregisterKeyEvent(this);
			}
			catch (Exception e)
			{
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Snap to keyboard.
	 * 
	 * @param mtKeyboard the mt keyboard
	 */
	public void snapToKeyboard(MTTextArea textArea)
	{
		// OLD WAY
		// this.translate(new Vector3D(30, -(getFont().getFontAbsoluteHeight() * (getLineCount())) + getFont().getFontMaxDescent() - borderHeight, 0));
		this.addChild(textArea);
		textArea.setPositionRelativeToParent(new Vector3D(40, -textArea.getHeightXY(TransformSpace.LOCAL) * 0.5f));
	}

	public boolean isCloseVisible ()
	{
		return this.keybCloseSvg.isVisible();
	}

	public void setCloseVisible (boolean visible)
	{
		this.keybCloseSvg.setVisible(visible);
	}
	
	public void addCloseKBListener (CloseKBListener listener) {
		cllisteners.add(listener);
	}
	
	public class CloseKBEvent	{
		/**
		 * @uml.property  name="listMenu"
		 * @uml.associationEnd  
		 */
		private DefaultKeyboard dKB;
		
		public CloseKBEvent(DefaultKeyboard dKB) {
			this.dKB = dKB;
		}

		public DefaultKeyboard getDefaultKB () {
			return dKB;
		}
	}
	public interface CloseKBListener {
		public void close(CloseKBEvent evt);
	}	
	
	/*@Override
	public void updateComponent(long delta)
	{
		super.updateComponent(delta);
		Date currentPressed = new Date();
		if( lastPressed != -1 && (currentPressed.getTime() - lastPressed ) < 100 )
			return;
		
		if(  waitingCursor.size() > 0 )
		{
			MTKey key = null; 
			float maxy = -9999;
			for( StoredClick cur : waitingCursor )
			{
				float y = globalToLocal(cur.pos).getY();
				if( y > maxy)
				{
					maxy = y;
					key = cur.key;
				}
			}
			
			onKeyboardButtonDown(key, shiftPressed);
			waitingCursor.clear();
			
		}
		
		lastPressed = currentPressed.getTime();
	}*/
	
	private void updateKey()
	{
		if(  waitingCursor.size() > 0 )
		{
			MTKey key = null; 
			float maxy = 9999;
			for( StoredClick cur : waitingCursor )
			{
				float y = globalToLocal(cur.pos).getY();
				if( y < maxy)
				{
					maxy = y;
					key = cur.key;
				}
			}
			
			onKeyboardButtonDown(key, shiftPressed);
			waitingCursor.clear();
			
		}
	}
	
	//Getters & Setters
	public ControlOrb getControlOrb() {return m_controlOrb;}
	public long getLastPressed() {return lastPressed;}
	
	public void setControlOrb(ControlOrb orb) {this.m_controlOrb = orb;}
	
	//Members
	private ControlOrb m_controlOrb;

}
