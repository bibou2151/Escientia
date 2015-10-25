package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cette classe represente un concept.</p>
 * <p>Elle contient le titre, la liste des prerequis,
 * le type de contenu et le contenu,
 * ansi que les {@link Question questions} du test du concept.
 * </p>
 * @author Ihab Bouaffar
 *
 */
public class Concept {
	
	/**le id du cours parent de ce concept*/
	private long coursParent;
	
	/**le titre de ce concept */
	private String titre;
	
	/**une liste des prerequis de ce concept*/
	private ArrayList<String> prerequis;
	
	/**le contenu de ce concept*/
	private Map<String, String> contenu;
	
	/**les questions du test de ce concept*/
	private ArrayList<Question> questions;
	
	/**
	 * <p>Cree un objet {@link Concept} vide.</p>
	 * */
	public Concept() {		
		prerequis = new ArrayList<String>();
		contenu = new HashMap<String, String>();
		questions = new ArrayList<Question>();
	}
	
	/**
	 * <p>Cette fonction retourne le id du cours parent de ce concept.</p>
	 * @return le id du cours parent de ce concept
	 */
	public long getCoursParentId() {
		return coursParent;
	}
	
	/**
	 * <p>Cette fonction modifie le id du cours parent de ce concept.</p>
	 * @param coursParent le id du cours parent de ce concept
	 */
	public void setCoursParent(long coursParent) {
		this.coursParent = coursParent;
	}
	
	/**
	 * <p>Cette fonction retourne le titre de ce concept.</p>
	 * <p>Le titre du concept est ce qui identifie un concept.</p>
	 * @return le titre du concept
	 */
	public String getTitre() {
		return titre;
	}
	
	/**
	 * <p>Cette modifie retourne le titre de ce concept.</p>
	 * <p>Le titre du concept est ce qui identifie un concept.</p>
	 * @param titre le titre du concept
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * <p>Cette fonction retourn une liste des titres des concepts prerequis de ce concept.</p>
	 * @return un {@link ArrayList} contenant les titres des concepts prerequis
	 */
	public ArrayList<String> getPrerequis() {
		return prerequis;
	}
	
	/**
	 * <p>Cette fonction modifie la liste des titres des concepts prerequis de ce concept.</p>
	 * @param prerequis un {@link ArrayList} contenant les titres des concepts prerequis
	 */
	public void setPrerequis(ArrayList<String> prerequis) {
		this.prerequis = prerequis;
	}
	
	/**
	 * <p>Cette fonction ajoute un tableau des prerequis a la liste de ce concept.</p>
	 * @param prerequis un tableau des prerequis 
	 */
	public void addPrerequis(String... prerequis) {
		for (String prerequi : prerequis) {
			this.prerequis.add(prerequi);
		}
	}
	
	/**
	 * <p>Cette fonction ajoute un prerequis a la liste de ce concept.</p>
	 * @param prerequis le titre du concept prerequis 
	 */
	public void addPrerequis(String prerequis) {
		this.prerequis.add(prerequis);
	}
	
	/**
	 * <p>Cette fonction retourn le contenu du concept.</p>
	 * @param typeDeContenu type de contenu
	 * @return le contenu du concept
	 * @throws NullPointerException si le type de contenu n'existe pas
	 */
	public String getContenu(String typeDeContenu) throws NullPointerException {
		if (!contenu.containsKey(typeDeContenu)) 
			throw new NullPointerException("Ce type de contenu n'est pas disponible");
		return contenu.get(typeDeContenu);
	}
	
	/**
	 * <p>Cette methode ajoute un contenu au concept.</p>
	 * @param contenu le contenu du concept
	 * @param typeDeContenu le type de contenu
	 */
	public void addContenu(String typeDeContenu, String contenu) {
		this.contenu.put(typeDeContenu, contenu);
	}
	
	/**
	 * <p>Cette fonction retourn tout les types de contenu disponible pour ce concept.</p>
	 * @return un {@link ArrayList} contenant tout les types de contenu
	 */
	public ArrayList<String> getAllContenus() {
		return new ArrayList<String>(this.contenu.keySet());
	}
	
	/**
	 * <p>Cette fonction retourne est ce que ce concept a un contenu de type donnee.</p>
	 * @param typeDeContenu le type de contenu
	 * @return {@code true} si le concept a un contenu de ce type
	 */
	public boolean hasContentOfType(String typeDeContenu) {
		return this.contenu.containsKey(typeDeContenu);
	}

	/**
	 * <p>Cette fonction retourne la liste des {@link Question questions} du test du concept.</p>
	 * @return un {@link ArrayList} contenant questions du test
	 */
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	/**
	 * <p>Cette fonction modifie la liste des {@link Question questions} du test du concept.</p>
	 * @param questions un {@link ArrayList} contenant questions du test
	 */
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	
	/**
	 * <p>Cette fonction ajoute un tableau des questions a la liste des questions de ce concept.</p>
	 * @param questions un tableau des questions
	 */
	public void addQuestions(Question... questions) {
		for (Question question: questions) {
			this.questions.add(question);
		}
	}
	
	/**
	 * <p>Cette fonction ajoute une question a la liste des questions de ce concept.</p>
	 * @param question une {@link Question question}
	 */
	public void addQuestion(Question question) {
		this.questions.add(question);
	}
	
}
