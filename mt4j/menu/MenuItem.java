package utc.tatinpic.model.menu;
/*
 * A menu item is defined by a row in a menu and a label
 * Its row avoid to test its label, which is painful in internationalisation
 */

public class MenuItem {
  private int row;
  private String label;
  private String stringrow;
  
public MenuItem(int row, String label) {
	super();
	this.row = row;
	this.label = label;
	stringrow = null;
}

public MenuItem(int row, String label,String r) {
	super();
	this.row = row;
	this.label = label;
	stringrow = r;
}

public int getRow() {
	return row;
}

public String getStringRow() {
	return stringrow;
}
/*
 * Its label is displayed in a menu
 */
  public String toString() {
	  return label;
  }
}
