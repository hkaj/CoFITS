/**
 * 
 */
package utc.bsfile.gui;

import java.util.TreeMap;

import org.mt4j.components.visibleComponents.AbstractVisibleComponent;
import org.mt4j.components.visibleComponents.StyleInfo;
import org.mt4j.util.MTColor;

/**
 * @author Aymeric PELLE
 *
 */
public class Theme {
	public static String LABEL = "LABEL";
	public static String OPAQUE_IMAGE_BUTTON = "OPAQUE_IMAGE_BUTTON";
	public static String TRANSPARENT_IMAGE_BUTTON = "TRANSPARENT_IMAGE_BUTTON";
	public static String LIST_CHOICE = "LIST_CHOICE";
	public static String RW_TEXT_AREA = "RW_TEXT_AREA";
	public static String RO_TEXT_AREA = "RO_TEXT_AREA";
	public enum StyleID
	{
		DEFAULT,
		TRANSPARENT,
		STROKE_ONLY,
		FILL_ONLY
	}
	
	private static Theme theme;
	
	public static void setTheme(Theme theme)
    {
	    Theme.theme = theme;
    }

	public static Theme getTheme()  {
		if (theme == null) {
			StyleInfo defaultStyle = new StyleInfo();
			//defaultStyle.setFillColor(22, 149, 163, 255);
			defaultStyle.setFillColor(34, 83, 120, 255); // file chooser color
			defaultStyle.setStrokeColor(MTColor.BLACK);
			defaultStyle.setDrawSmooth(false);
			theme = new Theme(defaultStyle);
			theme.addStyleFromFillColor(LABEL, new MTColor(40, 110, 110, 240)); // useless
			theme.addStyleFromFillColor(OPAQUE_IMAGE_BUTTON, new MTColor(100, 100, 100, 255)); // useless
			theme.addStyleFromFillColor(TRANSPARENT_IMAGE_BUTTON, MTColor.WHITE);
			
			//StyleInfo style = theme.addStyleFromFillColor(LIST_CHOICE, new MTColor(140, 210, 210, 240));
			StyleInfo style = theme.addStyleFromFillColor(LIST_CHOICE, new MTColor(22, 149, 163, 255)); // list element color
			theme.addStyle(RW_TEXT_AREA, style);
			theme.addStyleFromFillColor(RO_TEXT_AREA, new MTColor(70, 200, 200, 160)); // useless
		}
	    return theme;
    }
	
	private TreeMap<String, StyleInfo> styles; 
	
	public Theme (StyleInfo defaultStyleInfo)
	{
		styles = new TreeMap<String, StyleInfo>();
		
		addStyle(StyleID.DEFAULT, defaultStyleInfo);
		
		StyleInfo transparentStyle = new StyleInfo(defaultStyleInfo.getFillColor(),
				defaultStyleInfo.getStrokeColor(),
				defaultStyleInfo.isDrawSmooth(),
				true,
				true,
				defaultStyleInfo.getStrokeWeight(),
				defaultStyleInfo.getFillDrawMode(),
				defaultStyleInfo.getLineStipple());
		addStyle(StyleID.TRANSPARENT, transparentStyle);
		
		StyleInfo strokeStyle = new StyleInfo(defaultStyleInfo.getFillColor(),
				defaultStyleInfo.getStrokeColor(),
				defaultStyleInfo.isDrawSmooth(),
				false,
				true,
				defaultStyleInfo.getStrokeWeight(),
				defaultStyleInfo.getFillDrawMode(),
				defaultStyleInfo.getLineStipple());
		addStyle(StyleID.STROKE_ONLY, strokeStyle);

		StyleInfo fillStyle = new StyleInfo(defaultStyleInfo.getFillColor(),
				defaultStyleInfo.getStrokeColor(),
				defaultStyleInfo.isDrawSmooth(),
				true,
				false,
				defaultStyleInfo.getStrokeWeight(),
				defaultStyleInfo.getFillDrawMode(),
				defaultStyleInfo.getLineStipple());
		addStyle(StyleID.FILL_ONLY, fillStyle);

	}
	
	public StyleInfo getStyle (String id)
	{
		return styles.get(id);
	}

	public StyleInfo addStyle (String id, StyleInfo style)
	{
		return styles.put(id, style);
	}
	
	public StyleInfo getStyle (StyleID id)
	{
		return getStyle(id.toString());
	}

	public StyleInfo addStyle (StyleID id, StyleInfo style)
	{
		return addStyle(id.toString(), style);
	}
	
	public StyleInfo addStyleFromFillColor (String id, MTColor fillColor)
	{
		StyleInfo style = getStyleCopy(StyleID.DEFAULT);
		style.setFillColor(fillColor);
		addStyle(id.toString(), style);
		return style;
	}
	
	public StyleInfo addStyleFromStrokeColor (String id, MTColor strokeColor)
	{
		StyleInfo style = getStyleCopy(StyleID.DEFAULT);
		style.setStrokeColor(strokeColor);
		addStyle(id.toString(), style);
		return style;
	}
	
	public StyleInfo addStyle (String id, MTColor fillColor, MTColor strokeColor)
	{
		StyleInfo style = getStyleCopy(StyleID.DEFAULT);
		style.setFillColor(fillColor);
		style.setStrokeColor(strokeColor);
		addStyle(id.toString(), style);
		return style;
	}
	
	public StyleInfo removeStyle (String id)
	{
		StyleID sid = StyleID.valueOf(id);

		if (sid == null)
			return styles.remove(id);

		System.out.println("TATIN_WARNING - The style " + id + " is a primordial style. It can't be removed.");
		return null;
	}
	
	public void applyStyle (String id, AbstractVisibleComponent component)
	{
		StyleInfo style = getStyle(id);
		
		if (style == null)
		{
			style = getStyle(StyleID.DEFAULT);
			System.out.println("TATIN_WARNING - The style " + id + " doesn't exist. The default style is applied.");
		}
		
		component.setStyleInfo(style);
	}

	public void applyStyle (StyleID id, AbstractVisibleComponent component)
	{
		applyStyle(id.toString(), component);
		// TODO? Stock component into a list in order to change the theme and apply new styles automatically.
	}

	public StyleInfo getStyleCopy (String id)
	{
		StyleInfo copy = null;
		StyleInfo style = getStyle(id);
		
		if (style != null)
		{
			copy = new StyleInfo(style.getFillColor(),
					style.getStrokeColor(),
					style.isDrawSmooth(),
					style.isNoStroke(),
					style.isNoFill(),
					style.getStrokeWeight(),
					style.getFillDrawMode(),
					style.getLineStipple());
		}
		
		return copy;
	}

	public StyleInfo getStyleCopy (StyleID id)
	{
		return getStyleCopy(id.toString());
	}
}
