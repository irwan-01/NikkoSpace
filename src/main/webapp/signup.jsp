<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Signup</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Signup</h2>
        <form action="SignupController" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <button type="submit">Signup</button>
        </form>
        <p>Already have an account? <a href="login.jsp">Login here</a></p>
        <c:if test="${not empty errorMessage}">
            <p style="color: red;">${errorMessage}</p>
        </c:if>
    </div>
</body>
</html>
