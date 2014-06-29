package utc.bsfile.text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LexicoTree {
   
  private static char MOT_NULL = '*'; // Mot initial
  public static char FIN_MOT = '&'; // caract�re fin de mot
  
  private LexicoTreeNode root = null;
  private StringBuilder lesMots;
  // Constructeur : un noeud racine est ins�r�
  public LexicoTree() {
	root = new LexicoTreeNode(MOT_NULL,null,null);
   }
   
  // recherche si un mot figure dans l'arbre
  public boolean existeMot(String mot) {
	  // on ajoute le caract�re fin de mot
		return existeMotEtendu(mot.toLowerCase() + FIN_MOT);
  }
  
  // ins�re un mot dans l'arbre
  public void insereMot(String mot) {
		insereMotEtendu(mot.toLowerCase() + FIN_MOT); 
  }
  // initialise un dictionnaire � partir d'une liste de mots
   public void init(String[] mots) {
		for (String s : mots) {
		  insereMot(s);
	    }
	}
   // retourne le nombre de mots de l'arbre
   public int chargeArbre() {
	   if (estVide()) 
		   return 0;
	   return chargeArbre(root.suivants);
   }
   // affiche l'arbre en tant que structure
   public void afficheArbre() {
	   if (estVide())
		   return; // arbre vide
	   afficheArbre(0,root.suivants);
	   System.out.print('\n');
   }
   // affiche les mots de l'arbre
   public void afficheMots() {
	   if (estVide())
		   return; // arbre vide
	   afficheMots("",root.suivants);
	   System.out.print('\n');
   }
   // retourne les mots du dictionnaire
   public String getMots() {
	   if (estVide())
		   return ""; // arbre vide
	   lesMots = new StringBuilder();
	   getMots("",root.suivants,lesMots);
	   return lesMots.toString();
   }
// retourne les mots du dictionnaire
   public ArrayList<String> getWords() {
	   if (estVide())
		   return new ArrayList<String>(); // arbre vide
	   ArrayList<String> words = new ArrayList<String>();
	   getWords("",root.suivants,words);
	   return words;
   }
   // affiche les mots de l'arbre commen�ant par un pr�fixe donn�
   public void afficheMotsPrefixes(String prefixe) {
	   LexicoTreeNode nd = cherchePrefixe(prefixe);
	   if (nd != null ) {
		   // sinon le pr�fixe n'est pas trouv�
	     afficheMots(prefixe,nd);
	     System.out.print('\n');
	   }
   }
// retourne les mots de l'arbre commen�ant par un pr�fixe donn�
   public String getMotsPrefixes(String prefixe) {
	   LexicoTreeNode nd = cherchePrefixe(prefixe);
	   lesMots = new StringBuilder();
	   if (nd != null ) {
		   // sinon le pr�fixe n'est pas trouv�
		 getMots(prefixe,nd,lesMots);
	   }
	   return lesMots.toString();
   }
// retourne les mots de l'arbre commen�ant par un pr�fixe donn�
   public ArrayList<String> getPrefixWords(String prefixe) {
	   LexicoTreeNode nd = cherchePrefixe(prefixe);
	   ArrayList<String> words = new  ArrayList<String>();
	   if (nd != null ) {
		   // sinon le pr�fixe n'est pas trouv�
		 getWords(prefixe,nd,words);
	   }
	   return words;
   }
   // indique si l'arbre est vide
   public boolean estVide() {
	   return root.suivants == null;
   }
   // exporte les mots de l'arbre dans un file writer
   public void exportFichier(FileWriter fw) throws IOException {
	   if (estVide())
		   return; // arbre vide
	   exportFichier("",root.suivants,fw);
   }
   // 
   public LexicoTreeNode cherchePrefixe(String prefixe) {
	   if (estVide())
		   return null; 
	   if (prefixe == "")
		   return root.suivants;
	   return cherchePrefixeEtendu(prefixe.toLowerCase() + FIN_MOT);
   }
   
   // ----------------- M�thode priv�es ---------------------------
   // recherche si un mot compl�t� par le caract�re fin de mot est pr�sent dans l'arbre
   private boolean existeMotEtendu(String mot) {
   // l'arbre est non vide
	 int i=0;
	 LexicoTreeNode b=root;
	 // tant que la fin de mot n'est pas rencontr�e
	 while(mot.charAt(i)!= FIN_MOT) { 
		 // tant que le caract�re n'est pas trouv� on descend dans la branche suivants
		 while(b!=null && mot.charAt(i)!=b.c) {
		     b=b.suivants;
		 }
		 if(b==null)
		      return false; // mot non trouv�
		// si le caract�re courant du mot est trouv� on d�cale dans la branche suffixes 
		 if(mot.charAt(i)==b.c) {
		    i++;
		    b=b.suffixes;  
		 }
     }
	 return b.c == FIN_MOT;
   }
   
   // insere un mot �tendu
   private void insereMotEtendu(String mot) {
		int i=0;
		LexicoTreeNode b=root;
		LexicoTreeNode p = null; 
		// p utile dans le cas o� on ins�re un mot pr�fixe d'un mot existant
		while(b.suivants != null && mot.charAt(i)!= FIN_MOT) {
		   if(mot.charAt(i)!= b.c) {
			   p = b;
			   b=b.suivants;
		   }
		   // pour chaque caract�re coincidant on d�cale dans la branche suffixes
		   while(mot.charAt(i)== b.c && mot.charAt(i)!= FIN_MOT) {
		      i++;
		      p = b;
		      b=b.suffixes;
		   }
		}
        // tous les caract�res du mots ont �t� trouv� dans l'arbre
		if(mot.charAt(i)==' ') { 
		   if (b.c != ' ') { // b.c == ' ' le mot existe, on n'ins�re rien
			 // plus de lettres � ins�rer. Il faut cr�er un noeud
		     LexicoTreeNode n = new LexicoTreeNode(FIN_MOT,null,b);
		     p.suffixes=n;
		   }
		}
		// il faut ins�rer la fin du mot
		else {
		   // on ins�re les lettres restantes
		   b.suivants=new LexicoTreeNode(mot.charAt(i),null,null);
		   b=b.suivants;
		   i++;
		   while(mot.charAt(i)!= FIN_MOT) {
		      b.suffixes=new
		        LexicoTreeNode(mot.charAt(i),null,null);
		      b=b.suffixes;
		      i++;
		   }
		   b.suffixes=new LexicoTreeNode(FIN_MOT,null,null);
		} 
		
	   }
   
   public LexicoTreeNode cherchePrefixeEtendu(String prefixe) {
	     // l'arbre est non vide
		 int i=0;
		 LexicoTreeNode b=root;
		 // tant que la fin du pr�fixe n'est pas rencontr�e
		 while(prefixe.charAt(i)!= FIN_MOT) { 
			 // tant que le caract�re n'est pas trouv� on descend dans la branche suivants
			 while(b!=null && prefixe.charAt(i)!=b.c) {
			     b=b.suivants;
			 }
			 if(b==null)
			      return null; // pr�fixe non trouv�
			// si le caract�re courant du mot est trouv� on d�cale dans la branche suffixes 
			 if(prefixe.charAt(i)==b.c) {
			    i++;
			    b=b.suffixes;  
			 }
	     }
		 return b;
   }
   
   // affiche l'arbre en largeur d'abord
   private void afficheArbre(int decalage,LexicoTreeNode nd) {
	   System.out.print(nd.toString());
	   // affiche la branche suffixes r�cursivement 
	   if (nd.suffixes != null)
	       afficheArbre(decalage + 1,nd.suffixes);
	   // affiche la branche suivants r�cursivement
	   if (nd.suivants != null) {
	      System.out.print('\n');
	      for (int i = 0 ; i < decalage; i++)
		     System.out.print(".");
	      afficheArbre(decalage,nd.suivants);
	   }
   }
   // additionne les mots trouv�s dans chaque branche r�cursivement
   // incr�mente � chaque fois que le caract�re fin de mot est trouv�
   private int chargeArbre(LexicoTreeNode nd) {
	   int nbmots = 0;
	   if (nd.c == FIN_MOT)
		   nbmots++;
	   if (nd.suffixes != null)
	       nbmots += chargeArbre(nd.suffixes);
	   if (nd.suivants != null) 
		   nbmots += chargeArbre(nd.suivants);
	   return nbmots;
   }
   // retourne les mots � partir d'un noeud
   private void getMots(String prefix,LexicoTreeNode nd,StringBuilder sb) {
	   if (nd.estFinMot())
	     sb.append(prefix);
	   if (nd.suffixes != null)
	       getMots(prefix + nd.c,nd.suffixes,sb);
	   if (nd.suivants != null) {
	      sb.append('\n');
	      getMots(prefix,nd.suivants,sb);
	   }
   }
   // retourne les mots � partir d'un noeud
   private void getWords(String prefix,LexicoTreeNode nd,ArrayList<String> words) {
	   if (nd.estFinMot())
	     words.add(prefix);
	   if (nd.suffixes != null)
	       getWords(prefix + nd.c,nd.suffixes,words);
	   if (nd.suivants != null) {
	      getWords(prefix,nd.suivants,words);
	   }
   }
   // affiche un mot lorsque le caract�re fin de mot est rencontr�
   private void afficheMots(String prefix,LexicoTreeNode nd) {
	   if (nd.estFinMot())
	     System.out.print(prefix);
	   if (nd.suffixes != null)
	       afficheMots(prefix + nd.c,nd.suffixes);
	   if (nd.suivants != null) {
	      System.out.print('\n');
	      afficheMots(prefix,nd.suivants);
	   }
   }
   // exporte un mot par ligne physique du fichier
   // on passe r�cursivement les pr�fixes trouv�s en ajoutant le caract�re courant d'un noeud
   private void exportFichier(String prefix,LexicoTreeNode nd,FileWriter fw) throws IOException {
	   if (nd.estFinMot())
	      fw.write(prefix + "\n");
	   if (nd.suffixes != null)
		   exportFichier(prefix + nd.c,nd.suffixes,fw);
	   if (nd.suivants != null) {
	      exportFichier(prefix,nd.suivants,fw);
	   }
   }
}
