<%@page import="business.Notes"%>
<%@page import="java.util.Iterator"%>
<%@page import="business.NotesCours"%>
<%@page import="business.NotesEJB"%>
<%@page import="business.Concept"%>
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
	<title><%= concept.getTitre()%> - Escientia, un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
if (user == null) {
	
	request.setAttribute("link", "concept.jsp?cid="+cidp+"&con="+con);
	request.setAttribute("error", "Vous devez connceter pour étudier.");
	request.getRequestDispatcher("login.jsp").forward(request, response);
	
}
%>
<%
NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
NotesCours notes = nej.getNotesCours(user, cours.getId());

if (!notes.isEntitled(concept.getTitre())) {
	String msg = "Pour étudier "+con+", vous devez d'abord étudier: ";
	Iterator<String> preres = concept.getPrerequis().iterator();
	while (preres.hasNext()) {
		String pre = preres.next();
		msg += "<a href=\"concept.jsp?cid="+cidp+"&con="+pre+"\">"+pre+"</a>, ";
	}
	request.setAttribute("message", msg);
	request.getRequestDispatcher("/cours.jsp?cid="+cidp).forward(request, response);
}
%>
<%
String[] line = null; String target = null; int progress = -1;

try {
	target = notes.getTarget();
	progress = notes.getProgress();
	line = cours.getPrerequisListe(target);
	
} catch(NullPointerException e) {
	response.sendRedirect("cours.jsp?cid="+cidp);	
}
if(!line[progress].equals(con)) {
	response.sendRedirect("cours.jsp?cid="+cidp);
}
%>
<%
//skipser
/**/
if (progress < (line.length - 1)) {
	if(notes.isExpertIn(concept.getTitre())) {
		int n = notes.getProgress();
		n++;
		notes.setProgress(n);
		nej.updateNotesCours(user, notes);
		response.sendRedirect("concept.jsp?cid="+cid+"&con="+ line[n]);
	}
}
/**/
%>
<%
//other content
if (request.getParameter("content") != null) {
	notes.setPrefferedMeduim(request.getParameter("content"));
	nej.updateNotesCours(user, notes);
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
		<h1>Bienvenue, <%if(user != null) out.print(user.getPrenom());%> </h1>
		</div>
		<div class="article">
			<div class="more">
				<h1><a href="cours.jsp?cid=<%=cours.getId()%>"><%=cours.getTitre()%></a>: <%=concept.getTitre() %>.</h1><br>
				<%if(concept.getAllContenus().size()>1) { %>
				<h5>Ce concept est aussi disponible sous d'autre formes:
					<%
					for (String cnt : concept.getAllContenus()) {
						out.print("<a href=\"concept.jsp?cid="+cid+"&con="+con+"&content="+cnt+"\">"+cnt+"</a>,");
					}
					%><br>
				</h5>
				<%} %>
			</div>
		</div>	
	</div>
	
	<div id="header">
		<div style="background-color: #B8B8B8;" class="article" >
			<div style=" background-color: #fff; display: table; width: 100%;">
				<div style="display: table-cell;"><h2>
										<a href="cours.jsp?cid=<%=cid%>">&lt;&lt;- Retourner au cours</a></h2></div>
				<div style="display: table-cell;"><h2 style="text-align: right;">
										<a href="test.jsp?cid=<%=cid%>&con=<%=con%>">Continuer au test -&gt;&gt;</a></h2></div>
			</div>
			<div style=" height : 900px;">
			
			<%if(concept != null){
			
			if(concept.getAllContenus().size()>0){
				String pref = notes.getPrefferedMeduim();
				
				String type = null;
				String contenu = null;
				try {
					contenu = concept.getContenu(pref);
					type = pref;
				} catch (NullPointerException e) {
					type = concept.getAllContenus().get( ((int) Math.random()*concept.getAllContenus().size()) );
					contenu = concept.getContenu(type);
				}
				
				String format = contenu.substring(contenu.lastIndexOf(".")+1);
				String media = contenu.replaceAll("\t", "").replaceAll("\n", "");
				
				
				switch (type){
				case "pdf":
					%>
					<object data="resources/concepts/<%=cidp%>/<%=media%>"
						 type="application/pdf" width="100%" height="100%">
  					<p>Il semble que votre naviguateur ne supporte pas le plugin PDF.
 						 Pas de probléme... vous pouvez <a href="concepts/<%=cidp%>/<%=media%>">
 						 cliquer ici pour télécharger le fichier PDF.</a></p>
					</object>
					<%
					break;
				case "image":
					%>
					<img width="100%" alt="" src="resources/concepts/<%=cidp%>/<%=media%>">
					<%
					break;
				case "audio":
					%>
					<audio controls>
						<source src="resources/concepts/<%=cidp%>/<%=media%>" type="audio/<%=format%>">
						<p>Désolé, votre naviguateur ne peut pas afficher se contenu.</p>
					</audio>
					<%
					break;
				case "video":
					%>
					<video width="100%" height="480" controls>
						<source src="resources/concepts/<%=cidp%>/<%=media%>" type="video/<%=format%>">
						<p>Désolé, votre naviguateur ne peut pas afficher se contenu.</p>
					</video>
					<%
					break;
				case "YouTube":
					%>
					<iframe width="100%" height="480" 
						src="http://www.youtube.com/embed/<%=media.substring(media.indexOf("=")) %>"
						 frameborder="0" allowfullscreen></iframe>
					<%
					break;
				}
				%>
				</div>
				<div style="text-align:center; background-color: #fff;">
				<a class="button" href="test.jsp?cid=<%=cid%>&con=<%=con%>">Testez-Vous</a>
				</div>
				<%
			}else{
				%>
				<h4>Désolé, ce contenu n'est pas encore disponible.</h4>
				<img alt="" src="images/gray_sad_cat.jpg">
				<%
			}}
				%>
		
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