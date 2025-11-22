<%-- 
    Document   : procesamientoAutomatico
    Created on : 20 nov 2025
    Author     : monic
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Intangible" %>
<%@ page session="true" %>
<%
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
    
    List<Intangible> intangiblesPendientes = (List<Intangible>) request.getAttribute("intangiblesPendientes");
    Integer mesSeleccionado = (Integer) request.getAttribute("mesSeleccionado");
    Integer anioSeleccionado = (Integer) request.getAttribute("anioSeleccionado");
    Integer totalPendientes = (Integer) request.getAttribute("totalPendientes");
    String mesNombre = (String) request.getAttribute("mesNombre");
    Integer mesActual = (Integer) request.getAttribute("mesActual");
    Integer anioActual = (Integer) request.getAttribute("anioActual");
    
    if (mesSeleccionado == null) mesSeleccionado = java.time.LocalDate.now().getMonthValue();
    if (anioSeleccionado == null) anioSeleccionado = java.time.LocalDate.now().getYear();
    if (mesActual == null) mesActual = java.time.LocalDate.now().getMonthValue();
    if (anioActual == null) anioActual = java.time.LocalDate.now().getYear();
    if (totalPendientes == null) totalPendientes = 0;
    if (mesNombre == null) {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        mesNombre = meses[mesSeleccionado];
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Procesamiento Automático de Amortizaciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f4f6f9; }
        .card-header { background: #1565c0; color: white; }
        .titulo { font-weight: bold; font-size: 28px; }
    </style>
</head>
<body>
    <!-- NAVBAR -->
    <jsp:include page="/navbar.jsp"/>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card shadow">
                    <div class="card-header">
                        <h3 class="mb-0">⚙️ Procesamiento Automático de Amortizaciones</h3>
                    </div>
                    <div class="card-body">
                        <form method="GET" action="<%= request.getContextPath() %>/AmortizacionControlador" class="mb-4">
                            <input type="hidden" name="accion" value="procesarAutomatico">
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="mes" class="form-label fw-bold">Mes <span class="text-danger">*</span></label>
                                    <select class="form-select form-select-lg" id="mes" name="mes" required onchange="this.form.submit()">
                                        <%
                                            String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                                                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                                            for(int i = 1; i <= 12; i++) {
                                        %>
                                            <option value="<%=i%>" <%= (i == mesSeleccionado.intValue()) ? "selected" : "" %>><%=meses[i]%></option>
                                        <% } %>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label for="anio" class="form-label fw-bold">Año <span class="text-danger">*</span></label>
                                    <select class="form-select form-select-lg" id="anio" name="anio" required onchange="this.form.submit()">
                                        <%
                                            for(int y = anioActual.intValue() + 1; y >= anioActual.intValue() - 5; y--) {
                                        %>
                                            <option value="<%=y%>" <%= (y == anioSeleccionado.intValue()) ? "selected" : "" %>><%=y%></option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                        </form>

                        <div class="alert alert-info">
                            <h5 class="alert-heading">📋 Información del Proceso</h5>
                            <p class="mb-0">
                                El sistema procesará automáticamente las amortizaciones del mes de <strong><%= mesNombre %> <%= anioSeleccionado %></strong> 
                                para todos los intangibles que cumplan las siguientes condiciones:
                            </p>
                            <ul class="mb-0 mt-2">
                                <li>La fecha de adquisición sea anterior o igual al mes seleccionado</li>
                                <li>No exista una amortización previa para ese mes</li>
                                <li>No haya excedido el número total de cuotas de amortización</li>
                            </ul>
                        </div>

                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="card border-primary">
                                    <div class="card-body text-center">
                                        <h2 class="text-primary"><%= totalPendientes %></h2>
                                        <p class="mb-0">Intangibles pendientes de amortización</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="card border-success">
                                    <div class="card-body text-center">
                                        <h2 class="text-success"><%= mesNombre %> <%= anioSeleccionado %></h2>
                                        <p class="mb-0">Mes a procesar</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <% if (intangiblesPendientes != null && !intangiblesPendientes.isEmpty()) { %>
                            <h5 class="mb-3">📝 Intangibles que se procesarán:</h5>
                            <div class="table-responsive mb-4" style="max-height: 400px; overflow-y: auto;">
                                <table class="table table-striped table-hover">
                                    <thead class="table-dark sticky-top">
                                        <tr>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Costo</th>
                                            <th>Fecha Adquisición</th>
                                            <th>Vida Útil</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Intangible i : intangiblesPendientes) { %>
                                            <tr>
                                                <td><%= i.getCodigo() != null ? i.getCodigo() : "N/A" %></td>
                                                <td><%= i.getNombre_intangible() != null ? i.getNombre_intangible() : "N/A" %></td>
                                                <td>$ <%= String.format("%.2f", i.getCosto()) %></td>
                                                <td><%= i.getFechaAdquisicion() != null ? i.getFechaAdquisicion() : "N/A" %></td>
                                                <td><%= i.getVidaUtil() != null ? i.getVidaUtil() : "N/A" %></td>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>

                            <form method="POST" action="<%= request.getContextPath() %>/AmortizacionControlador" 
                                  onsubmit="return confirm('¿Está seguro de que desea procesar las amortizaciones para <%= mesNombre %> <%= anioSeleccionado %>?');">
                                <input type="hidden" name="accion" value="procesarAutomatico">
                                <input type="hidden" name="mes" value="<%= mesSeleccionado %>">
                                <input type="hidden" name="anio" value="<%= anioSeleccionado %>">
                                
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success btn-lg">
                                        ✅ Procesar Amortizaciones Automáticamente
                                    </button>
                                    <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=listar" 
                                       class="btn btn-secondary">
                                        Cancelar
                                    </a>
                                </div>
                            </form>
                        <% } else { %>
                            <div class="alert alert-success">
                                <h5 class="alert-heading">✅ No hay amortizaciones pendientes</h5>
                                <p class="mb-0">
                                    Todos los intangibles ya tienen registrada su amortización para el mes de 
                                    <strong><%= mesNombre %> <%= anioSeleccionado %></strong>, o no aplican para este período.
                                </p>
                            </div>
                            <div class="d-grid gap-2">
                                <a href="<%= request.getContextPath() %>/AmortizacionControlador?accion=listar" 
                                   class="btn btn-primary">
                                    Ver Amortizaciones Registradas
                                </a>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

