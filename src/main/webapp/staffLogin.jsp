<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Staff Login</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Login Staff</h2>
        <form action="StaffLoginController" method="POST">
            <label for="staffId">Staff ID:</label>
            <input type="text" id="staffId" name="staffId" required>
            <button type="submit">Login</button>
        </form>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
        <% } %>
    </div>
</body>
</html>
