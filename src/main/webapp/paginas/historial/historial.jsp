<%-- 
    Historial de Amortizaciones - Trazabilidad del proceso de amortización
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="modelo.Historial"%>
<%@page import="datos.HistorialDAO"%>
<%@page session="true"%>
<%
    String usuarioSesion = (String) session.getAttribute("usuario");
    if (usuarioSesion == null) {
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Amortizaciones - Sistema de Gestión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
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
        
        .page-header p {
            margin: 8px 0 0 0;
            opacity: 0.9;
            font-size: 15px;
            font-weight: 400;
        }
        
        .page-content {
            background: #ffffff;
            border-radius: 0 0 12px 12px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 2px 8px rgba(0, 0, 0, 0.06);
            padding: 35px 40px;
            border-top: none;
        }
        
        .navbar { box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        
        .progress-bar-custom {
            height: 20px;
            background-color: #e9ecef;
            border-radius: 4px;
            overflow: hidden;
        }
        
        .progress-fill {
            height: 100%;
            background: #1565c0;
            transition: width 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 11px;
            font-weight: bold;
        }
        
        .table th {
            white-space: nowrap;
            font-size: 0.85rem;
            background: #1565c0;
            color: white;
        }
        
        .table td {
            font-size: 0.85rem;
        }
        
        .summary-card {
            background: #1565c0;
            color: white;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(21, 101, 192, 0.4);
            border: 2px solid rgba(255, 255, 255, 0.2);
        }
        
        .card {
            border: none;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
            border-radius: 12px;
        }
        
        .btn-primary {
            background: #1565c0;
            border: none;
            font-weight: 700;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(21, 101, 192, 0.3);
        }
        
        .btn-primary:hover {
            background: #0d47a1;
            transform: translateY(-3px) scale(1.05);
            box-shadow: 0 8px 20px rgba(21, 101, 192, 0.5);
        }
    </style>
</head>
<body>
    <!-- NAVBAR -->
    <jsp:include page="/navbar.jsp"/>

<div class="container my-4">
    <div class="page-header">
        <h1>
            <i class="bi bi-clock-history"></i>
            Historial de Amortizaciones
        </h1>
        <p>Trazabilidad completa del proceso de amortización de cada intangible</p>
    </div>
    
    <div class="page-content">

    <%
        List<Historial> lista = (List<Historial>) request.getAttribute("lista");
        Integer total = (Integer) request.getAttribute("total");
        Integer pageNum = (Integer) request.getAttribute("page");
        Integer pageSize = (Integer) request.getAttribute("size");
        Integer totalPagesNum = (Integer) request.getAttribute("totalPages");

        String idIntangible = (String) request.getAttribute("idIntangible");
        String nombreIntangible = (String) request.getAttribute("nombreIntangible");

        // Fallback: cargar directamente si no viene del controlador
        if (lista == null) {
            idIntangible = request.getParameter("idIntangible");
            nombreIntangible = request.getParameter("nombreIntangible");
            
            try {
                int p = 1;
                int s = 10;
                try { p = Integer.parseInt(request.getParameter("page")); } catch (Exception ignored) {}
                try { s = Integer.parseInt(request.getParameter("size")); } catch (Exception ignored) {}
                if (p < 1) p = 1;
                if (s < 1) s = 10;
                int offset = (p - 1) * s;

                HistorialDAO.Filtros f = new HistorialDAO.Filtros();
                f.idIntangible = (idIntangible != null ? idIntangible.trim() : null);
                f.nombreIntangible = (nombreIntangible != null ? nombreIntangible.trim() : null);
                f.offset = offset;
                f.limit = s;

                HistorialDAO dao = new HistorialDAO();
                total = dao.contar(f);
                lista = dao.listar(f);
                pageSize = s;
                pageNum = p;
                totalPagesNum = (int) Math.ceil((double) total / pageSize);
            } catch (Exception e) {
                out.println("<div class='alert alert-danger'>Error cargando historial: " + e.getMessage() + "</div>");
            }
        }
        
        // Calcular estadísticas resumidas
        double totalAmortizado = 0;
        double totalCostoOriginal = 0;
        int totalRegistros = 0;
        if (lista != null && !lista.isEmpty()) {
            for (Historial h : lista) {
                totalAmortizado += h.getMonto();
                totalCostoOriginal += h.getCostoOriginal();
                totalRegistros++;
            }
        }
        double promedioMensual = totalRegistros > 0 ? totalAmortizado / totalRegistros : 0;
    %>

    <form class="row g-3 mb-3" method="get" action="${pageContext.request.contextPath}/HistorialControlador?accion=listar">
        <div class="col-md-4">
            <label class="form-label">ID Intangible</label>
            <input type="text" name="idIntangible" value="<%= request.getAttribute("idIntangible") != null ? request.getAttribute("idIntangible") : (request.getParameter("idIntangible") != null ? request.getParameter("idIntangible") : "") %>" class="form-control" placeholder="ID Intangible">
        </div>
        <div class="col-md-4">
            <label class="form-label">Nombre Intangible</label>
            <input type="text" name="nombreIntangible" value="<%= nombreIntangible != null ? nombreIntangible : "" %>" class="form-control" placeholder="Nombre Intangible">
        </div>
        <div class="col-md-4 d-flex align-items-end">
            <button type="submit" class="btn btn-primary me-2"><i class="bi bi-search"></i> Filtrar</button>
            <a href="${pageContext.request.contextPath}/HistorialControlador?accion=listar" class="btn btn-secondary">Limpiar</a>
        </div>
        <input type="hidden" name="page" value="<%= pageNum != null ? pageNum : 1 %>">
        <input type="hidden" name="size" value="<%= pageSize != null ? pageSize : 10 %>">
    </form>


    <div class="card">
        <div class="table-responsive">
            <table class="table table-striped mb-0 table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID Detalle</th>
                        <th>Intangible</th>
                        <th>Código</th>
                        <th>Proveedor</th>
                        <th>Cuota</th>
                        <th>Fecha Cuota</th>
                        <th>Costo Original</th>
                        <th>Vida Útil</th>
                        <th>Monto</th>
                        <th>Amort. Acum.</th>
                        <th>Valor Libros</th>
                        <th>Progreso</th>
                        <th>%</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (lista != null && !lista.isEmpty()) {
                        for (Historial h : lista) {
                            String[] mesesNombres = {"", "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                                                     "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
                            String fechaCuotaFormato = "";
                            if (h.getFechaCuota() != null && !h.getFechaCuota().isEmpty()) {
                                try {
                                    java.time.LocalDate fecha = java.time.LocalDate.parse(h.getFechaCuota());
                                    fechaCuotaFormato = mesesNombres[fecha.getMonthValue()] + " " + fecha.getYear();
                                } catch (Exception e) {
                                    fechaCuotaFormato = h.getFechaCuota();
                                }
                            }
                            
                            // Formatear progreso
                            String progresoTexto = h.getNumeroCuota() + "/" + h.getTotalCuotas();
                            double porcentajeProgreso = h.getPorcentajeCompletado();
                            String colorProgreso = porcentajeProgreso >= 100 ? "bg-success" : 
                                                   porcentajeProgreso >= 75 ? "bg-info" : 
                                                   porcentajeProgreso >= 50 ? "bg-warning" : "bg-danger";
                %>
                    <tr>
                        <td><small class="text-muted"><%= h.getIdDetalle() != null ? h.getIdDetalle() : "" %></small></td>
                        <td><%= h.getNombreIntangible() != null ? h.getNombreIntangible() : "" %></td>
                        <td><strong><%= h.getCodigo() != null ? h.getCodigo() : "" %></strong></td>
                        <td><%= h.getProveedor() != null ? h.getProveedor() : "" %></td>
                        <td><span class="badge bg-info"><%= h.getNumeroCuota() %></span></td>
                        <td><%= fechaCuotaFormato %></td>
                        <td>$<%= String.format(java.util.Locale.US, "%,.2f", h.getCostoOriginal()) %></td>
                        <td><small><%= h.getVidaUtil() != null ? h.getVidaUtil() : "" %></small></td>
                        <td><strong>$<%= String.format(java.util.Locale.US, "%,.2f", h.getMonto()) %></strong></td>
                        <td>$<%= String.format(java.util.Locale.US, "%,.2f", h.getAmortizacionAcumulada()) %></td>
                        <td><strong class="text-success">$<%= String.format(java.util.Locale.US, "%,.2f", h.getValorEnLibros()) %></strong></td>
                        <td>
                            <div class="progress-bar-custom">
                                <div class="progress-fill <%= colorProgreso %>" style="width: <%= Math.min(100, porcentajeProgreso) %>%">
                                    <%= progresoTexto %>
                                </div>
                            </div>
                        </td>
                        <td><span class="badge <%= colorProgreso %>"><%= String.format(java.util.Locale.US, "%.1f", porcentajeProgreso) %>%</span></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="13" class="text-center text-muted">No hay registros de amortizaciones para los filtros aplicados.</td>
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
                    int pageVal = pageNum != null ? pageNum : 1;
                    int sizeVal = pageSize != null ? pageSize : 10;
                    int totalPagesVal = totalPagesNum != null ? totalPagesNum : 1;
                %>
                <span>Mostrando página <%= pageVal %> de <%= totalPagesVal %> — <%= totalVal %> registros</span>
            </div>
            <div class="btn-group">
                <%
                    String idIntangibleParam = (String) request.getAttribute("idIntangible");
                    if (idIntangibleParam == null) {
                        idIntangibleParam = request.getParameter("idIntangible");
                    }
                    String nombreIntangibleParam = (String) request.getAttribute("nombreIntangible");
                    if (nombreIntangibleParam == null) {
                        nombreIntangibleParam = request.getParameter("nombreIntangible");
                    }
                    String base = request.getContextPath() + "/HistorialControlador?accion=listar"
                        + "&idIntangible=" + (idIntangibleParam != null ? java.net.URLEncoder.encode(idIntangibleParam, "UTF-8") : "")
                        + "&nombreIntangible=" + (nombreIntangibleParam != null ? java.net.URLEncoder.encode(nombreIntangibleParam, "UTF-8") : "")
                        + "&size=" + sizeVal;
                    String prevHref = base + "&page=" + Math.max(1, pageVal - 1);
                    String nextHref = base + "&page=" + Math.min(totalPagesVal, pageVal + 1);
                %>
                <a class="btn btn-outline-secondary <%= pageVal <= 1 ? "disabled" : "" %>" href="<%= prevHref %>">Anterior</a>
                <a class="btn btn-outline-secondary <%= pageVal >= totalPagesVal ? "disabled" : "" %>" href="<%= nextHref %>">Siguiente</a>
            </div>
        </div>
    </div>

    <!-- Resumen Estadístico -->
    <% if (lista != null && !lista.isEmpty()) { %>
    <div class="row mt-4">
        <div class="col-md-12">
            <div class="card summary-card">
                <div class="card-body">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <h5>Total Amortizado</h5>
                            <h3>$<%= String.format(java.util.Locale.US, "%,.2f", totalAmortizado) %></h3>
                        </div>
                        <div class="col-md-3">
                            <h5>Costo Original</h5>
                            <h3>$<%= String.format(java.util.Locale.US, "%,.2f", totalCostoOriginal) %></h3>
                        </div>
                        <div class="col-md-3">
                            <h5>Promedio Mensual</h5>
                            <h3>$<%= String.format(java.util.Locale.US, "%,.2f", promedioMensual) %></h3>
                        </div>
                        <div class="col-md-3">
                            <h5>Registros</h5>
                            <h3><%= totalRegistros %></h3>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <% } %>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

