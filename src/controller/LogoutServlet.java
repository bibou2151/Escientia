package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Utilisateur;

@WebServlet(name="logout",urlPatterns={"/logout"})
public class LogoutServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6339640521470758092L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			System.out.println("Successful Logout: "+((Utilisateur) req.getSession().getAttribute("user")).getUsername());
			req.getSession().setAttribute("user", null);
			req.getSession().setAttribute("admin", null);
		}catch(NullPointerException e){}
		resp.sendRedirect("index.jsp");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			System.out.println("Successful Logout: "+((Utilisateur) req.getSession().getAttribute("user")).getUsername());
			req.getSession().setAttribute("user", null);
			req.getSession().setAttribute("admin", null);
		}catch(NullPointerException e){}
		resp.sendRedirect("index.jsp");
	}
}
