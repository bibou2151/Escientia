package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.CoursEJB;

@WebServlet(name="AdmC", urlPatterns={"/AdmCours"})
public class AdminCoursServlet extends HttpServlet{

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
		System.out.println("Admin action = "+act+" cours");
		CoursEJB cej = new CoursEJB(getServletContext().getRealPath("/resources/"));
		switch(act){			
			case "supprimer": {
				long id = Long.parseLong(req.getParameter("cid"));
				cej.removeCours(id);
				break;
			}
		}
		resp.sendRedirect("administrationCours.jsp");
	}
}
