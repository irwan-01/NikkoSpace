<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Staff Login</title>
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
        <h2>Staff Login</h2>
        <form action="StaffLoginController" method="post">
            <label for="staffId">Staff ID:</label>
            <input type="text" id="staffId" name="staffId" required>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required><br>
            <label for="phoneNumber">Phone Number:</label>
            <input type="text" id="phoneNumber" name="phoneNumber" required><br>
            <label for="birthDate">Birth Date:</label>
            <input type="date" id="birthDate" name="birthDate" required><br>
            <label for="gender">Gender:</label>
            <select id="gender" name="gender" required>
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <button type="submit">Signup</button>
        </form>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
        <% } %>
    </div>
</body>
</html>
