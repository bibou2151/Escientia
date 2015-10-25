<%@page import="business.NotesCours"%>
<%@page import="business.NotesEJB"%>
<%@page import="business.Cours"%>
<%@page import="business.CoursEJB"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="css/jquery-ui.css" />
	<script src="js/jquery-1.9.1.js"></script>
	<script src="js/jquery-ui.js"></script>
  
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
	<title>Administration Cours - Escientia un site pour etudier online</title>
	
	<script>
	$(function() {
	    $( ".dialog" ).dialog({
	      autoOpen: false,
	      show: {
	        effect: "blind",
	        duration: 1000
	      },
	      hide: {
	        effect: "explode",
	        duration: 1000
	      }
	    });
	
	  
	  });
	  function show(cid) {
		    $( "#d"+cid ).dialog( "open" );
		  }
	</script>

</head>
<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");

if(user == null) {
	request.setAttribute("link", "administrationCours.jsp");
	request.getRequestDispatcher("login.jsp").forward(request, response);
} else {
	if(request.getSession().getAttribute("admin") == null) {
		response.sendRedirect("/index.jsp");
	}
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
			<li>
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
			<li class="selected">
				<a href="administrationCours.jsp">Administration Cours</a>
			</ul>
		</div>
		<% } %>
	</div>
	<!-- 
	<div id="body">
	<div class="featured">
		<div class="featured" style=" height : 300px;">
		<h2>Nouveau cours</h2>
		<h4 style=" color: red;">
				<%
					if(request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="createCours.jsp" method="get">
				<table>
				<tr><td>
					<label for="n">Nombre de concepts: </label>
					</td><td>
					<input name="n" type="number" required 
										onKeyPress='return event.charCode >= 48 && event.charCode <= 57'>
					</td>
				</tr><tr><td>
					<label for="titre">Titre du concepts: </label>
					</td><td>
					<input name="titre" type="text" required>
					</td>
				</tr>
				</table>
					<br>
					<input class="button" type="submit" value="Continuer">
				
				</form>
		</div>
		<div class="section">
				<div class="article">
				<h2>Bienvenue, Admin</h2>
				<p>Bienvenue à l'interface des cours.<br>
				D'ici vou pouvez ajouter et supprimer les cours
				</p>
				</div>
			</div>
		</div>
		-->
		<%CoursEJB cej = new CoursEJB(getServletContext().getRealPath("/resources/"));
			Iterator<Cours> list = cej.getAllCours().iterator();
		%>
		
		<div id="header">
			<div class="article">
			<div class="content-table">
			<h1>Cours: </h1>
			<table>
			<tbody>
				<tr>
					<th>Cours ID</th>
					<th>Titre du cours</th>
					<th>Nbr des concepts</th>
					<th>Nombre d'Etudiants</th>
					<th>Action</th>
				</tr>
				<%
				NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
				%>
				<%while(list.hasNext()){
					Cours c = list.next();
					ArrayList<NotesCours> notes = nej.getAllNotesCours(c.getId());
					%>
				<tr>
					<td><%= c.getId() %></td>
					<td><a href="cours.jsp?cid=<%= c.getId() %>"><%= c.getTitre() %></a></td>
					<td><%= c.getConcepts().size() %></td>
					<td><span   title="Afficer les étudiants" onclick="show(<%=c.getId()%>)" style="
										color: #0000EE; text-decoration: underline; cursor: pointer;">
							<%= notes.size() %> étudiants</span></td>
					
					
					<td>
						<form action="AdmCours" method="post">
							<input name="cid" type="hidden" value="<%= c.getId() %>">
							<input name="action" type="hidden" value="supprimer">
							<input class="button" type="submit" value="Supprimer">							
						</form>
						
					</td>
				</tr>
				
				<%} %>
			</tbody>
			</table>
			</div>
			</div>
		</div>
	</div>
	<%
	list = cej.getAllCours().iterator();
	%>
	
	<%while(list.hasNext()){
		Cours c = list.next();
		ArrayList<NotesCours> notes = nej.getAllNotesCours(c.getId());
		%>
	<div class="dialog" id="d<%=c.getId()%>">
      <h2>Etudiants de: <%=c.getTitre() %></h2>
      <div class="list">
      	<table>
			<%
			Iterator<NotesCours> nIt = notes.iterator();
			while(nIt.hasNext()) {
			 	NotesCours n = nIt.next();
			%>
				<tr><td><a href="editProfile.jsp?un=<%= n.getUsername() %>"><%= n.getUsername() %></a></td>
				<td><%=n.getCompletedConcepts() %>%</td></tr>
			<%} %>
		</table>
	</div>
    </div>
    <%} %>
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