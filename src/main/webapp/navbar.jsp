<%-- 
    Document   : navbar
    Created on : 13 nov 2025, 15:21:57
    Author     : monic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/navbar.css">

<nav class="navbar">
    <div class="navbar-inner">
        <div class="navbar-brand">
            Innova Licence
        </div>

        <div class="navbar-menu">
            <a href="index.jsp">Inicio</a>
         <a href="<%= request.getContextPath() %>/IntangibleControlador?accion=listar"> Gestión </a>
            <a href="#">Reporte</a>
            <a href="#">Historial</a>
        </div>
    </div>
</nav>