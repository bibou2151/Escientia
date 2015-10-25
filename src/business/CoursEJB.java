package business;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import res.DBConnection;
import res.Loader;

/**
 * <p>Cette classe est l ejb des cours.</p>
 * <p>Tout operations sur les cours sont fait ici.</p> 
 * @author Ihab Bouaffar
 *
 */
public class CoursEJB {
	
	/**Le chemin des resources dans l'execution (le serveur)*/
	private String resources;
	
	/**La conncection SQL*/
	private java.sql.Connection con = DBConnection.getConection();
	
	/**
	 * <p>Cree un nouveau EJB cours.</p>
	 * @param resourcesPath le chemin des resuorces dans le serveur.<br>
	 * Vous pouvez obtenir ce chemin depuit la servet on appelant la methode:<br> 
	 * <code>getServletContext().getRealPath("/resources/")</code>
	 */
	public CoursEJB(String resourcesPath) {
		this.resources = resourcesPath;
	}

	/**
	 * <p>Cette methode cherche si'il ya un cours au meme titre qu'un cours donnee, et leve une exception s'il y a.</p> 
	 * <p>C'est pour verifier qu'il n'y a pas de duplication de titres des cours</p>
	 * @param c le cours pour verifier
	 * @throws Exception s'il ya un cours au mem titre 
	 * 
	 */
	private void checkForCoursTitle(Cours c) throws Exception {
		try{
			
			PreparedStatement pr = con.prepareStatement("SELECT * FROM cours WHERE titre = ?");
			pr.setString(1, c.getTitre());
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.next()){
				throw new Exception("Un cours au meme titre existe deja!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection exception = "+e);
		}
	}
	
	/**
	 * <p>Cette methode ajoute un nouveau cours a la base de donnees, ansi que cree le ficier XML du cours.</p>
	 * @param c le cours a ajouter
	 * @return l'ID du cours dns la bse de donnees
	 * @throws Exception si un cours au meme titre existe deja
	 */
	public long addCours(Cours c) throws Exception {
		
		checkForCoursTitle(c);
		
		long id = -1;
		try{
			
			PreparedStatement pr = con.prepareStatement("INSERT INTO cours VALUES(NULL, ?)");
			pr.setString(1, c.getTitre());
			
			pr.execute();
			
			
			
			
			
			//on obtient l'ID depuis la base de donnees
			pr = con.prepareStatement("SELECT id from cours WHERE titre = ?");
			pr.setString(1, c.getTitre());
			
			ResultSet rs = pr.executeQuery();
			
			rs.next();
			id = rs.getLong("id");
			c.setId(id);
			
			//on cree un fichier XML du cours
			Loader.saveCours(new File(resources+"/cours/"+id+".cours.xml"), c);
			//on cree le repertoire des concepts
			Files.createDirectory(Paths.get(resources+"/concepts/"+id));
			
			System.out.println("Course successfully created: "+c.getId()+"- "+ c.getTitre());
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection exception = "+e);
		}
		
		return id;
	}
	
	/**
	 * <p>Cette methode modifie le cours i.e: le ficier XML du cours.</p>
	 * @param c le cours a sauveguarder
	 */
	public void updateCours(Cours c) {
		
		long id = c.getId();
		
		//on modifie le fichier XML du cours
		Loader.saveCours(new File(resources+"/cours/"+id+".cours.xml"), c);
		
		System.out.println("Course successfully updated: "+c.getId()+"- "+ c.getTitre());
		
	}
	
	/**
	 * <p>Cette methode supprime un cours depuis la base de donnees, 
	 * ansi que le ficier XML du cours, et les documents des concepts de ce cours</p>
	 * @param id l'identificateur du cours a supprimer
	 */
	public void removeCours(long id) {
		try{
			//supprimer depuis la base de donnees
			PreparedStatement pr = con.prepareStatement("DELETE FROM cours WHERE id = ?");
			pr.setLong(1, id);
			
			pr.execute();
			
			try {
				//supprimer le fichier xml du cours
				Files.delete(Paths.get(resources+"/cours/"+id+".cours.xml"));
				
				//supprimer le repertoire des concepts
				FileUtils.deleteDirectory(new File(resources+"/concepts/"+id));
				
			} catch (java.nio.file.NoSuchFileException | java.io.FileNotFoundException e) { }
				
			System.out.println("Course successfully deleted: "+ id);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection exception = "+e);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * <p>Cette fonction retourne tout les cours dans un {@link ArrayList}.</p>
	 * @return un {@link ArrayList} contenant tous les cours
	 */
	public ArrayList<Cours> getAllCours() {
		
		ArrayList<Cours> cours = new ArrayList<Cours>();
			try {
			PreparedStatement pr = con.prepareStatement("SELECT * FROM cours");

			ResultSet rs=pr.executeQuery();
			while(rs.next()){
				long id = (rs.getLong("id"));
				Cours c = getCours(id);
				cours.add(c);
			}
			
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		
		return cours;
	}
	
	/**
	 * <p>Cette fonction retourne un objet cours specifique a l'identificateur specifie.</p>
	 * @param id l'identificateur du cours
	 * @return le cours avec ce ID
	 */
	public Cours getCours(long id) {
		Cours cours = null;
		try {
			cours = Loader.LoadCours(new File(resources+"/cours/"+id+".cours.xml"));
			
		} catch (IOException e) { }
		return cours;
	}
	
}
