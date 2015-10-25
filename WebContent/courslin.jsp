<%@page import="business.Concept"%>
<%@page import="java.util.Iterator"%>
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
		
		boolean cop = (cidp != null);
		
		Cours cours = null;
		
		if(cop){
			if(cidp == null) request.getRequestDispatcher("cours.jsp").forward(request, response);
			long cid = Long.parseLong(cidp);
			CoursEJB cejb = new CoursEJB(getServletContext().getRealPath("/resources/"));
			cours = cejb.getCours(cid);
		}
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
		<title><%if(cop) out.print(cours.getTitre()); else out.print("Cours");%> 
				- Escientia, un site pour etudier online</title>
</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
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
				<a href="logout">Deconnectez</a>
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
		</div>
		<div id="header">
		<div class="article">
			<h1><%=cours.getTitre() %></h1>
			<h5><%=cours.getDescription().replaceAll("\n", "<br>") %></h5>
		</div>
		<div class="article">
			<%if (request.getAttribute("message") != null){ %>
			<h4 style="color: red;">    <%=request.getAttribute("message") %></h4>
			<%} %>
		<%
		if(cop) {
			%>
			<div class="list"> 
			<table>
			<%
			Iterator<Concept> concepts = cours.getConcepts().iterator();
			while (concepts.hasNext()){
				Concept concept= concepts.next();
		%>
		
			<tr><td><h5><a href="concept.jsp?cid=<%=cidp%>&con=<%=concept.getTitre()%>">
					<%=concept.getTitre()%></a></h5></td></tr>
		
		<%} 
			%>
			</table>
			</div>
			<%
		}
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