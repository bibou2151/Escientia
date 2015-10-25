<%@page import="business.NotesCours"%>
<%@page import="business.NotesEJB"%>
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
<%@page errorPage="index.jsp" %>

<%
Utilisateur user = null;
user = (Utilisateur)request.getSession().getAttribute("user");
%>
<%
	String cidp = (String) request.getParameter("cid");
	
	boolean cop = (cidp != null) && (user != null);
	
	if ((cidp != null) && (user == null)) {
		
		request.setAttribute("link", "cours.jsp?cid="+cidp);
		request.setAttribute("error", "Vous devez connceter pour etudier.");
		request.getRequestDispatcher("login.jsp").forward(request, response);
		
	}

	
	Cours cours = null;
	CoursEJB cejb = new CoursEJB(getServletContext().getRealPath("/resources/"));
	if(cop){
		long cid = Long.parseLong(cidp);
	
		cours = cejb.getCours(cid);
	}
%>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/style.css" type="text/css">
	<link rel="SHORTCUT ICON" type="image/x-icon" href="images/icons/favicon.ico"/>
	<link rel="icon" type="image/x-icon" href="images/icons/favicon.ico" />
		<title><%= cop ?  cours.getTitre() : "Cours"%> - Escientia, un site pour etudier online</title>
	
	<%
	NotesCours notes = null;
	if(cop) { 

		NotesEJB nej = new NotesEJB(getServletContext().getRealPath("/resources/"));
		notes = nej.getNotesCours(user, cours.getId());
		
	
	%>
	<script src="js/raphael-min.js" type="text/javascript"></script>
	
	<%
	Iterator<Concept> concepts = cours.getConcepts().iterator();
	%>
	<script type="text/javascript">

	Raphael.fn.connection = function (obj1, obj2, line, bg) {
	    if (obj1.line && obj1.from && obj1.to) {
	        line = obj1;
	        obj1 = line.from;
	        obj2 = line.to;
	    }
	    var bb1 = obj1.getBBox(),
	        bb2 = obj2.getBBox(),
	        p = [
	 	        {x: bb1.x + bb1.width / 2, y: bb1.y - 1},
		        {x: bb1.x + bb1.width / 2, y: bb1.y - 1},
		        {x: bb1.x + bb1.width / 2, y: bb1.y - 1},
		        {x: bb1.x + bb1.width / 2, y: bb1.y - 1},

		        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
		        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
		        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
		        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1}],


	        d = {}, dis = [];
	    for (var i = 0; i < 4; i++) {
	        for (var j = 4; j < 8; j++) {
	            var dx = Math.abs(p[i].x - p[j].x),
	                dy = Math.abs(p[i].y - p[j].y);
	            if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
	                dis.push(dx + dy);
	                d[dis[dis.length - 1]] = [i, j];
	            }
	        }
	    }
	    if (dis.length == 0) {
	        var res = [0, 4];
	    } else {
	        res = d[Math.min.apply(Math, dis)];
	    }
	    var x1 = p[res[0]].x,
	        y1 = p[res[0]].y,
	        x4 = p[res[1]].x,
	        y4 = p[res[1]].y;
	    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
	    dy = Math.max(Math.abs(y1 - y4) / 2, 10);

	    var path = ["M", x1.toFixed(3), y1.toFixed(3), "L", x4.toFixed(3), y4.toFixed(3)].join(",");
	    if (line && line.line) {
	        line.bg && line.bg.attr({path: path, 'stroke-width': 1,
	            'stroke': 'blue',
	            'arrow-end': 'block-midium-midium',
	            'arrow-start': 'oval-narrow-short'      });
	        line.line.attr({path: path});
	    } else {
	        var color = typeof line == "string" ? line : "#000";
	        return {
	            bg: bg && bg.split && this.path(path).attr({stroke: bg.split("|")[0], fill: "none", "stroke-width": bg.split("|")[1] || 3}),
	            line: this.path(path).attr({stroke: color, fill: "none", "stroke-width": 5,'arrow-end': 'oval-narrow-short','arrow-start': 'block-midium-midium'}),
	            from: obj1,
	            to: obj2
	        };
	    }
	};
	wid = 936;
	var hei = 40,
		wei = 180;
	var bcolor = "#006600",
		fcolor = "#0066FF",
		gcolor = "#FF9900";
	window.onload = function () {
	    var p = Raphael("paper", wid, 1750);
		var connections = [];
	        	
	    var tatt = {
	    		cursor: 'pointer', 
	    		fill: '#fff',
	    		"font-size": "17px",
	    		"font-family": "Arial",
	    		"font-weight": "bold"
	    		},
	    	satt;
			
		<%
		int x = 468 , y = 90, i = 0;
		while(concepts.hasNext()) {
			Concept c = concepts.next();
			String t = c.getTitre().replaceAll(" ", "_");
		%>
		satt = {
	    		"stroke-width": 2,
	    		fill: <%
	    		if(notes.getNote(c.getTitre()) >= 50) {
	    			if (notes.isExpertIn(c.getTitre()))
	    				out.print("gcolor");
	    			else out.println("bcolor");
	    		}
	    		else out.print("fcolor");
	    		%>,
	    		stroke : '#003300',
				cursor:'pointer'};
		
	    x = <%=x%> - wei / 2;
	    y = <%=y%> - hei / 2;
	    var <%=t%>= p.rect(x ,y , wei, hei, 6).attr(satt);
	   	
	    var tx = x + wei / 2,
	    	ty = y + hei / 2;
	    	p.text(tx, ty, "<%=c.getTitre()%>")
	    		.attr(tatt)
	        	.click(function(){
	    			window.location.href = "start?cid=<%=cours.getId()%>&con=<%=c.getTitre()%>";
	    		});
			
			<%=t%>.click(function(){
	    			window.location.href = "start?cid=<%=cours.getId()%>&con=<%=c.getTitre()%>";
	    		});
			<%
			switch(i) {
				case 0:
					x = 312;
					y = y + 230;
				break;
				case 1:
					x = x + 312;
				break;
				case 2:
					x = 312;
					y = y + 230;
				break;
				case 3:
					x = 840;
				break;
				case 4:
					y = y + 230;
					x = 120;
				break;
				case 7:
					y = y + 230;
					x = 520;
				break;
				case 8:
					x = 800;
				break;
				case 9:
					y = y + 230;
					x = 420;
				break;
				case 11:
					y = y + 230;
					x = 420;
				break;
				default: x = x + 200;break;
			}
			i++;
			}%>
	    <%
	    concepts = cours.getConcepts().iterator();
	    while (concepts.hasNext()) {
	    	Concept c = concepts.next();
	    	String t = c.getTitre().replaceAll(" ", "_");
	    	Iterator<String> pres = c.getPrerequis().iterator();
	    	while (pres.hasNext()){
	    	String p = pres.next().replaceAll(" ", "_");;	
	    %>
			connections.push(p.connection(<%=t%>, <%=p%>, '#003300'));
	   	<%}}%> 
	};
		
	</script>
	<%} %>
</head>

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
		
	<%if(cop) { %>
		<div class="article">
			<%if (request.getAttribute("message") != null){ %>
			<h4 style="color: red;">    <%=request.getAttribute("message") %></h4>
			<%} %>
			<%if (notes.getTarget() != null || notes.getTarget() == "") {%>
			<h4>Continuer à étudier <a href="start?cid=<%=notes.getCoursID()%>&con=<%=notes.getTarget()%>"><%=notes.getTarget() %></a>.</h4>
			<%} %>
			<noscript><h4 style="color: red;">Pour accéder à toutes les fonctionnalités de ce site, vous devez activer JavaScript. 
					Voici les <a href="http://www.enable-javascript.com/fr/" target="_blank"> 
				instructions pour activer JavaScript dans votre navigateur Web</a>.<br/>
				Ou vous pouvez <a href="courslin.jsp?cid=<%=cidp%>">voir le contenu dans son forme hideuse</a>.
				</h4></noscript>
			
			<h1><%=cours.getTitre() %></h1>
			<h5><%=cours.getDescription().replaceAll("\n", "<br>") %></h5>
		</div>
		<div class="article">
			
			<div title="Selectionner ce que vous vouler étudier" id="paper"></div>
	
		
		</div>
	<%} else { %>
		<div class="article">
		<h1>Bienvenue, <%if(user != null) out.print(user.getPrenom());%></h1>
		
		</div>
		<div class="article">
		<h1>Voici les vours disponible dans notre site:</h1>
		</div>
		<div class="article">
		<% int i = 1; 
		Iterator<Cours> courses= cejb.getAllCours().iterator();
		while (courses.hasNext()){
			Cours cou = courses.next();
		%>
		
		
			<h1><%=i+": " %><a href="cours.jsp?cid=<%=cou.getId()%>"><%=cou.getTitre() %></a></h1><br>
		
		<%
		i++;
		}}%>
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