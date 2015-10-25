package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

/**
 * <p>Cette classe represente les notes d'un cours d'un utilisateur.</p>
 * <p>Elle contient aussi le concept but et le progres de l'utilisateur.</p>
 * @author Ihab Bouaffar
 *
 */
public class NotesCours {
	
	/**Le cours concernant ces notes*/
	private Cours cours;
	
	/**Le nom de l'utilisateur dont les résultats lui appartiennent*/
	private String username;
	
	/**Un dictionaire des notes des conceps dont le cle est le titre du concept*/
	private Map<String, Double> notes;

	/**Le type de media prefere par l'utilisateur*/
	private String prefferedMeduim;
	
	/**Un pair du concept but et du progres */
	private Pair<String, Integer> progress;
	
	/**
	 * <p>Cree un objet {@link NotesCours} qui contiennera les notes d'un utilisateur dans un cours specifie.</p>
	 * @param cours le cours dont les notes concernees
	 */
	public NotesCours(Cours cours) {
		this.setCours(cours);
		notes = new HashMap<String, Double>();
	}

	/**
	 * <p>Cette fonction retourne le titre du concept que l'utilisateur veut etudier dans un cours specifie.</p>
	 * @return le titre du concept but
	 */
	public String getTarget() {
		return progress.getLeft();
	}

	/**
	 * <p>Cette fonction retourne si l'utiilisateur est deja entrain d'etuddier ce concept i.e. est ce que 
	 * ce concept est le concept but.</p>
	 * @param conceptTitre le titre du concept
	 * @return {@code true} si ce concept est le concept but
	 */
	public boolean isAlreadyStudying(String conceptTitre) {
		try {
			return getTarget().equals(conceptTitre);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	/**
	 * <p>Cette fonction retourne le progres dans la liste des concepts que l'utilisateur etudie en commencant de 0.</p>
	 * @return le progres dans les concepts
	 * @see Cours#getPrerequisListe(String)
	 */
	public int getProgress() {
		return progress.getRight();
	}
	
	/**
	 * <p>Cette fonction retourne le titre du concept que l'utilisateur veut etudier dans un cours specifie.</p>
	 * @return le titre du concept but
	 */
	public void setNewTarget(String conceptTitre) {
		progress = Pair.of(conceptTitre, 0);
	}

	/**
	 * <p>Cette methode retourne le progres dans la liste des concepts que l'utilisateur etudie en commencant de 0.</p>
	 * @param newProgress le progres dans les concepts
	 * @see Cours#getPrerequisListe(String)
	 */
	public void setProgress(int newProgress) {
		if(getTarget() != null)
			if (newProgress < cours.getPrerequisListe(getTarget()).length)
				progress = Pair.of(progress.getLeft(), newProgress);
			else {
				setProgress(0);
				setNewTarget("");
			}
	}

	/**
	 * <p>Cette fonction retourne le type de media prefere par l'utilisateur.</p>
	 * @return le type de media prefere
	 */
	public String getPrefferedMeduim() {
		return prefferedMeduim;
	}
	
	/**
 	 * <p>Cette methode modifie le type de media prefere par l'utilisateur.</p>
	 * @param prefferedMeduim le type de media prefere
	 */
	public void setPrefferedMeduim(String prefferedMeduim) {
		this.prefferedMeduim = prefferedMeduim;
	}

	/**
	 * <p>Cette fonction retourne le nom de l'utilisateur dont les résultats lui appartiennent.</p>
	 * @return le nom de l'utilisateur dont les notes lui appartiennent
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * <p>Cette fonction modifie le nom de l'utilisateur dont les résultats lui appartiennent.</p>
	 * @param username le nom de l'utilisateur dont les notes lui appartiennent
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * <p>Cette fonction modifie la note reçue dans un concept.</p>
	 * @param conceptTitre le titre du concept de la nouvelle note
	 * @param note la nouvelle note
	 */
	public void setNote(String conceptTitre, double note) {
		notes.put(conceptTitre, note);
	}
	
	/**
	 * <p>Cette fonction retourne la note reçue dans un concept specifie.</p>
	 * @param conceptTitre le titre du concept
	 * @return la note reçue dans ce concept
	 */
	public double getNote(String conceptTitre) {
		if (!notes.containsKey(conceptTitre)) return 0.;
		return notes.get(conceptTitre);
	}
	
	/**
	 * <p>Cette fonction retourne une liste des titres des concepts du cours.</p>
	 * @return un {@link ArrayList} contenant les titres des concepts
	 */
	public ArrayList<String> getTitres() {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.addAll(notes.keySet());
		return tmp;
	}
	
	/**
	 * <p>Cette fonction modifie le cours.</p>
	 * @param cours le cours
	 */
	public void setCours(Cours cours) {
		this.cours = cours;
	}
	
	/**
	 * <p>Cette fonction retourne le cours dont les notes concernees.</p>
	 * @return le cours dont les notes concernees 
	 */
	public Cours getCours() {
		return cours;
	}
	
	/**
	 * <p>Cette fonction retourne l'ID du cours dont les notes concernees.</p>
	 * @return l'ID du cours dont les notes concernees 
	 */
	public long getCoursID(){
		return cours.getId();
	}
	/**
	 * <p>Cette fonction retourne le pourcentage des concepts complete par l'utilisateur.</p>
	 * @return le pourcentage des concepts complete
	 */
	public double getCompletedConcepts(){
		Iterator<Double> nt = notes.values().iterator();
		double per = 0.;
		while (nt.hasNext()) {
			double n = nt.next();
			if (n >= 50.)
				per++;
		}
		per = (per / notes.size()) * 100;
		per = Math.round(per * 100)/100;
		return per;
	}
	
	/**
	 * <p>Cette fonction retourne si l'utilisateur a le droit pour étudier un concept spécifié ou non
	 *  en testant si l'utilisateur a deja passe les concepts prerequis de ce concept.</p>
	 * @param conceptTitre le titre du concept
	 * @return si l'utilisateur a le droit d'étudier le concept
	 */
	public boolean isEntitled(String conceptTitre) {
		Concept concept = cours.getConcept(conceptTitre);
		Iterator<String> prerequis = concept.getPrerequis().iterator();
		
		while (prerequis.hasNext()) {
			String prerqui = prerequis.next();
			if (getNote(prerqui) < 50) return false;
		}
		return true;
	}
	
	/**
	 * <p>Cette fonction retourne si l'utilisateur est expert dans un concept spécifié.</p>
	 * <p>Elle calcule la moyenne des resultats dans le concept et tout la liste des prerequis.</p>
	 * <p>L'etuddiant est un expert si la moyenne est plus ou eguale a 80%</p>
	 * @param conceptTitre le titre du concept
	 * @return si l'utilisateur est expert dans le concept
	 */
	public boolean isExpertIn(String conceptTitre) {
		String[] conl = cours.getPrerequisListe(conceptTitre);
		int nCon = 0;
		double avg = 0;
		
		for (int i = 0; i < conl.length; i++) {
			avg += getNote(conl[i]);
			nCon++;
		}
		avg /= nCon;
		
		return (avg >= 80.);
	}
	
}
