<%-- 
    Document   : Usuario
    Created on : 16 nov 2025, 08:35:09
    Author     : lulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Menú Principal - Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
      
        body {
            background: #f8f9fa; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
           
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .menu-container {
            margin-top: 50px;
        }
        .menu-card {
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            border: none;
            transition: transform 0.3s;
        }
        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .menu-icon {
            font-size: 4rem;
            margin-bottom: 20px;
        }
        .btn-menu {
            width: 100%;
            padding: 30px;
            font-size: 1.3rem;
            font-weight: bold;
            border-radius: 15px;
            transition: all 0.3s;
            border: none;
        }
       
    </style>
</head>
<body>
    <%
        
        String usuario = (String) session.getAttribute("usuario");
        String nombre = (String) session.getAttribute("nombre");
        
        if (usuario == null) {
           
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
    %>
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary"> 
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="bi bi-shield-check"></i> Sistema de Gestión de Activos Intangibles
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3 text-white">
                    <i class="bi bi-person-circle"></i> <%= nombre != null ? nombre : usuario %>
                </span>
                <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/ServletControlador?accion=Logout">
                    <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container menu-container">
        <div class="card menu-card">
            
            <div class="bg-primary text-white p-4 text-center" style="border-radius: 15px 15px 0 0;">
                <i class="bi bi-house-door-fill" style="font-size: 3rem;"></i>
                <h2 class="mt-3">Menú Principal</h2>
                <p class="mb-0">Bienvenido, <%= nombre != null ? nombre : usuario %></p>
            </div>
            
            <div class="card-body p-5">
                <div class="row g-4">
                    
                    <div class="col-md-4">
                        <button class="btn btn-menu btn-primary text-white" 
                                onclick="window.location.href='${pageContext.request.contextPath}/IntangibleControlador?accion=listar'">
                            <i class="bi bi-file-earmark-text menu-icon"></i>
                            <h4>Gestionar Intangibles</h4>
                            <p class="mb-0">Administrar activos intangibles</p>
                        </button>
                    </div>
                    
                    <div class="col-md-4">
                        <button class="btn btn-menu btn-success text-white" 
                                onclick="window.location.href='${pageContext.request.contextPath}/paginas/reporte.jsp'">
                            <i class="bi bi-graph-up-arrow menu-icon"></i>
                            <h4>Reporte Financiero y Contable</h4>
                            <p class="mb-0">Ver reportes financieros</p>
                        </button>
                    </div>
                    
                    <div class="col-md-4">
                        <button class="btn btn-menu btn-secondary text-white" 
                                onclick="window.location.href='${pageContext.request.contextPath}/paginas/historial.jsp'">
                            <i class="bi bi-clock-history menu-icon"></i>
                            <h4>Historial</h4>
                            <p class="mb-0">Ver historial de operaciones</p>
                        </button>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>