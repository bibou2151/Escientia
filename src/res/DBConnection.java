package res;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>Cette classe donne la connection SQL vers la base de donnees</p>
 * @author Ihab Bouaffar
 *
 */
public class DBConnection {
	
	/** Don't let anyone instantiate this class */
	private DBConnection() {
	}
	
	/**La Connection a la base de donnees*/
	private static Connection con;
	
	/**Nom de la base de donnees*/
	private static String db = "elear";
	
	/**Nom d'utilisateur de la base de donnees*/
	private static String user = "root";
	
	/**Mot de passe de l'utilisateur*/
	private static String password = "Ihab";
	
	/**
	 * <p>Cette methode retourne la connection vers la base de donnees.</p>
	 * @return La {@link Connection} vers la base de donnees
	 */
	public static Connection getConection() {
		try {
			String dbUrl = "jdbc:mysql://localhost:3306/"+db;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbUrl, user, password);
	//		System.out.println("Connection established for SQL");
			
		} catch(SQLException | ClassNotFoundException e){
			System.err.println("Database connection exception = "+e);
		}
		return con;
	}

}
