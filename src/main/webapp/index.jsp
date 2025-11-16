<%-- 
    Document   : index
    Created on : 16 nov 2025, 08:47:57
    Author     : lulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Redirigiendo...</title>
</head>
<body>
    <%
        String usuario = (String) session.getAttribute("usuario");
        if (usuario != null) {
            response.sendRedirect(request.getContextPath() + "/Usuario.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        }
    %>
</body>
</html>