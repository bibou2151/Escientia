package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Utilisateur;
import business.UtilisateurEJB;

@WebServlet(name="insc", urlPatterns={"/inscrire"})
public class InscrireServlet extends HttpServlet{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1444806922885090827L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String prenom= req.getParameter("prenom");
		String nom= req.getParameter("nom");
		String email= req.getParameter("email");
		String adresse=req.getParameter("adresse");
		
		if(adresse == null)
			adresse = "";
		
		try {
			
			Utilisateur u=new Utilisateur(username, password, nom, prenom, email, adresse);
			UtilisateurEJB ue = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
			ue.addUser(u);
			
			req.getSession().setAttribute("user", u);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			
		} catch (Exception e) {
			String error=e.getMessage();
			req.setAttribute("error", error);
			req.getRequestDispatcher("/inscription.jsp").forward(req, resp);
		}
	}
		
}
