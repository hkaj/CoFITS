package utc.bsfile.text;

import java.util.ArrayList;
import java.util.Collection;

import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.font.IFontCharacter;

import processing.core.PApplet;

import utc.bsfile.gui.widget.menu.ListMenu;
import utc.bsfile.model.menu.DefaultMenuModel;
import utc.bsfile.model.menu.IMenuModel;

public class ListMenuKeyListener extends ListMenu implements ITextInputListener {
	int min_nb=3;
    public ListMenuKeyListener(PApplet applet, int x, int y, float width,
			int nbItem, Collection<Object> choices) {
		super(applet, x, y, width, nbItem, choices);
		setMustBeDestroy(false);
	}
    public ListMenuKeyListener(PApplet applet, int x, int y, float width, int nbItem, IMenuModel model) {
    	super(applet, x, y, width, nbItem, model);
    	setMustBeDestroy(false);
    }
    public ListMenuKeyListener(PApplet applet, int x, int y, float width, int nbItem, Object... choices) {
    	super(applet, x, y, width, nbItem,choices);
    	setMustBeDestroy(false);
    }
	Dictionary dico;
    StringBuilder characters = new StringBuilder();
	@Override
	public void clear() {
	
	}

	@Override
	public void appendText(String text) {
		System.out.println("Append");
        characters.delete(0, characters.length());
		characters.append(text);
		if (characters.length()>=min_nb) 
		   update();
	}

	@Override
	public void setText(String text) {
		System.out.println("Set");

	}

	@Override
	public void appendCharByUnicode(String unicode) {
		IFont font = FontManager.getInstance().createFont(getRenderer(), "calibri", 20, MTColor.BLACK, true);
		IFontCharacter character = font.getFontCharacterByUnicode(unicode);
		int n = unicode.charAt(0);
		//System.out.println("-" + unicode + "- " + ":" + n + " value=" + character.getUnicode());
		characters.append(unicode);
		if (characters.length()>=min_nb) 
			update();
	}

	@Override
	public void removeLastCharacter() {
		System.out.println("Remove");
		if (characters.length() > 0) {
		  characters.deleteCharAt(characters.length()-1);
		}
		if (characters.length()>=min_nb) 
			update();
	}

	public void setDico(Dictionary dico) {
		this.dico = dico;
		if (dico.getArbre().chargeArbre()<= 100000)
			min_nb = 2;
		if (dico.getArbre().chargeArbre()<= 1000)
			min_nb = 1;
		if (dico.getArbre().chargeArbre()<= 100)
			min_nb = 0;
			
	}
	private void update() {
		ArrayList<String> wds = dico.getArbre().getPrefixWords(characters.toString());
        DefaultMenuModel dmm = new DefaultMenuModel();
        for (String s : wds) {
        	dmm.add(s);
        }
        
        setModel(dmm);
	}
	
}
