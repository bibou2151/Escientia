package controller;

import java.io.IOException;
import java.util.ArrayList;

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
import business.Question;
import business.Utilisateur;

@WebServlet(name="test", urlPatterns={"/corriger"})
public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4851018350593901176L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("index.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Utilisateur user = (Utilisateur) req.getSession().getAttribute("user");
		String cidp = req.getParameter("cid");
		String con = req.getParameter("con");
		
		if(user == null || cidp == null || con == null)
			req.getRequestDispatcher("/cours.jsp").forward(req, resp);
		
		long cid = Long.parseLong(req.getParameter("cid"));
		
		CoursEJB cejb = new CoursEJB(getServletContext().getRealPath("/resources/"));
		Cours cours = cejb.getCours(cid);
		Concept concept = cours.getConcept(con);
		
		NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
		NotesCours nc = nej.getNotesCours(user, cid);
		
		//if no target
		if(nc.getTarget() == null)
			resp.sendRedirect("cours.jsp?cid=" + cid);
		
		ArrayList<String> reponses = new ArrayList<>();
		int i=0;
		while (req.getParameter("reponse"+i) != null) {
			reponses.add(req.getParameter("reponse"+i));
			i++;
		}
		
		double resultat = 0.;
		try {
			resultat = getResultat(reponses, concept);
		}catch(ArrayIndexOutOfBoundsException e){
			req.getRequestDispatcher("/test.jsp?cid="+cid+"&con="+con).forward(req, resp);
		}
		
		if(nc.getNote(con) < resultat) {
			
			nc.setNote(con, resultat);
			
		} //else { resultat = nc.getNote(con); }
		System.out.println("Test pour "+user.getUsername()+" sur "+cours.getTitre()+": "+concept.getTitre()+"\n"
				+ "Resultat: "+resultat+"%");
		String[] line = null; String target = null; int progress = -1;

		try {
			line = (String[]) req.getSession().getAttribute("line");
			target = (String) req.getSession().getAttribute("target");
			progress = (int) req.getSession().getAttribute("progress");
			
		} catch (NullPointerException e) { }
		/*
		if (target != nc.getTarget()) {
			resp.sendRedirect("cours.jsp?cid=" + cid);
		}
		*/
		if (resultat >= 50) {
			progress++;
			nc.setProgress(progress);
		}
		
		req.getSession().setAttribute("line", line);
		req.getSession().setAttribute("coursId", cours.getId());
		req.getSession().setAttribute("target", nc.getTarget());
		req.getSession().setAttribute("progress", progress);
		
		nej.updateNotesCours(user, nc);
		if (nc.getTarget().equals("")) {
			req.setAttribute("done", "finished");
			System.out.println(nc.getUsername()+" has finished studying: "
					+cours.getTitre()+", "+target);
		}

		req.setAttribute("cours", cours.getTitre());
		req.setAttribute("coursID", cours.getId());
		req.setAttribute("concept", concept.getTitre());
		req.setAttribute("target", target);
		req.setAttribute("resultat", resultat);
		
		req.getRequestDispatcher("resultat.jsp").forward(req, resp);
	}
	
	/**
	 * Cette fonction compare les reponse au reponses correctes et retoune la resultat du test
	 * @param reponses
	 * @param concept
	 * @return resultat%
	 */
	private static double getResultat(ArrayList<String> reponses, Concept concept) {
		double resultat = 0.;
		
			String[] res = new String[reponses.size()];
			reponses.toArray(res);
			Question[] que = new Question[concept.getQuestions().size()];
			concept.getQuestions().toArray(que);
			
			int reCorr = 0;
			for(int i = 0; i< que.length;i++) {
				if(que[i].getReponseCorrecte().equals(res[i])) {
					reCorr = reCorr+1;
				}
			}
			resultat = ((((double) reCorr)/que.length)*100);
			//
			resultat = Math.round(resultat*100)/100;
			
		
		return resultat;
	}
}
