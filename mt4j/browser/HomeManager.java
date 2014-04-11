package utc.tatinpic.gui.widgets.browser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HomeManager {
	final String DEFAULT_URL = "http://www.google.fr";
	private String user;
	private String file;
	private String home;

	public HomeManager(String user, String file) throws Exception {
		this.user = user;
		this.file = file;

		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		File xml = new File(file);
		Document document = constructeur.parse(xml);
		Element racine = document.getDocumentElement();
		NodeList listMember = racine.getChildNodes();
		boolean userKnown = false;
		for (int s = 0; s < listMember.getLength(); s++) {
			Node node = listMember.item(s);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element member = (Element) node;
				if (member.getAttribute("nom").equals(user)) {
					userKnown = true;
					home = member.getTextContent();
				}
			}
		}
		if (!userKnown) {
			addUser();
			home = DEFAULT_URL;
		}

	}

	public String getHome() {
		return home;
	}

	private void addUser() throws Exception {
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		File xml = new File(file);
		Document document = constructeur.parse(xml);
		Element racine = document.getDocumentElement();
		Element nouveauMembre = document.createElement("member");
		nouveauMembre.setAttribute("nom", user);
		nouveauMembre.appendChild(document.createTextNode(DEFAULT_URL));
		racine.appendChild(nouveauMembre);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(file));
		transformer.transform(source, result);
	}

	public void setHome(String h) throws Exception {
		home = h;
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		File xml = new File(file);
		Document document = constructeur.parse(xml);
		Element racine = document.getDocumentElement();
		NodeList listMember = racine.getChildNodes();
		for (int s = 0; s < listMember.getLength(); s++) {
			Node node = listMember.item(s);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element member = (Element) node;
				if (member.getAttribute("nom").equals(user)) {
					member.removeChild(member.getChildNodes().item(0));
					member.appendChild(document.createTextNode(h));
					TransformerFactory transformerFactory = TransformerFactory
							.newInstance();
					Transformer transformer = transformerFactory
							.newTransformer();
					DOMSource source = new DOMSource(document);
					StreamResult result = new StreamResult(new File(file));
					transformer.transform(source, result);
				}
			}
		}
	}
}
