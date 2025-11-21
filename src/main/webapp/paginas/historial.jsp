<%-- 
    Historial de Compras - Renderiza datos desde controlador si está presente,
    si no, carga directamente vía DAO con los parámetros de la URL.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="modelo.Historial"%>
<%@page import="datos.HistorialDAO"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Historial de Compras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container my-4">
    <h2 class="mb-4">Historial de Compras</h2>

    <%
        List<Historial> lista = (List<Historial>) request.getAttribute("lista");
        Integer total = (Integer) request.getAttribute("total");
        Integer page = (Integer) request.getAttribute("page");
        Integer size = (Integer) request.getAttribute("size");
        Integer totalPages = (Integer) request.getAttribute("totalPages");

        String usuario = (String) request.getAttribute("usuario");
        String proveedor = (String) request.getAttribute("proveedor");
        String codigo = (String) request.getAttribute("codigo");
        String desde = (String) request.getAttribute("desde");
        String hasta = (String) request.getAttribute("hasta");

        // Fallback: cargar directamente si no viene del controlador
        if (lista == null) {
            usuario = request.getParameter("usuario");
            proveedor = request.getParameter("proveedor");
            codigo = request.getParameter("codigo");
            desde = request.getParameter("desde");
            hasta = request.getParameter("hasta");
            try {
                int p = 1;
                int s = 10;
                try { p = Integer.parseInt(request.getParameter("page")); } catch (Exception ignored) {}
                try { s = Integer.parseInt(request.getParameter("size")); } catch (Exception ignored) {}
                if (p < 1) p = 1;
                if (s < 1) s = 10;
                int offset = (p - 1) * s;

                HistorialDAO.Filtros f = new HistorialDAO.Filtros();
                f.usuario = (usuario != null ? usuario.trim() : null);
                f.proveedor = (proveedor != null ? proveedor.trim() : null);
                f.codigo = (codigo != null ? codigo.trim() : null);
                f.fechaDesde = (desde != null ? desde.trim() : null);
                f.fechaHasta = (hasta != null ? hasta.trim() : null);
                f.offset = offset;
                f.limit = s;

                HistorialDAO dao = new HistorialDAO();
                total = dao.contar(f);
                lista = dao.listar(f);
                size = s;
                page = p;
                totalPages = (int) Math.ceil((double) total / size);
            } catch (Exception e) {
                out.println("<div class='alert alert-danger'>Error cargando historial: " + e.getMessage() + "</div>");
            }
        }
    %>

    <form class="row g-3 mb-3" method="get" action="${pageContext.request.contextPath}/paginas/historial.jsp">
        <div class="col-md-2">
            <label class="form-label">Usuario</label>
            <input type="text" name="usuario" value="<%= usuario != null ? usuario : "" %>" class="form-control">
        </div>
        <div class="col-md-2">
            <label class="form-label">Proveedor</label>
            <input type="text" name="proveedor" value="<%= proveedor != null ? proveedor : "" %>" class="form-control">
        </div>
        <div class="col-md-2">
            <label class="form-label">Código</label>
            <input type="text" name="codigo" value="<%= codigo != null ? codigo : "" %>" class="form-control">
        </div>
        <div class="col-md-2">
            <label class="form-label">Desde</label>
            <input type="date" name="desde" value="<%= desde != null ? desde : "" %>" class="form-control">
        </div>
        <div class="col-md-2">
            <label class="form-label">Hasta</label>
            <input type="date" name="hasta" value="<%= hasta != null ? hasta : "" %>" class="form-control">
        </div>
        <div class="col-md-2 align-self-end">
            <button type="submit" class="btn btn-primary w-100">Filtrar</button>
        </div>
        <input type="hidden" name="page" value="<%= page != null ? page : 1 %>">
        <input type="hidden" name="size" value="<%= size != null ? size : 10 %>">
    </form>

    <div class="card">
        <div class="table-responsive">
            <table class="table table-striped mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Fecha Compra</th>
                        <th>Usuario</th>
                        <th>Intangible (Código)</th>
                        <th>Proveedor</th>
                        <th>Monto</th>
                        <th>Vence</th>
                        <th>ID Compra</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (lista != null && !lista.isEmpty()) {
                        for (Historial h : lista) {
                %>
                    <tr>
                        <td><%= h.getFechaCompra() != null ? h.getFechaCompra() : "" %></td>
                        <td><%= h.getNombreUsuario() != null ? h.getNombreUsuario() : h.getIdUsuario() %></td>
                        <td><%= (h.getCodigo() != null ? h.getCodigo() : "") %></td>
                        <td><%= h.getProveedor() != null ? h.getProveedor() : "" %></td>
                        <td><%= String.format(java.util.Locale.US, "%.2f", h.getMonto()) %></td>
                        <td><%= h.getFechaVencimiento() != null ? h.getFechaVencimiento() : "" %></td>
                        <td><%= h.getIdCompra() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="7" class="text-center text-muted">No hay registros para los filtros aplicados.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
        <div class="card-footer d-flex justify-content-between align-items-center">
            <div>
                <%
                    int totalVal = total != null ? total : 0;
                    int pageVal = page != null ? page : 1;
                    int sizeVal = size != null ? size : 10;
                    int totalPagesVal = totalPages != null ? totalPages : 1;
                %>
                <span>Mostrando página <%= pageVal %> de <%= totalPagesVal %> — <%= totalVal %> registros</span>
            </div>
            <div class="btn-group">
                <%
                    String base = request.getContextPath() + "/paginas/historial.jsp"
                        + "?usuario=" + (usuario != null ? usuario : "")
                        + "&proveedor=" + (proveedor != null ? proveedor : "")
                        + "&codigo=" + (codigo != null ? codigo : "")
                        + "&desde=" + (desde != null ? desde : "")
                        + "&hasta=" + (hasta != null ? hasta : "")
                        + "&size=" + sizeVal;
                    String prevHref = base + "&page=" + Math.max(1, pageVal - 1);
                    String nextHref = base + "&page=" + Math.min(totalPagesVal, pageVal + 1);
                %>
                <a class="btn btn-outline-secondary <%= pageVal <= 1 ? "disabled" : "" %>" href="<%= prevHref %>">Anterior</a>
                <a class="btn btn-outline-secondary <%= pageVal >= totalPagesVal ? "disabled" : "" %>" href="<%= nextHref %>">Siguiente</a>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


