<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Signup</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
    <script>
        function validatePasswords() {
            var password = document.getElementById("password").value.trim;
            var confirmPassword = document.getElementById("confirmPassword").value.trim;
            if (password !== confirmPassword) {
                alert("Passwords do not match!");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Signup</h2>
        
        <form action="StaffCreateController" method="POST" onsubmit="return validatePasswords();>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <br>
            <button type="submit">Signup</button>
        </form>
        <p>Already have an account? <a href="loginAdmin.jsp">Login here</a></p>
        <c:if test="${not empty errorMessage}">
            <p style="color: red;">${errorMessage}</p>
        </c:if>
    </div>
</body>
</html>
