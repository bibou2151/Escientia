package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Utilisateur;
import business.UtilisateurEJB;

@WebServlet(name="update", urlPatterns={"/updateProfile"})
public class UpdateUserServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5213237887100698173L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String prenom = req.getParameter("prenom");
		String nom = req.getParameter("nom");
		String email = req.getParameter("email");
		String adresse = req.getParameter("adresse");
		String newPassword = req.getParameter("newpassword");
		
		
		
		if(adresse == null)
			adresse = "";
		
		try {
			boolean passwordChange = !(newPassword == null || newPassword.length() == 0);
			
			Utilisateur u = null;
			UtilisateurEJB ue = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
			
			if(req.getSession(true).getAttribute("admin") == null){
				ue.authentifyUser(username, password);
				if(req.getSession(true).getAttribute("user") != ue.getUtilisateur(username))
					throw new Exception("Nice try hahaha");
				if (passwordChange)
					u = new Utilisateur(username, newPassword, nom, prenom, email, adresse);
				else
					u = new Utilisateur(username, password, nom, prenom, email, adresse);
				u = ue.updateUser(u, passwordChange);
				
				req.getSession(true).setAttribute("user", u);
				req.getRequestDispatcher("/compte.jsp").forward(req, resp);
				
			} else {
				System.out.println("Admin action: update user");
				u = new Utilisateur(username, newPassword, nom, prenom, email, adresse);
				ue.updateUser(u, passwordChange);
				req.getRequestDispatcher("/administrationUsers.jsp").forward(req, resp);
			}
			
			
		} catch (Exception e) {
			String error=e.getMessage();
			req.setAttribute("error", error);
			req.getRequestDispatcher("/editProfile.jsp").forward(req, resp);
		}
	}
}
