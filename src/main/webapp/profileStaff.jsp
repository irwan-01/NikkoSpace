<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Staff Profile</title>
    <link rel="stylesheet" href="IndexPackageStyle.css">
</head>
<body>
    <div class="container">
        <h2>Welcome, <%= request.getAttribute("staffName") %>!</h2>

        <h3>Your Staff Profile</h3>
        <table>
            <tr>
                <th>Full Name:</th>
                <td><%= request.getAttribute("staffName") %></td>
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
                <th>Position:</th>
                <td><%= request.getAttribute("position") %></td>
            </tr>
            <tr>
                <th>Department:</th>
                <td><%= request.getAttribute("department") %></td>
            </tr>
            <tr>
                <th>Hire Date:</th>
                <td><%= request.getAttribute("hireDate") %></td>
            </tr>
        </table>
        <a href="updateStaffProfile.jsp">Update Profile</a>
        <a href="Logout.jsp">Logout</a>
    </div>
</body>
</html>
