package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Concept;
import business.Cours;
import business.CoursEJB;
import business.NotesCours;
import business.NotesEJB;
import business.Utilisateur;

@WebServlet(name = "start", urlPatterns = {"/start"})
public class StartStudyingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4253045388812478900L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
		long cid = Long.parseLong(req.getParameter("cid"));
		String con = req.getParameter("con");
		if (user == null) {
			
			req.setAttribute("link", "cours.jsp?cid="+cid);
			req.setAttribute("error", "Vous devez connceter pour étudier.");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
			
		} else {
		
			CoursEJB cejb = new CoursEJB(getServletContext().getRealPath("/resources/"));
			Cours cours = cejb.getCours(cid);
			Concept concept = cours.getConcept(con);
			
			NotesEJB nojb = new NotesEJB(getServletContext().getRealPath("/resources/"));
			NotesCours nc = nojb.getNotesCours(user, cid);
			
			if (!nc.isAlreadyStudying(con)) {
				nc = nojb.makeNewTarget(user, cid, con);
				System.out.println(nc.getUsername()+" has started studying: "
										+cours.getTitre()+", "+nc.getTarget());
			}
			
			nojb.updateNotesCours(user, nc);
			String[] studyLine = cours.getPrerequisListe(concept.getTitre());
			
			int prog = nc.getProgress();
			
			
			req.getSession().setAttribute("line", studyLine);
			req.getSession().setAttribute("coursId", cours.getId());
			req.getSession().setAttribute("target", concept.getTitre());
			req.getSession().setAttribute("progress", prog);
			
			resp.sendRedirect("concept.jsp?cid="+cours.getId()+"&con="+studyLine[prog]);
		}
	}
}
