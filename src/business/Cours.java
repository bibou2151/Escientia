package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>Cette classe represente un cours.</p>
 * <p>Elle contient l'ID, titre, et description du cours,
 * ansi que les {@link Concept concepts} composant le cours.
 * </p>
 * @author Ihab Bouaffar
 *
 */
public class Cours implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7668760721257491591L;

	
	/**L'ID dans la base de donnees*/
	private long id;
	
	/**Le titre du cours*/
	private String titre;
	
	/**Une petite description du cours*/
	private String description;
	
	/**un vecteur qui contient tout les concepts du cours*/
	private ArrayList<Concept> concepts;
	
	
	/**
	 * <p>Cree un objet cours vide.</p>
	 * */
	public Cours() {
		concepts = new ArrayList<Concept>();
	}
	
	/**
	 * <p>Cette fonction retourne un tableau contenant une liste de tout les prerequis (titres des concepts) d'un concept.</p>
	 * <p>Pas comme la metode {@link Concept#getPrerequis()}, cette methode retourne une liste etendue des prerequis,
	 * i.e. les prerequis du concept et les prerequis de son prerequis et les prerequis des prerequis... etc.</p>
	 * <p><b>Remarque:</b> la resultat est organisee tel que la naviguatiion de 0 a la fin ne rencontre aucun prerequis non passee.</p>
	 * @param conceptTitre le titre du concept
	 * @return un tableau des titre de tout les prerequis du concept
	 */
	public String[] getPrerequisListe(String conceptTitre) {
		ArrayList<String> pres = new ArrayList<String>();
		Iterator<String> prerequisIt = getConcept(conceptTitre).getPrerequis().iterator();
		 
		while (prerequisIt.hasNext()) {
			String prereq = prerequisIt.next();
			
			Concept c = getConcept(prereq);
			for(int i = 0; i < getPrerequisListe(c.getTitre()).length-1; i++) {
				pres.add(getPrerequisListe(c.getTitre())[i]);
			}
			pres.add(prereq);
		}
		
		pres.add(conceptTitre);
		
		String[] tmp = new String[pres.size()];
		pres.toArray(tmp);
		
		return tmp;
	}
	
	/**
	 * <p>Cette fonction retourne l'ID du cours dans la base de donnees.</p>
	 * @return l'ID du cours dans la base de donnees
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * <p>Cette fonction modifie l'ID du cours dans la base de donnees.</p>
	 * @param id l'ID du cours dans la base de donnees
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * <p>Cette fonction retourne le titre du cours.</p>
	 * @return le titre du cours
	 */
	public String getTitre() {	
		return titre;
	}
	
	/**
	 * <p>Cette fonction modifie le titre du cours.</p>
	 * @param titre le titre du cours
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	/**
	 * <p>Cette fonction retourne la description du cours.</p>
	 * @return la description du cours
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * <p>Cette fonction modifie la description du cours.</p>
	 * @param description la description du cours
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * <p>Cette fonction retourn tout les concepts du cours dans un {@link ArrayList}.</p>
	 * @return un {@link ArrayList} contenant tous les concepts
	 */
	public ArrayList<Concept> getConcepts() {
		return concepts;
	}
	
	
	/**
	 * <p>Cette fonction retourne un {@link Concept concept} specifique en donnant son titre.</p>
	 * @param titre le titre du concept
	 * @return le concept avec ce titre
	 */
	public Concept getConcept(String titre) {
		Concept concept = null;
		Iterator<Concept> cons = concepts.iterator();
		while (cons.hasNext()) {
			concept = (Concept) cons.next();
			if (concept.getTitre().equals(titre)) return concept;
		}
		return null;
	}
	
	/**
	 * <p>Cette fonction remplace les concepts du cours par les concepts qui sont dans un {@link ArrayList} donne.</p>
	 * @param concepts {@link ArrayList} des concepts
	 */
	public void setConcepts(ArrayList<Concept> concepts) {
		this.concepts = new ArrayList<Concept>();
		Iterator<Concept> cons = concepts.iterator();
		
		while (cons.hasNext()) {
			Concept concept = (Concept) cons.next();
			concept.setCoursParent(this.getId());
		}
		
		this.concepts = concepts;
	}
	
	/**
	 * <p>Cette fonction remplace les concepts du cours par les concepts qui sont dans un tableau donne.</p>
	 * @param concepts tableau des concepts
	 */
	public void setConcepts(Concept... concepts) {
		this.concepts = new ArrayList<Concept>();
		for (Concept concept : concepts) {
			addConcept(concept);
		}
	}
	
	/**
	 * <p>Cette fonction ajoute un {@link Concept concept} au cours.</p>
	 * @param concept le concept a ajouter
	 */
	public void addConcept(Concept concept) {
		concept.setCoursParent(this.getId());
		this.concepts.add(concept);
	}
	
}
