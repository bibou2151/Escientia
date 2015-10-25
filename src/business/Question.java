package business;

import java.util.ArrayList;

/**
 * <p>Cette classe represente une question.</p>
 * <p>Elle contient le type et l'entete de la question,
 * ansi que les reponses du test du concept et la reponse correcte.
 * </p>
 * @author Ihab Bouaffar
 *
 */
public class Question {
	
	/**le type de question*/
	private String type;
	
	/**l'entete du question*/
	private String header;
	
	/**la liste des reponses*/
	private ArrayList<String> reponses;
	
	/**la reponse correcte*/
	private String reponseCorrecte;
	
	/**
	 * <p>Cree un objet {@link Question} vide.</p>
	 * 
	 */
	public Question() {
		reponses = new ArrayList<String>();
	}
	
	/**
	 * <p>Cette fonction retourne le type de la question.</p>
	 * @return le type de la question
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * <p>Cette fonction modifie le type de la question.</p>
	 * @param type le type de la question
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * <p>Cette fonction retourne l'entete de la question.</p>
	 * @return l'entete de la question
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * <p>Cette fonction modifie l'entete de la question.</p>
	 * @param header l'entete de la question
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	
	/**
	 * <p>Cette fonction retourn une liste des reponses de la question.</p>
	 * @return un {@link ArrayList} contenant les reponses de la question
	 */
	public ArrayList<String> getReponses() {
		return reponses;
	}
	
	/**
	 * <p>Cette fonction modifie la liste des reponses de la question.</p>
	 * @param prerequis un {@link ArrayList} contenant les reponses de la question.
	 */
	public void setReponses(ArrayList<String> reponses) {
		this.reponses = reponses;
	}
	
	/**
	 * <p>Cette fonction ajoute un tableau des repoonses a la liste des reponses de cette question.</p>
	 * @param reponses un tableau des reponses
	 */
	public void addReponses(String... reponses){
		for(String reponse:reponses){
			this.reponses.add(reponse);
		}
	}
	
	/**
	 * <p>Cette fonction ajoute une reponse a la liste des reponses de cette question.</p>
	 * @param reponse une reponse
	 */
	public void addReponse(String reponse) {
		this.reponses.add(reponse);
	}
	
	/**
	 * <p>Cette fonction retourn la reponse correcte de cette question.</p>
	 * @return la reponse correcte de cette question
	 */
	public String getReponseCorrecte() {
		return reponseCorrecte;
	}
	
	/**
	 * <p>Cette fonction modifie la reponse correcte de cette question.</p>
	 * @param reponseCorrecte la reponse correcte de cette question
	 */
	public void setReponseCorrecte(String reponseCorrecte) {
		this.reponseCorrecte = reponseCorrecte;
	}
	
}
