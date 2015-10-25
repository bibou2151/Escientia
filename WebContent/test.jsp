<%@page import="business.NotesCours"%>
<%@page import="business.NotesEJB"%>
<%@page import="business.Question"%>
<%@page import="java.util.Iterator"%>
<%@page import="business.Concept"%>
<%@page import="java.io.File"%>
<%@page import="business.Cours"%>
<%@page import="business.CoursEJB"%>
<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page errorPage="cours.jsp" %>
	<%
	String cidp = (String) request.getParameter("cid");
	
	if(cidp == null) request.getRequestDispatcher("cours.jsp").forward(request, response);
	String con = (String) request.getParameter("con");
	if(con == null) request.getRequestDispatcher("cours.jsp?cid="+cidp).forward(request, response);
	long cid = Long.parseLong(cidp);
	CoursEJB cejb = new CoursEJB(getServletContext().getRealPath("/resources/"));
	Cours cours = cejb.getCours(cid);
	Concept concept = cours.getConcept(con);
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
		<title><%= concept.getTitre()%> 
				- Escientia, un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
if (user == null) {
	
	request.setAttribute("link", "test.jsp?cid="+cidp+"&con="+con);
	request.setAttribute("error", "Vous devez connceter pour faire le test.");
	request.getRequestDispatcher("login.jsp").forward(request, response);
	
}
%>
<%
NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
NotesCours notes = nej.getNotesCours(user, cours.getId());

if (!notes.isEntitled(concept.getTitre())) {
	String msg = "Pour étudier "+con+", vous devez d'abord étudier ";
	Iterator<String> preres = concept.getPrerequis().iterator();
	while (preres.hasNext()) {
		String pre = preres.next();
		msg += "<a href=\"concept.jsp?cid="+cidp+"&con="+pre+"\">"+pre+"</a>, ";
	}
	request.setAttribute("message", msg);
	request.getRequestDispatcher("/cours.jsp?cid="+cidp).forward(request, response);
}
%>
<body>
	<div id="header">
		<div class="section">
			<div class="logo">
				<a href="index.jsp">Escientia</a>
			</div>
			<ul>
			<li>
				<a href="index.jsp">Acceuil</a>
			</li>
			<li class="selected">
				<a href="cours.jsp">Cours</a>
			</li>
			<%if(user != null){ %>
			<li>
				<a href="compte.jsp">Compte</a>
			</li>
			<li>
				<a href="logout">Déconnectez</a>
			</li>
			<%}else{ %>
			<li>				
				<a href="login.jsp">Connexion</a>
			</li>
			<%} %>
			<li>
				<a href="about.jsp">A propos</a>
			</li>
			</ul>
		</div>
		<%if(request.getSession().getAttribute("admin") != null){ %>
		<div class="section">
			<ul>
			<li>
				<a href="administrationUsers.jsp">Administration Utilisateurs</a>
			</li>
			<li>
				<a href="administrationCours.jsp">Administration Cours</a>
			</ul>
		</div>
		<% } %>
		<div class="article">
		<h1>Bienvenue, <%if(user != null) out.print(user.getPrenom());%></h1>
		
		</div>
		<div class="article">
			<div class="more">
				<h1><a href="cours.jsp?cid=<%=cours.getId()%>"><%=cours.getTitre()%>:</a>
				 <a href="concept.jsp?cid=<%=cours.getId()%>&con=<%=concept.getTitre()%>">
				 		<%=" "+concept.getTitre() %>.</a>
				 		</h1>
			</div>
		</div>	
	</div>
	<div id="body">
		<div class="section">
			<div class="featured">
			<h2>Test de <%=concept.getTitre() %></h2>
			
			<form action="corriger" method="post">
			<input name="cid" type="hidden" value="<%=cid%>">
			<input name="con" type="hidden" value="<%=con%>">
			
			<%int i =0;
			Iterator<Question> questions = concept.getQuestions().iterator();
			while(questions.hasNext()){
				Question q = questions.next();
			%>
			<fieldset>
			<legend><%=q.getHeader() %></legend>
				<div>
				<%int j = 0;
				Iterator<String> rep = q.getReponses().iterator();
				while(rep.hasNext()){
					String r= rep.next();
				%>
				<label for="<%=j%>reponse<%=i%>">
				<input class="radio" id="<%=j%>reponse<%=i%>" name="reponse<%=i%>" type="radio" value="<%=r%>" required>
				<span><%="	"+r%></span>
				</label><br>
				
				<%j++;} %>
				</div>
			</fieldset>
			<%
			i++;
			} %>
			<br>
			<% if(i>0){%>
			<input class="button" type="submit" value="Soumettre">
			<%}else{ %>
			<h4>Désolé, ce contenu n'est pas encore disponible.</h4>
			<img alt="" style=" width: 440px; margin-right: -10px;" width="auto" src="images/gray_sad_cat.jpg">
			<%}%>
			</form>
			</div>
			<div class="section">
				<div class="article">
				<h2>Comment faire le test:</h2>
				<p>- Selectionnez les reponses correctes et cliquez sur "Soumettre".<br>
				- Tout les questions doivent être repondus.<br>
				- Vous pouvez refaire le test n'importe quand vous voulez<br>
				- Si vous repondez 50% des questions correctement, vous �tes consider� comme admis pour ce concept.<br>
				- Si vous réussissez dans ce test, vous pouvez passer à d'autre concepts.<br>
				- Si vous avez déjà fait ce teste, votre meilleur resutat sera sauveguardée.<br>
				</p>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<div>
			<div class="connect">
				<a href="http://stackoverflow.com/users/4824308/robert-f" id="twitter">twitter</a>
				<a href="http://stackoverflow.com/users/4824308/robert-f" id="facebook">facebook</a>
				<a href="https://github.com/bibou2151" id="googleplus">googleplus</a>
				<a href="https://github.com/bibou2151" id="pinterest">pinterest</a>
			</div>
			<p>
				&copy; copyright 2015 | all rights reserved.
			</p>
		</div>
	</div>
</body>
</html>