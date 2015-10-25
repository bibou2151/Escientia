package business;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import res.DBConnection;
import res.Loader;

/**
 * <p>This class is the EJB of the users.</p>
 * <p>All operations on the users are done here.</p>
 * @author Ihab Bouaffar
 *
 */
@Stateless(name="users")
public class UtilisateurEJB implements UBeanInterface{
	
	/**Le chemin des resources dans l'execution (le serveur)*/
	private String resources;
	
	/**La conncection SQL*/
	private java.sql.Connection con = DBConnection.getConection();
	
	@PersistenceContext(unitName="Esc_PU")
	EntityManager em;
	
	/**
	 * <p>cree un nouveau EJB utilisateurs.</p>
	 * @param resourcesPath le chemin des resuorces dans le serveur.<br>
	 * Vous pouvez obtenir ce chemin depuit la servet on appelant la methode:<br> <code>getServletContext().getRealPath("/resources/")</code>
	 */
	public UtilisateurEJB(String resourcesPath) {
		this.resources = resourcesPath;
	}
	
	/**
	 * <p>Cette methode ajoute un nouveau utilisateur au table 'utilisateur' dans la base de donnees.</p>
	 * @param u l'utilisateur q'on va ajouter
	 * @see DBConnection
	 * @throws Exception en cas de: 
	 * <ul>
	 * <li>Nom d'utilisateur deja existant</li>
	 * <li>L'email est invalide ou deja associe a un autre compte</li>
	 * <li>Mot de passe tres court</li>
	 * <li>Mot de passe ne contient pas a la fois des lettres et des chiffres.</li>
	 * </ul>
	 * 
	 */
	public void addUser(Utilisateur u) throws Exception {
		//On valide les information de cet utilisateur
		validateSignup(u);
		u.setPassword(hash(u.getPassword()));
		
		em.persist(u);
		Loader.createNewUserFile(new File(resources+"/notes/"+u.getUsername()+".notes.xml"), u);
		
		/*
		try{
		
			PreparedStatement pr = con.prepareStatement("INSERT INTO utilisateur VALUES(?, ?, ?, ?, ?, ?)");
			pr.setString(1, u.getUsername());
			pr.setString(2, u.getPassword());
			pr.setString(3, u.getNom());
			pr.setString(4, u.getPrenom());
			pr.setString(5, u.getEmail());
			pr.setString(6, u.getAdresse());
			
			pr.execute();
			
			
			//on cree un fichier des notes pour ce utilisateur
			Loader.createNewUserFile(new File(resources+"/notes/"+u.getUsername()+".notes.xml"), u);
			
			System.out.println("User successfully created: "+ u.toString());
			
			u.setPassword(hash(u.getPassword()));
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection exception = "+e);
		}*/
	}
	
	/**
	 * <p>Cette methode supprime un utilisateur (s'il existe) de la base de donnees.</p>
	 * @param username le nom d'utilisateur q'on va supprimer.
	 */
	public void removeUser(String username) {
		PreparedStatement pr;
		try {
			pr = con.prepareStatement("DELETE FROM utilisateur WHERE username = ?");
			pr.setString(1, username);
			
			pr.execute();
			
			//on supprime le fichier des notes
			NotesEJB nej = new NotesEJB(resources);
			nej.deleteNotes(username);
			
			
			System.out.println("User successfully removed: "+username);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection exception = "+e);
		}
	}
	
	/**
	 * <p>Cette methode modifie les donnees personnel de l'utilisateur.</p>
	 * @param u le nouveau utilisateur
	 * @param passwordChange si avec un nouveau mot de passe ou non
	 * @return un objet utilisateur avec les nouvelles donnees
	 * @throws Exception
	 */
	public Utilisateur updateUser(Utilisateur u, boolean passwordChange) throws Exception{
		
		verifyUpdate(getUtilisateur(u.getUsername()), u, passwordChange);
		
		try {
			String statement = null;
			if(passwordChange)
				statement = "UPDATE utilisateur SET "
					+ "password = ?, nom = ?, prenom = ?, email = ?, adresse = ?"
					+ "WHERE username = ?";
			else 
				statement = "UPDATE utilisateur SET "
					+ "nom = ?, prenom = ?, email = ?, adresse = ?"
					+ "WHERE username = ?";
			
			
			PreparedStatement pr = con.prepareStatement(statement);
			
			if(passwordChange) {
				pr.setString(1, hash(u.getPassword()));
				pr.setString(2, u.getNom());
				pr.setString(3, u.getPrenom());
				pr.setString(4, u.getEmail());
				pr.setString(5, u.getAdresse());
				pr.setString(6, u.getUsername());
			} else {
				pr.setString(1, u.getNom());
				pr.setString(2, u.getPrenom());
				pr.setString(3, u.getEmail());
				pr.setString(4, u.getAdresse());
				pr.setString(5, u.getUsername());
			}
			pr.execute();
			
			System.out.println("User data successfully updated: "+ u.getUsername());
			
			return getUtilisateur(u.getUsername());
			
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		return null;
	}
	
	/**
	 * <p>Cette fonction retourn tout les utilisateurs de la base de donnees dans un {@link ArrayList}.</p>
	 * @return un {@link ArrayList} contenant tous les utilisateurs
	 */
	public ArrayList<Utilisateur> getUsers() {
		ArrayList<Utilisateur> users = new ArrayList<Utilisateur>();
		try {
			PreparedStatement pr = con.prepareStatement("SELECT * FROM utilisateur");

			ResultSet rs=pr.executeQuery();
			
			while(rs.next()){
				Utilisateur u=new Utilisateur();
				
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setNom(rs.getString("nom"));
				u.setPrenom(rs.getString("prenom"));
				u.setEmail(rs.getString("email"));
				u.setAdresse(rs.getString("adresse"));

				users.add(u);
			}
			
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		return users;
	}
	/**
	 * <p>Cette fonction retourne un objet utilisateur specifique depuis la base de donnees qui a le nom d'utilisateur specifie.</p>
	 * @param username le nom d'utilisateur
	 * @return l'utilisateur avec le nom d'utilisateur
	 */
	public Utilisateur getUtilisateur(String username) {
		Utilisateur u = null;
		try {
			PreparedStatement pr = con.prepareStatement("SELECT * FROM utilisateur WHERE username = ?");
			pr.setString(1, username);
			ResultSet rs=pr.executeQuery();
			
			if(rs.next()){
				u=new Utilisateur();
				
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setNom(rs.getString("nom"));
				u.setPrenom(rs.getString("prenom"));
				u.setEmail(rs.getString("email"));
				u.setAdresse(rs.getString("adresse"));
				
			}
			
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		
		return u;
	}
	/**
	 * <p>Cette fonction retourne un objet utilisateur specifique depuis la base de donnees qui a l'email specifie.</p>
	 * @param email l'email de l'utilisateur
	 * @return l'utilisateur avec ce email
	 */
	public Utilisateur getUtilisateurByEmail(String email) {
		Utilisateur u = null;
		try {
			PreparedStatement pr = con.prepareStatement("SELECT * FROM utilisateur WHERE email = ?");
			pr.setString(1, email);
			ResultSet rs=pr.executeQuery();
			
			if(rs.next()){
				u=new Utilisateur();
				
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setNom(rs.getString("nom"));
				u.setPrenom(rs.getString("prenom"));
				u.setEmail(rs.getString("email"));
				u.setAdresse(rs.getString("adresse"));
				
			}
			
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		
		return u;
	}
	/**
	 * <p>Cette fonction est utilisee pour autentifier un utilisateur en donnant le nom d'utilisateur et le mot de passe.</p>
	 * <p>Elle teste le nom d'utilisateur et le mot de passe dans la base de donnees et retourne l'objet utilisateur si les credetiels 
	 * sont correct, sinon elle leve une exception.</p>
	 * @param username le nom d'utlisateur 
	 * @param password le mot de passe
	 * @return l'utilisateur avec ce nom d'utilisateur et mot de passe
	 * @throws Exception si les credentiels sont faux 
	 */
	public Utilisateur authentifyUser(String username,String password) throws Exception{
		Utilisateur u = null;
		try {
			PreparedStatement pr = con.prepareStatement("SELECT * FROM utilisateur WHERE username=? AND password=?");
			pr.setString(1, username);
			pr.setString(2, hash(password));
			ResultSet rs=pr.executeQuery();
			if(rs.next()){
				u=new Utilisateur();
				
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setNom(rs.getString("nom"));
				u.setPrenom(rs.getString("prenom"));
				u.setEmail(rs.getString("email"));
				u.setAdresse(rs.getString("adresse"));
				
			}else{
				System.out.println("Failed login: "+username);
				Exception e = new Exception("Nom d'utilisateur ou mot de passe incorrect.");
				throw e;
			}
		} catch (SQLException e) {		
			System.err.println("Database connection exception = "+e);
		}
		return u;
	}
	
	/**
	 * <p>Cette methode retourne le hash d'un string selon la politique MD5</p>
	 * @param md5
	 * @return
	 */
	private static String hash(String md5) {
	   try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
	
	
	/**
	 * <p>Cette methode verifie les informations de l'utilisateur pour les erreur, et leve une exception s'il y a 
	 * un prbleme</p> 
	 * @param u l'utilisateur pour verifier
	 * @throws Exception quand ces erreurs occurent:
	 * <li>Nom d'utilisateur deja existant</li>
	 * <li>L'email est deja associe a un autre compte</li>
	 * <li>L'email est invalide</p>
	 * <li>Mot de passe tres court</li>
	 * <li>Mot de passe ne contient pas a la fois des lettres et des chiffres.</li>
	 * </ul>
	 */
	private boolean validateSignup(Utilisateur u) throws Exception{
		Exception e;
		
		//username 
		if(getUtilisateur(u.getUsername()) != null) {
			e = new Exception("Nom d'utilisateur deja existant!");
			throw e;
		}/**/
		//	pour qu'aucun utilisateur ne peut prendre le username "admin"/"admininsrateur"
		if(u.getUsername().toLowerCase().equals("admin") ||
				u.getUsername().toLowerCase().equals("administrateur")) {
			e = new Exception("Nom d'utilisateur deja existant!");
			throw e;
		}
		/**/
		//email
		if(getUtilisateurByEmail(u.getEmail()) != null) {
			e = new Exception("Ce email est deja utilise!");
			throw e;
		}
		String email = u.getEmail();
		if(email.indexOf('@')<=1||email.lastIndexOf('.')<0){ 
			e=new Exception("Email invalide");
			throw e;
		}
		if(email.indexOf('@')>email.lastIndexOf('.')){ 
			e=new Exception("Email invalide");
			throw e;
		}
		//password
		String p=u.getPassword();
		if(p.length()<6){
			e = new Exception("Mot de passe tres court; au moins 6 caracteres");
			throw e;
		}
		if( !(p.matches(".*\\d+.*")) || !(p.matches(".*[a-zA-Z]+.*"))){
			e = new Exception("Mot de passe doit contient des lettres et des chiffre.");
			throw e;
		}
		return true;
	}
	
	/**
	 * <p>Cette methode verifie les informations de l'utilisateur pour les erreur, et leve une exception s'il y a 
	 * un prbleme</p>
	 * @param oldUder l'ancien donnees de l'utilisateur 
	 * @param newUser les nouvelles donnees de l'utilisateur
	 * @param passwordChange 
	 * @throws Exception quand ces erreurs occurent:
	 * <li>Le nouveau email est deja associe a un autre compte</li>
	 * <li>L'email est invalide</p>
	 * <li>Mot de passe tres court</li>
	 * <li>Mot de passe ne contient pas a la fois des lettres et des chiffres.</li>
	 * </ul>
	 */
	private void verifyUpdate(Utilisateur oldUder, Utilisateur newUser, boolean passwordChange) throws Exception {
		Exception e;
		
		//email
		if (!oldUder.getEmail().equals(newUser.getEmail())) {
			if(getUtilisateurByEmail(newUser.getEmail()) != null) { 
				e = new Exception("Ce email est deja utilise!");
				throw e;
			}
		}
		String email = newUser.getEmail();
		if(email.indexOf('@')<=1||email.lastIndexOf('.')<0){ 
			e=new Exception("Email invalide");
			throw e;
		}
		if(email.indexOf('@')>email.lastIndexOf('.')){ 
			e=new Exception("Email invalide");
			throw e;
		}
		if(passwordChange) {
			//password
			String p = newUser.getPassword();
			if(p.length()<6){
				e = new Exception("Mot de passe tres court; au moins 6 caracteres");
				throw e;
			}
			if( !(p.matches(".*\\d+.*")) || !(p.matches(".*[a-zA-Z]+.*"))){
				e = new Exception("Mot de passe doit contient des lettres et des chiffre.");
				throw e;
			}
		}
	}
}


