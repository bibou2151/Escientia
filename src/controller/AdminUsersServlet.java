package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Utilisateur;
import business.UtilisateurEJB;

@WebServlet(name="AdmU", urlPatterns={"/AdmUser"})
public class AdminUsersServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -351799122152816343L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("index.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getSession().getAttribute("admin") == null) {
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
		
		String act =req.getParameter("action");
		System.out.println("Admin action = "+act+" user");
		UtilisateurEJB ue = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
		switch(act){
			case "ajouter": {
				String username=req.getParameter("username");
				String password=req.getParameter("password");
				String prenom= req.getParameter("prenom");
				String nom= req.getParameter("nom");
				String email= req.getParameter("email");
				String adresse=req.getParameter("adresse");
			
				try {
					Utilisateur u=new Utilisateur(username, password, nom, prenom, email, adresse);
					ue.addUser(u);
				} catch (Exception e) {
					String error=e.getMessage();
					req.setAttribute("error", error);
					
				}
				break;
			}
			
			case "supprimer": {
				String username=req.getParameter("username");
				if (!username.equals("Admin"))
					ue.removeUser(username);
				break;
			}
		}
		req.getRequestDispatcher("/administrationUsers.jsp").forward(req, resp);
	}
}
