<%-- 
    Document   : listadoAmortizaciones
    Created on : 20 nov 2025
    Author     : monic
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="modelo.DetalleAmortizacion" %>
<%@ page import="datos.IntangibleDAO" %>
<%@ page import="modelo.Intangible" %>
<%@ page session="true" %>
<%
    request.setCharacterEncoding("UTF-8");
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
    
    List<DetalleAmortizacion> lista = (List<DetalleAmortizacion>) request.getAttribute("listaAmortizaciones");
    String mensaje = request.getParameter("mensaje");
    if (mensaje != null) {
        try {
            mensaje = URLDecoder.decode(mensaje, "UTF-8");
        } catch (Exception e) {
            // Si falla la decodificación, usar el mensaje original
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Amortizaciones - Sistema de Gestión de Activos Intangibles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/listado.css">
    <style>
        body { 
            background: #f4f6f9;
            font-family: 'Inter', 'Segoe UI', -apple-system, BlinkMacSystemFont, 'Roboto', sans-serif;
        }
        
        .page-header {
            background: #283593;
            color: white;
            padding: 40px 45px;
            border-radius: 12px 12px 0 0;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
            margin-bottom: 0;
            border-top: 4px solid #1565c0;
        }
        
        .page-header h1 {
            font-size: 32px;
            font-weight: 700;
            margin: 0;
            letter-spacing: -0.5px;
            display: flex;
            align-items: center;
            gap: 12px;
        }
        
        .page-header h1 i {
            font-size: 36px;
            opacity: 0.95;
        }
        
        .page-content {
            background: #ffffff;
            border-radius: 0 0 12px 12px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 2px 8px rgba(0, 0, 0, 0.06);
            padding: 35px 40px;
            border-top: none;
        }
        
        table thead { 
            background: #1565c0;
            color: white; 
        }
        
        .btn-success {
            background: #1565c0;
            border: none;
            font-weight: 700;
            padding: 14px 28px;
            border-radius: 10px;
            transition: all 0.3s ease;
            box-shadow: 0 6px 20px rgba(21, 101, 192, 0.4);
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .btn-success:hover {
            background: #0d47a1;
            transform: translateY(-4px) scale(1.05);
            box-shadow: 0 10px 30px rgba(21, 101, 192, 0.6);
        }
        
        .btn-success i {
            margin-right: 8px;
        }
    </style>
</head>
<body>
    <!-- NAVBAR -->
    <jsp:include page="/navbar.jsp"/>

    <div class="container mt-5">
        <div class="page-header">
            <h1>
                <i class="bi bi-calculator"></i>
                Amortizaciones Registradas
            </h1>
        </div>
        
        <div class="page-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <p class="text-muted mb-0">Gestión y consulta de amortizaciones de activos intangibles</p>
                <div>
                    <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=procesarAutomatico" 
                       class="btn btn-success">
                        <i class="bi bi-gear"></i> Procesar Amortizaciones Automáticamente
                    </a>
                </div>
            </div>

        <% if (mensaje != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= mensaje %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <!-- Buscador -->
        <div class="row mb-3">
            <div class="col-md-4">
                <input type="text" id="buscar" class="form-control" placeholder="Buscar en todas las columnas...">
            </div>
        </div>

        <!-- Tabla -->
        <div class="table-responsive">
            <table id="tablaAmortizaciones" class="table table-bordered table-hover align-middle">
                <thead class="text-center">
                    <tr>
                        <th>Código Intangible</th>
                        <th>Nombre Intangible</th>
                        <th>N° Cuota</th>
                        <th>Mes</th>
                        <th>Año</th>
                        <th>Monto</th>
                        <th>Fecha Cuota</th>
                        <th>Fecha Registro</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (lista != null && !lista.isEmpty()) {
                            IntangibleDAO intangibleDAO = new IntangibleDAO();
                            for (DetalleAmortizacion detalle : lista) {
                                Intangible intangible = intangibleDAO.obtenerIntangiblePorId(detalle.getIdIntangible());
                                String nombreIntangible = intangible != null ? intangible.getNombre_intangible() : "N/A";
                                String codigoIntangible = intangible != null ? intangible.getCodigo() : "N/A";
                                
                                String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                                 "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                    %>
                        <tr>
                            <td><%= codigoIntangible %></td>
                            <td><%= nombreIntangible %></td>
                            <td class="text-center"><%= detalle.getNumero_cuota() %></td>
                            <td><%= meses[detalle.getMes()] %></td>
                            <td class="text-center"><%= detalle.getAnio() %></td>
                            <td class="text-end">$ <%= String.format("%.2f", detalle.getMonto()) %></td>
                            <td><%= detalle.getFechaCuota() != null ? detalle.getFechaCuota() : "N/A" %></td>
                            <td><%= detalle.getFechaRegistro() != null ? detalle.getFechaRegistro() : "N/A" %></td>
                            <td class="text-center">
                                <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=verPorIntangible&idintangible=<%= detalle.getIdIntangible() %>" 
                                   class="btn btn-sm btn-info" title="Ver todas las amortizaciones de este intangible">
                                    Ver
                                </a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="9" class="text-center text-muted">No hay amortizaciones registradas.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        </div>
    </div>

    <!-- Script para búsqueda -->
    <script>
        document.getElementById("buscar").addEventListener("keyup", function () {
            let filtro = this.value.toLowerCase();
            let filas = document.querySelectorAll("#tablaAmortizaciones tbody tr");
            filas.forEach(fila => {
                let textoFila = fila.innerText.toLowerCase();
                if (textoFila.includes(filtro)) {
                    fila.style.display = "";
                } else {
                    fila.style.display = "none";
                }
            });
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

