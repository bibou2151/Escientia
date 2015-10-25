package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Utilisateur;
import business.UtilisateurEJB;

@WebServlet(name="login",urlPatterns={"/login"})
public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7746504385056114642L;

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
		
		try{
			UtilisateurEJB ue = new UtilisateurEJB(getServletContext().getRealPath("/resources/"));
			
			Utilisateur u = ue.authentifyUser(username, password);
			
			if(u.getUsername().contains("Admin")) 
				req.getSession(true).setAttribute("admin", u);
			req.getSession(true).setAttribute("user", u);
			
			System.out.println("Successful login: "+u.getUsername());
			/*
			if(req.getParameter("save") != null) {
				req.getSession(true).setMaxInactiveInterval(2952000);
				System.out.println("Session saved for 30 days.");
			}
			*/
			String link = req.getParameter("link");
	//		req.getRequestDispatcher((link == null) ? "/index.jsp" : "/"+link).forward(req, resp);
			resp.sendRedirect((link == null) ? "index.jsp" : link);
			
		} catch (Exception e) {
			req.setAttribute("error", e.getMessage());
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}
	
}
