package utc.tatinpic.gui.widgets.browser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HistoryManager {

	static Element racine;
	private Document document;
	private String uri;

	public HistoryManager(String adressedufichier) {

		try {
			this.uri = adressedufichier;
			// création d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory
					.newInstance();
			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			// lecture du contenu d'un fichier XML avec DOM
			File xml = new File(adressedufichier);
			this.document = constructeur.parse(xml);

		} catch (ParserConfigurationException pce) {
			System.out.println("Erreur de configuration du parseur DOM");
			System.out
					.println("lors de l'appel à fabrique.newDocumentBuilder();");
		} catch (SAXException se) {
			System.out.println("Erreur lors du parsing du document");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		} catch (IOException ioe) {
			System.out.println("Erreur d'entrée/sortie");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		}

	}

	public void afficherTout() {
		afficherInfos(document, 0);
	}

	public ArrayList<String> getMemberHistory(String nom) {
		ArrayList<String> bms = new ArrayList<String>();
		racine = document.getDocumentElement();
//		System.out.println("racine :" + racine);
		NodeList listMember = racine.getChildNodes();
//		System.out.println("list" + listMember);
		for (int s = 0; s < listMember.getLength(); s++) {
			Node node = listMember.item(s);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element member = (Element) node;
				if (member.getAttribute("nom").equals(nom)) {
					NodeList bookmarks = member.getElementsByTagName("history");
					for (int x = 0; x < bookmarks.getLength(); x++) {
						bms.add(bookmarks.item(x).getChildNodes().item(0)
								.getNodeValue());
					}
				}
			}
		}
		return bms;
	}

	public void addMemberHistory(String nom, String his) {
		racine = document.getDocumentElement();
		NodeList listMember = racine.getChildNodes();
		boolean memberOK = false;
		for (int s = 0; s < listMember.getLength(); s++) {
			Node node = listMember.item(s);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element member = (Element) node;
				if (member.getAttribute("nom").equals(nom)) {
					memberOK = true;
					NodeList bookmarks = member.getElementsByTagName("history");
					//System.out.println("member " + member.getAttribute("nom"));
					Element newHistory = document.createElement("history");
					newHistory.appendChild(document.createTextNode(his));
					//member.appendChild(newHistory);
					member.insertBefore(newHistory, member.getFirstChild());
					//System.out.println(getMemberHistory(nom));
					//System.out.println("Added " + his);
					this.enregistre(uri);
				}
			}
		}
		if (!memberOK) {
			Element newMember = document.createElement("member");
			newMember.setAttribute("nom", nom);
			racine.appendChild(newMember);
			System.out.println("Creating history for " + nom);
			addMemberHistory(nom, his);
		}
	}

	public void enregistre(String fichier) {
		File f = new File(fichier);
		StreamResult result = new StreamResult(f);
		DOMSource source = new DOMSource(document);
		//System.out.println("result: " + result.equals(null));
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			// transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void afficherInfos(Node noeud, int niv) {
		short type = noeud.getNodeType();
		String nom = noeud.getNodeName();
		String valeur = noeud.getNodeValue();
		indenter(niv, type == Node.TEXT_NODE);
//		System.out.print(nom + " (" + type + ") = '");
//		if (valeur != null && !valeur.matches("^\\s+$"))
//			System.out.print(valeur);
//		System.out.println("'");

		if ((type == Node.DOCUMENT_NODE || type == Node.ELEMENT_NODE)
				&& noeud.hasChildNodes()) {
			NodeList liste = noeud.getChildNodes();
			for (int i = 0; i < liste.getLength(); i++)
				afficherInfos(liste.item(i), niv + 1);
		}
	}

	public static void indenter(int n, boolean texte) {
		String tab = "\t";
		for (int i = 0; i < n; i++) {
			System.out.print(tab);
		}
		if (texte)
			System.out.print(" - ");
		else
			System.out.print(" + ");
	}

	public void eraseHistory(String currentUser) {
		System.out.println("ERASE");
		racine = document.getDocumentElement();
		NodeList listMember = racine.getChildNodes();
		for (int s = 0; s < listMember.getLength(); s++) {
			Node node = listMember.item(s);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element member = (Element) node;
				if (member.getAttribute("nom").equals(currentUser)) {
					NodeList list = member.getElementsByTagName("history");
					for(int i=list.getLength()-1; i>=0; i--){
						member.removeChild(list.item(i));
					}
					this.enregistre(uri);
				}
			}
		}
	}
}
