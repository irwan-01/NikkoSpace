<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="pack.connection.AzureSqlDatabaseConnection" %>
<%@ page import="java.sql.*" %>
<%@ page import="pack.model.Package" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Pet Grooming Package</title>
<link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    
    <header>
        <div id="menu-bar" class="fa fa-bars"></div>
        <a href="#" class="logo"><img class="capal_logo" src="img/CAPAL LOGO.png" alt="Logo"></a>
        <nav class="navbar">
            <a href="index.jsp">Home</a>
            <a href="index.jsp">Appointment</a>
            <a href="indexPet.jsp">Pet</a>
            <a href="indexProfile.jsp">Profile</a>
        </nav>
        <div class="icons">
            <a href="indexProfile.jsp"><i></i></a>
        </div>
    </header>

    <div class="container">
        <h2>All Products</h2>

        <%-- Display success or error message if any --%>
        <% 
            String message = (String) request.getAttribute("message");
            String error = (String) request.getAttribute("error");

            if (message != null) {
                out.println("<p style='color: green;'>" + message + "</p>"); 
            }

            if (error != null) {
                out.println("<p style='color: red;'>" + error + "</p>"); 
            }
        %>
</body>
</html>
