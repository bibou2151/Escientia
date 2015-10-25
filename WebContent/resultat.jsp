<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page errorPage="index.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
	<title>Votre resultat - Escientia, un site pour etudier online</title>
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
	</div>
	<div id="header">
		<div class="article">
		<h1>Bienvenue, <%if(user != null) out.print(user.getPrenom());%> </h1>
		</div>
		<div class="article">
			<%
			String cours = (String) request.getAttribute("cours");
			String concept = (String) request.getAttribute("concept");
			double resultat = (double) request.getAttribute("resultat");
			
			String[] line = (String[]) request.getSession().getAttribute("line");
			int progress = (int) request.getSession().getAttribute("progress");
			String target = (String) request.getAttribute("target");
			long couId = (long) request.getSession().getAttribute("coursId");

			if (request.getAttribute("done") != null) {
				request.getSession().setAttribute("line", null);
				request.getSession().setAttribute("coursId", null);
				request.getSession().setAttribute("target", null);
				request.getSession().setAttribute("progress", null);
			}
			%>
			<h1>Votre resultat dans <%=cours %>: <%=concept %> est: <em><%=resultat %>%</em><br>
				Vous êtes: 
				<%
				if (resultat>=50) {%>
					<span style="color: green">Admis</span><br>
					<%if (request.getAttribute("done") == null) { %>
						<a href="concept.jsp?cid=<%=couId%>&con=<%=line[progress]%>">Continuer.</a>
					<%} else {%>
						<%int r= (int) (Math.random()*2); String img = r==0 ? "yippie"+r+".jpg" : "yippie"+r+".gif";%>
						<img alt="" width="100%" src="images/<%=img%>">
						Félicitations !!!<br>
						Vous avez terminé l'étude de: <%=target %><br>
						<a href="cours.jsp?cid=<%=couId%>">Retourner au cours.</a>
				<%}} else { %>
					<span style="color: red">Ajournée</span><br>
					<a href="concept.jsp?cid=<%=couId%>&con=<%=concept%>">Retourner au concept.</a>
				<%}	%>
					
			</h1>
		</div>	
	</div>
	
	<div id="body">
		<div class="section">
			
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