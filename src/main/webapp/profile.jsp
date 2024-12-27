<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Profile</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Welcome, <%= request.getAttribute("username") %>!</h2>
        
        <h3>Your Profile</h3>
        <table>
            <tr>
                <th>Full Name:</th>
                <td><%= request.getAttribute("fullName") %></td>
            </tr>
            <tr>
                <th>Email:</th>
                <td><%= request.getAttribute("email") %></td>
            </tr>
            <tr>
                <th>Phone Number:</th>
                <td><%= request.getAttribute("phoneNumber") %></td>
            </tr>
            <tr>
                <th>Birth Date:</th>
                <td><%= request.getAttribute("birthDate") %></td>
            </tr>
            <tr>
                <th>Gender:</th>
                <td><%= request.getAttribute("gender") %></td>
            </tr>
        </table>
        
        <a href="Logout.jsp">Logout</a>
    </div>
</body>
</html>
