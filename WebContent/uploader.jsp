<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="business.UtilisateurEJB"%>
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
	<style type="text/css" media="all">
		body {
			background: #fff;
		}
	</style>
	<script type="text/javascript">
	$('#i_submit').click( function() {
	    //check whether browser fully supports all File API
	    if (window.File && window.FileReader && window.FileList && window.Blob)
	    {
	        //get the file size and file type from file input field
	        var fsize = $('#i_file')[0].files[0].size;
	        
	        if(fsize>1048576) //do something if file size more than 1 mb (1048576)
	        {
	            alert(fsize +" bites\nToo big!");
	        }else{
	            alert(fsize +" bites\nYou are good to go!");
	        }
	    }else{
	        alert("Please upgrade your browser, because your current browser lacks some new features we need!");
	    }
	});

	</script>
	<title>FileUploader - Escientia un site pour etudier online</title>
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
long id = Long.parseLong(request.getParameter("cid"));
%>
<body>
	<h4 id="msg" style=" color: red;">
	<%
		if(request.getAttribute("error") != null)
			out.print(request.getAttribute("error")); 
	%></h4>
	<form action="uploadConcept" method="post" enctype="multipart/form-data">
		<input name="cid" type="hidden" value="<%=id%>">
		<table>
			<tr><td>
				<label for="file">Fichier: </label>
			<td>
				<input style=" width: 310px; " name="file" type="file" id="myfile" required>
		</table>
				<input class="button" type="submit" value="Télécharger" id="i_submit">
	</form>
</body>
</html>