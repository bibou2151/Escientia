package business;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>This class represents a user.</p>
 * <p>It contains the necessary informations about the user.</p>
 * @author Ihab Bouaffar
 *
 */
@Entity
@Table(name="utilisateur")
public class Utilisateur implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8261944717806779946L;

	/**le nom d'utilisateur*/
	@Column
	private String username;
	
	/**le mot de passe de l'utilisateur*/
	@Column
	private String password;
	
	/**le nom personnel de l'utilisateur*/
	@Column
	private String nom;
	
	/**le prenom personnel de l'utilisateur*/
	@Column
	private String prenom;
	
	/**l'email de l'utilisateur*/
	@Column
	private String email;
	
	/**l'adress de l'utilisateur*/
	@Column
	private String adresse;
	
	/**
	 * <p>Creates a new user without any informations</p>
	 */
	public Utilisateur() {
	}
	
	/**
	 * <p>Cree un utilisateur a partir des arguments</p>
	 * 
	 * @param username nom d'utilisateur
	 * @param password mot de passe
	 * @param nom nom
	 * @param prenom prenom
	 * @param email email
	 * @param adresse adresse 
	 */
	public Utilisateur(String username, String password, String nom,
			String prenom, String email, String adresse) {
		this.username = username;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.adresse = adresse;
	}
	
	/**
	 * <p>Cette fonction retourne le nom d'utilisateur</p>
	 * @return le nom de l'utilisateur
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * <p>Cette fonction modifie le nom d'utilisateur</p>
	 * @param username le nom de l'utilisateur
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * <p>Cette fonction retourne le mot de passe de l'utilisateur</p>
	 * @return le met de passe de l'utilisateur
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * <p>Cette fonction modifie le mot de passe de l'utilisateur</p>
	 * @param password le mot de passe de l'utilisateur
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * <p>Cette fonction retourne le nom personnel de l'utilisateur</p>
	 * @return le nom personnel de l'utilisateur
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * <p>Cette fonction modifie le nom personnel de l'utilisateur</p>
	 * @param nom le nom personnel de l'utilisateur
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * <p>Cette fonction retourne le prenom de l'utilisateur</p>
	 * @return le prenom de l'utilisateur
	 */
	public String getPrenom() {
		return prenom;
	}
	
	/**
	 * <p>Cette fonction modifie le nom personnel de l'utilisateur</p>
	 * @param prenom le nom personnel de l'utilisateur
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	/**
	 * <p>Cette fonction retourne l'email de l'utilisateur</p>
	 * @return l'email de l'utilisateur
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * <p>Cette fonction modifie l'email de l'utilisateur</p>
	 * @param email l'email personnel de l'utilisateur
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * <p>Cette fonction retourne l'adresse de l'utilisateur</p>
	 * @return l'adresse de l'utilisateur
	 */
	public String getAdresse() {
		return adresse;
	}
	
	/**
	 * <p>Cette fonction modifie adresse de l'utilisateur</p>
	 * @param adresse l'adresse de l'utilisateur
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	/**
	 * <p>Returns a string representation of the user i.e. all the user informations in 
	 * a form of a string (including the password).</p>
	 * @return All the user informations in a string
	 */
	public String toString() {
		return "Utilisateur [nom d'utilisateur=" + username + ", mot de passse=" + password
				+ ", nom=" + nom + ", prenom=" + prenom + ", email=" + email
				+ ", adresse=" + adresse + "]";
	}	
}
