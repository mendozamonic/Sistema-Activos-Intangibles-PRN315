<%-- 
    Document   : amortizacionesPorIntangible
    Created on : 20 nov 2025
    Author     : monic
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.DetalleAmortizacion" %>
<%@ page import="modelo.Intangible" %>
<%@ page session="true" %>
<%
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
    
    Intangible intangible = (Intangible) request.getAttribute("intangible");
    List<DetalleAmortizacion> amortizaciones = (List<DetalleAmortizacion>) request.getAttribute("amortizaciones");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Amortizaciones del Intangible</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f4f6f9; }
        table thead { background:#1f2937; color:white; }
        .titulo { font-weight: bold; font-size: 28px; }
    </style>
</head>
<body>
    <!-- NAVBAR -->
    <jsp:include page="/navbar.jsp"/>

    <div class="container mt-5">
        <div class="mb-4">
            <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=listar" 
               class="btn btn-secondary mb-3">← Volver al listado</a>
            
            <h2 class="titulo">Amortizaciones del Intangible</h2>
            
            <% if (intangible != null) { %>
                <div class="card mt-3">
                    <div class="card-body">
                        <h5 class="card-title">Información del Intangible</h5>
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Código:</strong> <%= intangible.getCodigo() %></p>
                                <p><strong>Nombre:</strong> <%= intangible.getNombre_intangible() %></p>
                                <p><strong>Tipo de Licencia:</strong> <%= intangible.getTipoLicencia() %></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Costo:</strong> $ <%= String.format("%.2f", intangible.getCosto()) %></p>
                                <p><strong>Vida Útil:</strong> <%= intangible.getVidaUtil() %></p>
                                <p><strong>Fecha Adquisición:</strong> <%= intangible.getFechaAdquisicion() %></p>
                            </div>
                        </div>
                    </div>
                </div>
            <% } %>

        </div>

        <!-- Tabla de amortizaciones -->
        <div class="table-responsive mt-4">
            <table class="table table-bordered table-hover align-middle">
                <thead class="text-center">
                    <tr>
                        <th>N° Cuota</th>
                        <th>Mes</th>
                        <th>Año</th>
                        <th>Monto</th>
                        <th>Amortización Mensual</th>
                        <th>Amortización Anual</th>
                        <th>Fecha Cuota</th>
                        <th>Fecha Registro</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (amortizaciones != null && !amortizaciones.isEmpty()) {
                            String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                            double totalAmortizado = 0;
                            for (DetalleAmortizacion detalle : amortizaciones) {
                                totalAmortizado += detalle.getMonto();
                    %>
                        <tr>
                            <td class="text-center"><%= detalle.getNumero_cuota() %></td>
                            <td><%= meses[detalle.getMes()] %></td>
                            <td class="text-center"><%= detalle.getAnio() %></td>
                            <td class="text-end">$ <%= String.format("%.2f", detalle.getMonto()) %></td>
                            <td class="text-end">$ <%= String.format("%.2f", detalle.getAmortizacionMensual()) %></td>
                            <td class="text-end">$ <%= String.format("%.2f", detalle.getAmortizacionAnual()) %></td>
                            <td><%= detalle.getFechaCuota() != null ? detalle.getFechaCuota() : "N/A" %></td>
                            <td><%= detalle.getFechaRegistro() != null ? detalle.getFechaRegistro() : "N/A" %></td>
                            <td class="text-center">
                                <span class="text-muted">Solo lectura</span>
                            </td>
                        </tr>
                    <%
                            }
                    %>
                        <tr class="table-info">
                            <td colspan="3" class="text-end"><strong>Total Amortizado:</strong></td>
                            <td class="text-end"><strong>$ <%= String.format("%.2f", totalAmortizado) %></strong></td>
                            <td colspan="5"></td>
                        </tr>
                    <%
                        } else {
                    %>
                        <tr>
                            <td colspan="9" class="text-center text-muted">No hay amortizaciones registradas para este intangible.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

