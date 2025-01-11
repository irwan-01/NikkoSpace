<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Update Admin Profile</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Update Your Profile Admin</h2>
        
        <form action="UpdateProfileAdmin" method="POST">
            <div>
                <label for="username">Username:</label>
                <input type="text" name="username" id="username" value="<%= request.getAttribute("username") %>" required />
            </div>
            
            
            
            <!-- Password fields -->
            <div>
                <label for="currentPassword">Current Password:</label>
                <input type="password" name="currentPassword" id="currentPassword" required />
            </div>
            
            <div>
                <label for="newPassword">New Password:</label>
                <input type="password" name="newPassword" id="newPassword" required />
            </div>
            
            <div>
                <label for="confirmPassword">Confirm New Password:</label>
                <input type="password" name="confirmPassword" id="confirmPassword" required />
            </div>

            <div>
                <button type="submit">Update Profile</button>
            </div>
        </form>

        <a href="ProfileAdmin">Back to Profile</a>
        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>
</body>
</html>
