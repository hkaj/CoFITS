package utc.bsfile.text;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Dictionary {
    // encapsule un arbre
	private LexicoTree arbre;
	public Dictionary() {
		super();
		arbre = new LexicoTree();
	}
    // le dictionnaire est responsable de la cr�ation du FileWriter 
	public void exportFichier(String nomFichier) throws IOException {
	    FileWriter fw = new FileWriter(nomFichier);
	    arbre.exportFichier(fw);
		fw.close();
   }
	
    // un objet Scanner permet de lire tr�s facilement dans un fichier texte
	public void importFichier(String nomFichier) throws IOException {
		 Scanner scanner = new Scanner(new FileInputStream(nomFichier));
		 while (scanner.hasNextLine())
			arbre.insereMot(scanner.nextLine());
	}

/**
 * @return the arbre
 */
public LexicoTree getArbre() {
	return arbre;
}
   
}
