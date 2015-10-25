package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cette classe representes les notes d'un utilisateur.</p>
 * @author Ihab Bouaffar
 *
 */
public class Notes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1673524009177424354L;
	
	/**Le nom de l'utilisateur dont les notes lui appartiennent */
	private String username;
	
	/**Un dictionaire des notes des cours dont le cle est l ID du cours*/
	private Map<Long, NotesCours> resultat;
	
	/**
	 * <p>Cree un objet {@link Notes} qui contiennera les notes d'un utilisateur.</p>
	 * @param username le nom de l'utilisateur dont les notes lui appartiennent
	 */
	public Notes(String username) {
		this.username = username;
		resultat = new HashMap<Long, NotesCours>();
	}
	
	/**
	 * <p>Cette fonction retourne le nom de l'utilisateur dont les résultats lui appartiennent.</p>
	 * @return le nom de l'utilisateur dont les notes lui appartiennent
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * <p>Cette methode modifie le nom de l'utilisateur dont les résultats lui appartiennent.</p>
	 * @param username le nom de l'utilisateur dont les notes lui appartiennent
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * <p>Cette fonction retourne une liste des IDs des cours que l'utilisateur étudie.</p>
	 * @return un {@link ArrayList} contenant les identificateur des cours
	 */
	public ArrayList<Long> getCoursIDs() {
		ArrayList<Long> tmp = new ArrayList<Long>();
		tmp.addAll(resultat.keySet());
		return tmp;
	}
	
	/**
	 * <p>Cette fonction modifie une note d'un cours.</p>
	 * @param notesCours la note
	 */
	public void setNotes(NotesCours notesCours){
		notesCours.setUsername(username);
		resultat.put(notesCours.getCoursID(), notesCours);
	}
	
	/**
	 * <p>Cette fonction retourne une note d'un cours specifie.</p>
	 * @param coursID l'ID du cours 
	 * @return le {@link NotesCours} du cours
	 */
	public NotesCours getNotes(long coursID) {
		return resultat.get(coursID);
	}
	
	/**
	 * <p>Cette fonction retourne une liste des notes du cours de l'utilisateur.</p>
	 * @return une {@link ArrayList} contenant les notes des cours
	 */
	public ArrayList<NotesCours> getNotes() {
		return new ArrayList<NotesCours>(resultat.values());
	}
	
	/**
	 * <p>Cette fonction dit si l'utilisateur a deja etudier se cours.<p>
	 * <p>Un utilisateur "inscrive" a un cours apres faire un(1) test d'un concept de ce cours.</p>
	 * @param cours le cours 
	 * @return si l'utilisateur est inscrivee dans ce cours
	 */
	public boolean isInscrivee(Cours cours) {
		return isInscrivee(cours.getId());
	}
	
	/**
	 * <p>Cette fonction dit si l'utilisateur a deja etudier se cours.<p>
	 * <p>Un utilisateur "inscrive" a un cours apres il ouvre la page du cours.</p>
	 * @param coursID l'ID du cours
	 * @return si l'utilisateur est inscrivee dans ce cours
	 */
	public boolean isInscrivee(long coursID) {
		return resultat.containsKey(coursID);
	}
}
