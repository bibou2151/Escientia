package business;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import res.Loader;

/**
 * <p>Cette classe est l ejb des notes.</p>
 * <p>Tout les operations sur les notes sont fait ici.</p> 
 * @author Ihab Bouaffar
 * 
 */
public class NotesEJB {
	
	/**Le chemin des resources dans l'execution (le serveur)*/
	private String resources;
	
	/**La conncection SQL*
	private java.sql.Connection con = DBConnection.getConection();
	/**/
	
	/**
	 * <p>Cree un nouveau EJB notes.</p>
	 * @param resourcesPath le chemin des resuorces dans le serveur.<br>
	 * Vous pouvez obtenir ce chemin depuit la servet on appelant la methode:<br> 
	 * <code>getServletContext().getRealPath("/resources/")</code>
	 */
	public NotesEJB(String resourcesPath) {
		this.resources = resourcesPath;
	}
	
	/**
	 * <p>Cette methode ajoute l'espace pour les notes d'un cours dans le ficier XML 
	 * du notes d'un utilisateur afin que les notes puissent etre mise.</p>
	 * @param u l'utilisateur concerne
	 * @param c le cours 
	 */
	public void inscrireCours(Utilisateur u, Cours c) {
		Loader.fillCoursNotes(c, new File(resources+"/notes/"+u.getUsername()+".notes.xml"));
	}
	
	/**
	 * <p>Cette fonction retourne les {@link Notes notes} d'un utilisateur.</p>
	 * @param u l'utilisateur concerne
	 * @return les {@link Notes notes} de l'utilisateur
	 */
	public Notes getNotes(Utilisateur u) {
		Notes notes = null;
		notes = Loader.LoadNotes(new File(resources+"/notes/"+u.getUsername()+".notes.xml"));
		return notes;
	}
	
	/**
	 * <p>Cette fonction retourne les {@link NotesCours notes d'un cours} d'un utilisateur.</p>
	 * <p>Si l'utilisateur n'as pas etudier ce cours, elle retourne {@code null}.</p> 
	 * @param u l'utilisateur concerne
	 * @param cours le cours
	 * @return les {@link NotesCours notes d'un cours} de l'utilisateur
	 */
	public NotesCours getNotesCours(Utilisateur u, Cours cours) {
		return getNotesCours(u, cours.getId());
	}
	
	/**
	 * <p>Cette fonction retourne les {@link NotesCours notes d'un cours} d'un utilisateur.</p>
	 * <p>Si l'utilisateur n'as pas etudier ce cours, elle retourne {@code null}.</p> 
	 * @param u l'utilisateur concerne
	 * @param coursID l'identificateur du cours
	 * @return les {@link NotesCours notes d'un cours} de l'utilisateur
	 */
	public NotesCours getNotesCours(Utilisateur u, long coursID) {
		File xmlFile = new File(resources+"/notes/"+u.getUsername()+".notes.xml");
		NotesCours nc = null;
		Notes notes = getNotes(u);
		if(!notes.isInscrivee(coursID)) {
			Loader.fillCoursNotes(new CoursEJB(resources).getCours(coursID), xmlFile);
			notes = Loader.LoadNotes(xmlFile);
		}
		nc = notes.getNotes(coursID);
		return nc;
	}
	
	/**
	 * <p>Cette fonction retourne une {@link ArrayList liste} des {@link NotesCours notes d'un cours} 
	 * de tout les utilisateurs de ce cours.</p>
	 * <p>Si un utilisateur n'a pas des notes dans ce cours il ne sera pas inclu.(duh!)</p>
	 * @param cid le ID du cours
	 * @return une {@link ArrayList liste} des {@link NotesCours notes d'un cours} 
	 */
	public ArrayList<NotesCours> getAllNotesCours(long cid) {
		ArrayList<NotesCours> all = new ArrayList<NotesCours>();
		UtilisateurEJB uej = new UtilisateurEJB(this.resources);
		Iterator<Utilisateur> uIt = uej.getUsers().iterator();
		
		while (uIt.hasNext()) {
			Utilisateur u = uIt.next();
			Notes n = getNotes(u);
			if (n.isInscrivee(cid))
				all.add(n.getNotes(cid));
		}
		return all;
	}
	
	/**
	 * 
	 * @param u
	 * @param coursID
	 * @param newProgress
	 * @deprecated use {@link NotesEJB#updateNotesCours(Utilisateur, NotesCours)} instead
	 */
	@Deprecated
	public void updateProgressIn(Utilisateur u, long coursID, int newProgress) {
		Notes notes = getNotes(u);
		if (notes.isInscrivee(coursID)) {
			NotesCours nc = notes.getNotes(coursID);
			nc.setProgress(newProgress);
			updateNotesCours(u, nc);
		}
	}
	
	public NotesCours makeNewTarget(Utilisateur u, long coursID, String conceptTitre) {
		Notes notes = getNotes(u);
		if (notes.isInscrivee(coursID)) {
			NotesCours nc = notes.getNotes(coursID);
			nc.setNewTarget(conceptTitre);
			updateNotesCours(u, nc);
		}
		return getNotesCours(u, coursID);
	}
	
	/**
	 * <p>Cette methode modifie les notes d'un cours pour un utilisateur.</p>
	 * @param u  l'utilisateur concerne
	 * @param nc les {@link NotesCours notes du cours}
	 */
	public void updateNotesCours(Utilisateur u, NotesCours nc) {
		Loader.updateNotes(new File(resources+"/notes/"+u.getUsername()+".notes.xml"), nc);
	}
	
	/**
	 * <p>Cette methode supprime le fichier XML des note d'un utilisateur.</p>
	 * @param username le pseudo de l'utilisateur
	 */
	public void deleteNotes(String username){
		File f = new File(resources+"/notes/"+username+".notes.xml");
		f.delete();
	}

}
