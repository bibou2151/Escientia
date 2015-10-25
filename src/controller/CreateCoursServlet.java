package controller;

import java.io.IOException;

import business.Concept;
import business.Cours;
import business.CoursEJB;
import business.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateCoursServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045385699077108064L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String resourcesPath = getServletContext().getRealPath("/resources/");
		CoursEJB cej = new CoursEJB(resourcesPath);
		
		Cours c = new Cours();
		
		try {
			//titre
			c.setTitre(req.getParameter("titre"));
			//description
			c.setDescription(req.getParameter("description"));
			//concepts
			int i = 1;
			while (req.getParameter("titre"+i) != null) {
				Concept con = new Concept();
				//titre
				con.setTitre(req.getParameter("titre"+i));
				//prerequis
				String[] prerequis = req.getParameter("prerequis"+i).split(";");
				con.addPrerequis(prerequis);
				//type de contenu
				con.setTitre(req.getParameter("typeContenu"+i));
				//file stuffs
			//	con.setContenu(req.getParameter("contenu"+i));
				//questions
				int j = 0;
				while (req.getParameter("header"+i+"q"+j) != null){
					Question q = new Question();
					q.setHeader(req.getParameter("header"+i+"q"+j));
					q.setType(req.getParameter("type"+i+"q"+j));
					//reponses
					int k = 0;
					while (req.getParameter("reponse"+i+"q"+j+"r"+k) != null) {
						q.addReponse(req.getParameter("reponse"+i+"q"+j+"r"+k));
						k++;
					}
					
					con.addQuestion(q);
					j++;
				}
				
				c.addConcept(con);
				i++;
			}
			
			cej.updateCours(c);
			
			
		} catch (Exception e) {
			req.setAttribute("error", e.getMessage());
			req.getRequestDispatcher("/administrationCours.jsp").forward(req, resp);
		}
	}
	
}