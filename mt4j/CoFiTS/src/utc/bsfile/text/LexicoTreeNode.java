package utc.bsfile.text;

public class LexicoTreeNode {
	char c;
	LexicoTreeNode suffixes;
	LexicoTreeNode suivants;


	public LexicoTreeNode (char c, LexicoTreeNode suffixes,
	    LexicoTreeNode suivants) {
	   this.c = c;
	   this.suffixes = suffixes;
	   this.suivants = suivants;
	}
	
   public String toString() {
	   return estFinMot() ? "*" : c + "";
	}
   
   public boolean estFinMot() {
	   return c == LexicoTree.FIN_MOT;
   }
}
