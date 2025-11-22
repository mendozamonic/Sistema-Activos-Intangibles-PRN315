<%-- 
    Document   : editarAmortizacion
    Created on : 20 nov 2025
    Author     : monic
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Intangible" %>
<%@ page import="modelo.DetalleAmortizacion" %>
<%@ page session="true" %>
<%
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
    
    String error = (String) request.getAttribute("error");
    DetalleAmortizacion detalle = (DetalleAmortizacion) request.getAttribute("detalle");
    Intangible intangible = (Intangible) request.getAttribute("intangible");
    List<Intangible> intangibles = (List<Intangible>) request.getAttribute("intangibles");
    
    if (detalle == null) {
        response.sendRedirect(request.getContextPath() + "/AmortizacionControlador?accion=listar");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Amortización</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/formulario.css">
</head>
<body>
    <!-- NAVBAR -->
    <jsp:include page="/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <div class="card-header bg-warning text-dark">
                        <h3 class="mb-0">Editar Amortización</h3>
                    </div>
                    <div class="card-body">
                        <% if (error != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= error %>
                            </div>
                        <% } %>

                        <form method="POST" action="<%= request.getContextPath() %>/AmortizacionControlador">
                            <input type="hidden" name="accion" value="actualizar">
                            <input type="hidden" name="iddetalle" value="<%= detalle.getIdDetalle() %>">

                            <!-- Intangible -->
                            <div class="mb-3">
                                <label for="idintangible" class="form-label">Intangible <span class="text-danger">*</span></label>
                                <select class="form-select" id="idintangible" name="idintangible" required>
                                    <% if (intangibles != null) {
                                        for (Intangible i : intangibles) {
                                    %>
                                        <option value="<%= i.getIdIntangible() %>" 
                                                <%= i.getIdIntangible().equals(detalle.getIdIntangible()) ? "selected" : "" %>>
                                            <%= i.getCodigo() %> - <%= i.getNombre_intangible() %> 
                                            (Costo: $<%= String.format("%.2f", i.getCosto()) %>)
                                        </option>
                                    <% 
                                        }
                                    } %>
                                </select>
                            </div>

                            <!-- Mes -->
                            <div class="mb-3">
                                <label for="mes" class="form-label">Mes <span class="text-danger">*</span></label>
                                <select class="form-select" id="mes" name="mes" required>
                                    <%
                                        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                                        for(int i = 1; i <= 12; i++) {
                                    %>
                                        <option value="<%=i%>" <%= (i == detalle.getMes()) ? "selected" : "" %>><%=meses[i]%></option>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Año -->
                            <div class="mb-3">
                                <label for="anio" class="form-label">Año <span class="text-danger">*</span></label>
                                <select class="form-select" id="anio" name="anio" required>
                                    <%
                                        int añoNow = java.time.LocalDate.now().getYear();
                                        int añoInicio = añoNow - 5;
                                        int añoFin = añoNow + 1;
                                        for(int y = añoFin; y >= añoInicio; y--) {
                                    %>
                                        <option value="<%=y%>" <%= (y == detalle.getAnio()) ? "selected" : "" %>><%=y%></option>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Monto -->
                            <div class="mb-3">
                                <label for="monto" class="form-label">Monto <span class="text-danger">*</span></label>
                                <input type="number" step="0.01" class="form-control" id="monto" name="monto" 
                                       value="<%= String.format("%.2f", detalle.getMonto()) %>" required>
                                <small class="form-text text-muted">Monto de la amortización mensual</small>
                            </div>

                            <!-- Información adicional -->
                            <div class="alert alert-info">
                                <strong>Información actual:</strong><br>
                                N° Cuota: <strong><%= detalle.getNumero_cuota() %></strong><br>
                                Amortización Mensual: <strong>$ <%= String.format("%.2f", detalle.getAmortizacionMensual()) %></strong><br>
                                Amortización Anual: <strong>$ <%= String.format("%.2f", detalle.getAmortizacionAnual()) %></strong><br>
                                Fecha Registro Original: <strong><%= detalle.getFechaRegistro() != null ? detalle.getFechaRegistro() : "N/A" %></strong>
                            </div>

                            <!-- Botones -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=listar" 
                                   class="btn btn-secondary">Cancelar</a>
                                <button type="submit" class="btn btn-warning">Actualizar Amortización</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

