<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="ISO-8859-1">
  <title>Package View</title>
  <link rel="stylesheet" href="IndexPackageStyle.css">  </head>
<body>

<header>
  <%-- You can add your header content here --%>
</header>

<main>
  <div class="container">
    <h1>View Package</h1>

    <table border="1">
      <tr>
        <th>Name</th>
        <th>Value</th>
      </tr>

      <% 
        if (request.getAttribute("packageName") != null && request.getAttribute("packagePrice") != null) { 
      %>
      <tr>
        <td>Package Name</td>
        <td><%= request.getAttribute("packageName") %></td> 
      </tr>
      <tr>
        <td>Package Price</td>
        <td><%= request.getAttribute("packagePrice") %></td> 
      </tr>
      <% 
        } else { 
      %>
      <tr>
        <td colspan="2">No package found.</td> 
      </tr>
      <% 
        } 
      %>
    </table>

  </div>
</main>

</body>
</html>
