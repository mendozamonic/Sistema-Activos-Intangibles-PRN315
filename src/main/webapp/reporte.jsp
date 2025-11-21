<%-- 
    Document   : reporte
    Created on : 20 nov 2025, 19:09:08
    Author     : MINEDUCYT
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="web.ReporteControlador.ReporteFila" %>

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
    <jsp:include page="navbar.jsp"/>

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
                    <option value="1">Enero</option>
                    <option value="2">Febrero</option>
                    <option value="3">Marzo</option>
                    <option value="4">Abril</option>
                    <option value="5">Mayo</option>
                    <option value="6">Junio</option>
                    <option value="7">Julio</option>
                    <option value="8">Agosto</option>
                    <option value="9">Septiembre</option>
                    <option value="10">Octubre</option>
                    <option value="11">Noviembre</option>
                    <option value="12">Diciembre</option>
                </select>
            </div>

            <!-- AÑO -->
            <div class="col-md-3">
                <label class="form-label">Año:</label>
                <select name="anio" class="form-select">
                    <%  
                        int añoNow = java.time.LocalDate.now().getYear();
                        for(int y = añoNow; y >= añoNow - 15; y--) {
                    %>
                        <option value="<%=y%>"><%=y%></option>
                    <% } %>
                </select>
            </div>

            <!-- BOTÓN -->
            <div class="col-md-3 d-flex align-items-end">
                <button class="btn btn-primary w-100">Generar reporte</button>
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
                        <th>Remanente</th>
                    </tr>
                </thead>

                <tbody>
                <%
                    List<ReporteFila> reporte = (List<ReporteFila>) request.getAttribute("reporte");
                    if (reporte != null) {
                        for (ReporteFila r : reporte) {
                %>
                    <tr>
                        <td><%= r.codigo %></td>
                        <td><%= r.nombre %></td>
                        <td><%= r.tipo %></td>
                        <td><%= r.fecha %></td>
                        <td>$ <%= r.costo %></td>
                        <td><%= r.vidaAnios %></td>
                        <td>$ <%= r.amortAnual %></td>
                        <td>$ <%= r.amortMensual %></td>
                        <td>$ <%= r.amortPeriodo %></td>
                        <td>$ <%= r.amortAcum %></td>
                        <td>$ <%= r.valorLibros %></td>
                        <td><%= r.cuotasPend %></td>
                        <td>$ <%= r.remanente %></td>
                    </tr>
                <%
                        }
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

</body>
</html>
