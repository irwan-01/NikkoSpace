<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Login</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <form action="LoginController" method="POST">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <button type="submit">Login</button>
        </form>

        <!-- Google Login Button -->
        <div class="g-signin2" data-onsucess="onSignIn"></div>
        <p>Don't have an account? <a href="signup.jsp">Signup here</a></p>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
        <% } %>

        <script src="https://apis.google.com/js/platform.js" async defer></script>
<script>
  function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    var id_token = googleUser.getAuthResponse().id_token;

    // Send the ID token to your server for verification and to fetch user data
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/yourapp/GoogleLoginController');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
      console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send('idtoken=' + id_token);
  }
</script>
    </div>
</body>
</html>
