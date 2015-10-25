<%@page import="business.Cours"%>
<%@page import="business.CoursEJB"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="business.Utilisateur"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page errorPage="administrationCours.jsp" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
	<title>Cree Cours - Escientia un site pour etudier online</title>
	<script type="text/javascript">
	document.getElementById('frame1').contentWindow.targetFunction();
		
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
<%
int n = 0;
try {
	n = Integer.parseInt(request.getParameter("n"));
	if(n < 1) throw new Exception("Il faut au moins un concept");
}catch (NumberFormatException e) {
	String error = "Saizes le nombre de concepts dans ce cours.";
	request.setAttribute("error", error);
	request.getRequestDispatcher("administrationCours.jsp").forward(request, response);
}catch (Exception e2) {
	request.setAttribute("error", e2.getMessage());
	request.getRequestDispatcher("administrationCours.jsp").forward(request, response);
}

String titre = request.getParameter("titre");

if(titre == null || titre.length() == 0){
	String error = "Saizes le titre du cours.";
	request.setAttribute("error", error);
	request.getRequestDispatcher("administrationCours.jsp").forward(request, response);
}

%>
<%
Cours c = new Cours();

CoursEJB cej  = new CoursEJB(getServletContext().getRealPath("/resources/"));

//un peut d'initialisation
c.setDescription("");
c.setTitre(titre);

long id = -1;

try {
	id = cej.addCours(c);
} catch (Exception e) {
	request.setAttribute("error", e.getMessage());
	request.getRequestDispatcher("administrationCours.jsp").forward(request, response);
}

c.setId(id);

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
				<a href="logout">D�connectez</a>
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
	
	<div id="body">
	<div class="featured">
		<div class="featured">
		<h2>Nouveau cours</h2>
		<h4 style=" color: red;">
				<%
					if (request.getAttribute("error") != null)
					out.print(request.getAttribute("error")); 
				%></h4>
				<form action="AdmCours" method="post">
				<input name="titre" type="hidden" value="<%=titre%>" >
				<label>Description du cours</label>
				<textarea name="description" required rows="7" cols="50"></textarea> 
				<table>
				<tr><td><br>
				<%for(int i = 1; i <= n; i++){ %>
				<tr><td><h3 style="text-decoration: underline;">Concept <%=i %>:</h3></td></tr>
				<tr><td>
					<label for="titre<%=i%>">Titre du concepts: </label>
					</td><td>
					<input name="titre<%=i%>" type="text" required>
					</td>
				</tr><tr><td>
					<label for="prerequis<%=i%>">Prerequis: <br><span style="font-size: 10px;"> pas d'espace et <br>separer par des </span>;</label>
					</td><td>
					<input name="prerequis<%=i%>" type="text" required>
					</td>
				</tr><tr><td>
					<label for="typeContenu<%=i%>">Type de contenu:</label>
					</td><td>
					<select name="typeContenu<%=i%>" required style="width:226px;">
						<option>pdf</option>
						<option>image</option>
						<option>audio</option>
						<option>video</option>
						<option>!!!!!!!Youtube</option>
					</select>
					<input name="contenu<%=i%>" id="anchor<%=i%>" type="hidden">
					</td>
				</tr><tr><td colspan="2">
					<iframe src="uploader.jsp?cid=<%=id%>" width="380px" scrolling="no" id="frame<%=id%>" onLoad="alert(this.contentWindow.location);"
					style="
						border: none; padding-top: 8px;
					"></iframe>
				</tr><tr><td>
					<label for="prerequis<%=i%>">Q: ;</label>
					</td><td>
					<input name="prerequis<%=i%>" type="text" required>
					</td>
				<tr><td><br><tr><td><br>
				<%} %>
				</table>
					<br>
					<input class="button" type="submit" value="Ajouter cours">
				</form>
		</div>
		<div class="section">
				<div class="article">
				<h2>Bienvenue, Admin</h2>
				<p>Bienvenue � l'interface de creation de cours.<br>
				- 
				</p>
				</div>
			</div>
		</div>
		
		<div id="header">
			<div class="article">
			
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