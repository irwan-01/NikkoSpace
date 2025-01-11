<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Update Profile</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Update Your Profile</h2>
        
        <form action="UpdateProfileController" method="POST">
            <input type="hidden" name="userId" value="<%= request.getAttribute("userId") %>" />
            
            <div>
                <label for="username">Username:</label>
                <input type="text" name="username" id="username" value="<%= request.getAttribute("username") %>" required />
            </div>
            
            <div>
                <label for="email">Email:</label>
                <input type="email" name="email" id="email" value="<%= request.getAttribute("email") %>" required />
            </div>
            
            <div>
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" name="phoneNumber" id="phoneNumber" value="<%= request.getAttribute("phoneNumber") %>" required />
            </div>
            
            <div>
                <label for="birthDate">Birth Date:</label>
                <input type="date" name="birthDate" id="birthDate" value="<%= request.getAttribute("birthDate") %>" required />
            </div>
            
            <div>
                <label for="gender">Gender:</label>
                <select name="gender" id="gender" required>
                    <option value="Male" <%= "Male".equals(request.getAttribute("gender")) ? "selected" : "" %>>Male</option>
                    <option value="Female" <%= "Female".equals(request.getAttribute("gender")) ? "selected" : "" %>>Female</option>
                    <option value="Other" <%= "Other".equals(request.getAttribute("gender")) ? "selected" : "" %>>Other</option>
                </select>
            </div>
            
            <div>
                <label for="password">New Password:</label>
                <input type="password" name="password" id="password" placeholder="Enter new password" />
            </div>
            
            <div>
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm new password" />
            </div>
            
            <div>
                <button type="submit">Update Profile</button>
            </div>
        </form>

        <a href="profile.jsp">Back to Profile</a>
        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>
</body>
</html>
