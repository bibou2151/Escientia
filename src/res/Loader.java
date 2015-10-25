package res;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import business.Concept;
import business.Cours;
import business.Notes;
import business.NotesCours;
import business.Question;
import business.Utilisateur;

/**
 * <p>Cette classe est le moteur responsable de la conversion objet -> fichier XML 
 * et ficier XML -> objet, plus les mise a jours des fichier XML.</p>
 * <p>Cette classe utilise les principes de DOM (Document Object Model) et les bibliotheques 
 * {@code javax.xml.*} et {@code org.w3c.*}.</p>
 * @author Ihab Bouaffar
 *
 */
public class Loader {
	
	/** Don't let anyone instantiate this class */
	private Loader() {
	}
	
	//-------------------------------------------------------------------------------------------------------------------//
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder documentBuilder;
	/**Parse le contenu d'un fichier XML et retourne un objet {@link Document}*/
	private static Document getDocument(File xmlFile) throws IOException {
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(xmlFile);
			
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**Cree un document XML vide*/
	private static Document createDocument() {
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.newDocument();
			
		} catch (ParserConfigurationException | DOMException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static Transformer transformer;
	/**Transforme un {@link Document} XML a un fichier*/
	private static void persistDocument(Document document, File xmlFile){
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------Cours-------------------------------------------------------//

	/**
	 * <p>Cette methode sauveguarde un objet {@link Cours} en un fichier XML.</p>
	 * @param xmlFile le fichhier xml
	 * @param c le cours a sauveguarder
	 */
	public static void saveCours(File xmlFile, Cours c) {
		Document coursDoc = createDocument();
		coursDoc.setXmlVersion("1.0");
		coursDoc.setXmlStandalone(true);
		
		Element root = coursDoc.createElement("cours");
		root.setAttribute("id", ""+c.getId());
		root.setAttribute("titre", c.getTitre());
		
		//description
		Element desc = coursDoc.createElement("description");
		desc.setTextContent(c.getDescription());
		
		root.appendChild(coursDoc.createTextNode("\n\t"));
		root.appendChild(desc);
		//concepts
		Iterator<Concept> concepts = c.getConcepts().iterator();
		
		while (concepts.hasNext()) {
			Concept con = concepts.next();
			//titre
			Element concept = coursDoc.createElement("concept");
			concept.setAttribute("titre", con.getTitre());
			//prerequisite
			Iterator<String> prereqs = con.getPrerequis().iterator();
			Element preqs = coursDoc.createElement("prerequisites");
			
			while (prereqs.hasNext()) {
				String pre = prereqs.next();
				Element preElemnt = coursDoc.createElement("prerequisite");
				preElemnt.setTextContent(pre);
				
				preqs.appendChild(coursDoc.createTextNode("\n\t\t\t"));
				preqs.appendChild(preElemnt);
			}
			preqs.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(preqs);
			//content
			Iterator<String> contenus = con.getAllContenus().iterator();
			Element contenElement = coursDoc.createElement("contenus");
			
			while (contenus.hasNext()) {
				String tconten = contenus.next();
				Element conElemnt = coursDoc.createElement("contenu");
				conElemnt.setAttribute("type-de-contenu", tconten);
				conElemnt.setTextContent(con.getContenu(tconten));
				
				contenElement.appendChild(coursDoc.createTextNode("\n\t\t\t"));
				contenElement.appendChild(conElemnt);
			}
			contenElement.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(contenElement);
			
			//test
			Iterator<Question> questions = con.getQuestions().iterator();
			Element test = coursDoc.createElement("test");
			while (questions.hasNext()) {
				//qusetion
				Question que = questions.next();
				Element quElement = coursDoc.createElement("question");
				quElement.setAttribute("type", que.getType());
				//header
				Element headElement = coursDoc.createElement("header");
				headElement.setTextContent(que.getHeader());
				
				quElement.appendChild(coursDoc.createTextNode("\n\t\t\t\t"));
				quElement.appendChild(headElement);
				//reponses
				Iterator<String> resps = que.getReponses().iterator();
				while (resps.hasNext()) {
					String resp = resps.next();
					Element respElement = coursDoc.createElement("reponse");
					respElement.setTextContent(resp);
					if(que.getReponseCorrecte().equals(resp))
						respElement.setAttribute("correcte", "correcte");
					
					quElement.appendChild(coursDoc.createTextNode("\n\t\t\t\t"));
					quElement.appendChild(respElement);
				}
				quElement.appendChild(coursDoc.createTextNode("\n\t\t\t"));
				test.appendChild(coursDoc.createTextNode("\n\t\t\t"));
				test.appendChild(quElement);
			}
			test.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(coursDoc.createTextNode("\n\t\t"));
			concept.appendChild(test);
			concept.appendChild(coursDoc.createTextNode("\n\t"));
			
			root.appendChild(coursDoc.createTextNode("\n\t"));
			root.appendChild(concept);
			root.appendChild(coursDoc.createTextNode("\n"));
		}

		coursDoc.appendChild(root);
		coursDoc.normalize();
		
		persistDocument(coursDoc, xmlFile);
			
	}/**/
	
	/**
	 * <p>Cette fonction retourne un objet {@link Cours} construit depuis un fichier XML.</p>
	 * @param xmlFile le ficier xml contenant le cours
	 * @return le cours depuis le ficier XML
	 * @throws IOException Si le fichier XML n'existe pas
	 */
	public static Cours LoadCours(File xmlFile) throws IOException {
		Cours cours = null;
		
		Document coursDoc = getDocument(xmlFile);
		
		cours = new Cours();
		
		cours.setId((Long.parseLong(coursDoc.getDocumentElement().getAttribute("id"))));
		cours.setTitre(coursDoc.getDocumentElement().getAttribute("titre"));
		try {
			cours.setDescription(coursDoc.getElementsByTagName("description").item(0).getTextContent());
		} catch (NullPointerException e1) { cours.setDescription(""); }
		
		NodeList conceptNodes = coursDoc.getElementsByTagName("concept");
		
		for(int i=0; i < conceptNodes.getLength(); i++){
			
			Concept concept = new Concept();
			Element conceptElement = (Element) conceptNodes.item(i);
			//Titre
			concept.setTitre(conceptElement.getAttribute("titre"));
			//Prerequisites
			NodeList prerequisNodes = conceptElement.getElementsByTagName("prerequisite");
			for (int j=0; j<prerequisNodes.getLength();j++){
				concept.addPrerequis(prerequisNodes.item(j).getTextContent());
			}
			//Contenu
			
			NodeList contentNodes = conceptElement.getElementsByTagName("contenu");
			for (int j = 0; j < contentNodes.getLength(); j++) {
				Element contenu = (Element) contentNodes.item(j);
				concept.addContenu(contenu.getAttribute("type-de-contenu"), contenu.getTextContent());
			}
			
			//Questions
			NodeList questionNodes = conceptElement.getElementsByTagName("question");
			for (int j=0; j<questionNodes.getLength();j++){
				
				Element questionElement = (Element) questionNodes.item(j);
				Question question = new Question();
				question.setType(questionElement.getAttribute("type"));
				question.setHeader(questionElement.getElementsByTagName("header").item(0).getTextContent());
				
				NodeList reponseNodes = questionElement.getElementsByTagName("reponse");
				for(int k=0;k<reponseNodes.getLength();k++){
					Element reponseElemt = (Element) reponseNodes.item(k);
					question.addReponse(reponseElemt.getTextContent());
					if(reponseElemt.hasAttribute("correcte"))
						question.setReponseCorrecte(reponseElemt.getTextContent());
				}
				concept.addQuestion(question);
			}
			//Un concept
			cours.addConcept(concept);
		}
		
		return cours;
	}
	
	//-------------------------------------------------------Notes-------------------------------------------------------//
	
	/**
	 * <p>Cette methode cree un nouveau ficier XML de notes d'un utilisateur<p>
	 * @param xmlFile le fichier xml
	 * @param user l'utilisateur dont les notes apartient
	 */
	public static void createNewUserFile(File xmlFile, Utilisateur user) {
		Document notesDoc = createDocument();
		
		notesDoc.setXmlVersion("1.0");
		notesDoc.setXmlStandalone(true);
		
		Element root = notesDoc.createElement("notes");
		root.setAttribute("username", user.getUsername());
		notesDoc.appendChild(root);
			
		persistDocument(notesDoc, xmlFile);
			
	}

	/**
	 * <p>Cette fonction retourne un objet {@link Notes} construit depuis un fichier XML.</p>
	 * @param xmlFile le ficier xml contenant les notes
	 * @return l'objet {@link Notes} depuis le ficier XML
	 */
	public static Notes LoadNotes(File xmlFile) {
		Notes notes = null;
		
		Document notesDoc = null;
		try {
			notesDoc = getDocument(xmlFile);
		} catch (IOException e1) { e1.printStackTrace();}

		notes = new Notes(notesDoc.getDocumentElement().getAttribute("username"));
		
		//notes
		NodeList coursNodes = notesDoc.getElementsByTagName("cours");
		
		for(int i=0; i < coursNodes.getLength(); i++){
			try {
				Element coursElement = (Element) coursNodes.item(i);
				//
				String coursPath = xmlFile.getParentFile().getParent()+"\\cours\\"
														+coursElement.getAttribute("id")+".cours.xml";
				
				NotesCours notesCours = new NotesCours(LoadCours(new File(coursPath)));
				//
				//progress
				Element progressElement = (Element)coursElement.getElementsByTagName("progress").item(0);
				notesCours.setNewTarget(
						progressElement.getAttribute("target").length() > 0 ? progressElement.getAttribute("target") : null);
				try {
					notesCours.setProgress(Integer.parseInt(progressElement.getTextContent()));
				} catch (NumberFormatException | NullPointerException e) {
					notesCours.setProgress(0);
				}
				//preference
				Element prefElement = (Element) coursElement.getElementsByTagName("meduim").item(0);
				notesCours.setPrefferedMeduim(prefElement.getTextContent());
				
				//concepts
				NodeList conceptNodes = coursElement.getElementsByTagName("concept");
				for (int j=0; j<conceptNodes.getLength();j++){
					String conceptTitre = ((Element) conceptNodes.item(j)).getAttribute("titre");
					double note = Double.parseDouble(conceptNodes.item(j).getTextContent());
					
					notesCours.setNote(conceptTitre, note);
				}
				notes.setNotes(notesCours);
			} catch (IOException | NullPointerException e) {
				// Si le cours n'existe plus, on le supprime des notes
				notesDoc.getDocumentElement().removeChild(coursNodes.item(i));
				persistDocument(notesDoc, xmlFile);
			}
		}
			
		return notes;
	}

	/**
	 * <p>Cette methode sauveguarde les {@link NotesCours notes de cours} dans un fichier 
	 * XML.</p>
	 * @param xmlFile le ficier xml contenant les notes
	 * @param notes l'objet {@link NotesCours} contenant les notes
	 */
	public static void updateNotes(File xmlFile, NotesCours notes) {
		
		Document notesDoc = null;
		try {
			notesDoc = getDocument(xmlFile);
		} catch (IOException e) { }
		
		NodeList coursNodes = notesDoc.getElementsByTagName("cours");
		
		for(int i=0; i < coursNodes.getLength(); i++){
			Element coursElement = (Element) coursNodes.item(i);
			if(Long.parseLong(coursElement.getAttribute("id")) == notes.getCoursID()){
				//progress
				Element progressElement = (Element) coursElement.getElementsByTagName("progress").item(0);
				progressElement.setAttribute("target", notes.getTarget());
				progressElement.setTextContent("" + notes.getProgress());
				
				//preference
				Element prefElement = (Element) coursElement.getElementsByTagName("meduim").item(0);
				prefElement.setTextContent(notes.getPrefferedMeduim());
				
				//notes des concepts
				NodeList conceptNodes = coursElement.getElementsByTagName("concept");
				for (int j=0; j < conceptNodes.getLength(); j++){
					String conceptTitre = ((Element) conceptNodes.item(j)).getAttribute("titre");
					double no = notes.getNote(conceptTitre);
					conceptNodes.item(j).setTextContent(""+no);
				}
			}
		}
		
		persistDocument(notesDoc, xmlFile);
	}

	/**
	 * <p>Cette methode cree "l'espace" (i.e. les balises) des {@link NotesCours notes de cours}
	 *  dans le fichier XML en donnant une note de 0 pour chaque concept.</p>
	 *  <p>Ansi que la balise de progres.</p>
	 * @param xmlFile xmlFile le ficier xml contenant les notes
	 * @param cours le cours
	 */
	public static void fillCoursNotes(Cours cours, File xmlFile) {
		
		Document notesDoc = null;
		try {
			notesDoc = getDocument(xmlFile);
		} catch (IOException e) { }
			
		Notes n = LoadNotes(xmlFile);
		
		if(n.getNotes(cours.getId()) == null) {
			
			Element coursElement = notesDoc.createElement("cours");
			coursElement.setAttribute("id", ""+cours.getId());
			//progress	
			Element progressElement = notesDoc.createElement("progress");
			progressElement.setAttribute("target", "");
			coursElement.appendChild(notesDoc.createTextNode("\n\t\t"));
			coursElement.appendChild(progressElement);
			coursElement.appendChild(notesDoc.createTextNode("\n\t\t"));
			
			//preferrence
			Element prefElement = notesDoc.createElement("preference");
				//meduim
				Element mediElement = notesDoc.createElement("meduim");
				prefElement.appendChild(notesDoc.createTextNode("\n\t\t\t"));
				prefElement.appendChild(mediElement);
				prefElement.appendChild(notesDoc.createTextNode("\n\t\t"));
				
			coursElement.appendChild(prefElement);
			coursElement.appendChild(notesDoc.createTextNode("\n\n\t"));
			
			//concepts
			Iterator<Concept> concepts = cours.getConcepts().iterator();
			
			while (concepts.hasNext()) {
				Concept concept = (Concept) concepts.next();
	
				Element conceptElement = notesDoc.createElement("concept");
				conceptElement.setAttribute("titre", concept.getTitre());
				conceptElement.setTextContent(""+0.);
				coursElement.appendChild(notesDoc.createTextNode("\n\t\t"));
				coursElement.appendChild(conceptElement);
					
			}
			notesDoc.getDocumentElement().appendChild(notesDoc.createTextNode("\n\t"));
			coursElement.appendChild(notesDoc.createTextNode("\n\t"));
			notesDoc.getDocumentElement().appendChild(coursElement);
			notesDoc.getDocumentElement().appendChild(notesDoc.createTextNode("\n"));
			notesDoc.normalize();
			
			persistDocument(notesDoc, xmlFile);
		}
	}
	
}
