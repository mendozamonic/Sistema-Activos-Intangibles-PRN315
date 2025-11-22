<%-- 
    Document   : reporte
    Created on : 20 nov 2025, 19:09:08
    Author     : MINEDUCYT
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Reporte" %>
<%@ page session="true" %>
<%
    String usuario = (String) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reporte de Amortización</title>
    <!-- BOOTSTRAP -->
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
        <h2 class="text-center titulo mb-4">Reporte de Amortización</h2>

        <!-- ================= FORMULARIO ================= -->
        <form class="row g-3 mb-4" method="GET"
              action="${pageContext.request.contextPath}/ReporteControlador">

            <input type="hidden" name="modo" id="modo" value="mes">

            <!-- MES -->
            <div class="col-md-3">
                <label class="form-label">Mes:</label>
                <select name="mes" class="form-select">
                    <%
                        int mesActual = java.time.LocalDate.now().getMonthValue();
                        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                        String mesParam = request.getParameter("mes");
                        int mesSeleccionado = mesParam != null ? Integer.parseInt(mesParam) : mesActual;
                        for(int i = 1; i <= 12; i++) {
                    %>
                        <option value="<%=i%>" <%= (i == mesSeleccionado) ? "selected" : "" %>><%=meses[i]%></option>
                    <% } %>
                </select>
            </div>

            <!-- AÑO -->
            <div class="col-md-3">
                <label class="form-label">Año:</label>
                <select name="anio" class="form-select">
                    <%  
                        int añoNow = java.time.LocalDate.now().getYear();
                        String añoParam = request.getParameter("anio");
                        int añoSeleccionado = añoParam != null ? Integer.parseInt(añoParam) : añoNow;
                        for(int y = añoNow; y >= añoNow - 15; y--) {
                    %>
                        <option value="<%=y%>" <%= (y == añoSeleccionado) ? "selected" : "" %>><%=y%></option>
                    <% } %>
                </select>
            </div>

            <!-- BOTONES -->
            <div class="col-md-6 d-flex align-items-end gap-2">
                <button type="submit" class="btn btn-primary">Generar reporte</button>
                <a href="${pageContext.request.contextPath}/ReporteControlador?formato=excel&mes=<%=mesSeleccionado%>&anio=<%=añoSeleccionado%>" 
                   class="btn btn-success">Exportar Excel</a>
                <a href="${pageContext.request.contextPath}/ReporteControlador?formato=pdf&mes=<%=mesSeleccionado%>&anio=<%=añoSeleccionado%>" 
                   class="btn btn-danger">Exportar PDF</a>
            </div>

        </form>
        <!-- ================================================= -->

        <!-- ============= BUSCADOR GLOBAL ============= -->
        <div class="row mb-3">
            <div class="col-md-4">
                <input type="text" id="buscar" class="form-control" placeholder="Buscar en todas las columnas...">
            </div>
        </div>
        <!-- ============================================= -->

        <!-- ================= TABLA REPORTE ================= -->
        <div class="table-responsive">
            <table id="tablaReporte" class="table table-bordered table-hover align-middle">
                <thead class="text-center">
                    <tr>
                        <th>Código</th>
                        <th>Nombre</th>
                        <th>Tipo</th>
                        <th>Fecha Adq</th>
                        <th>Costo</th>
                        <th>Vida (años)</th>
                        <th>Amort. Anual</th>
                        <th>Amort. Mensual</th>
                        <th>Amort. del Período</th>
                        <th>Amort. Acum.</th>
                        <th>Valor en Libros</th>
                        <th>Cuotas Pend.</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Reporte> reporte = (List<Reporte>) request.getAttribute("reporte");
                    if (reporte != null && !reporte.isEmpty()) {
                        for (Reporte r : reporte) {
                %>
                    <tr>
                        <td><%= r.getCodigo() != null ? r.getCodigo() : "" %></td>
                        <td><%= r.getNombre() != null ? r.getNombre() : "" %></td>
                        <td><%= r.getTipo() != null ? r.getTipo() : "" %></td>
                        <td><%= r.getFecha() != null ? r.getFecha() : "" %></td>
                        <td>$ <%= String.format("%.2f", r.getCosto()) %></td>
                        <td><%= r.getVidaAnios() %></td>
                        <td>$ <%= String.format("%.2f", r.getAmortAnual()) %></td>
                        <td>$ <%= String.format("%.2f", r.getAmortMensual()) %></td>
                        <td>$ <%= String.format("%.2f", r.getAmortPeriodo()) %></td>
                        <td>$ <%= String.format("%.2f", r.getAmortAcum()) %></td>
                        <td>$ <%= String.format("%.2f", r.getValorLibros()) %></td>
                        <td><%= r.getCuotasPend() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="12" class="text-center text-muted">No hay datos para mostrar en el reporte.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
        <!-- ================================================= -->

    </div>

    <!-- ========== BUSCADOR GLOBAL MULTICOLUMNA ========== -->
    <script>
        document.getElementById("buscar").addEventListener("keyup", function () {
            let filtro = this.value.toLowerCase();
            let filas = document.querySelectorAll("#tablaReporte tbody tr");
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
    <!-- ==================================================== -->

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

