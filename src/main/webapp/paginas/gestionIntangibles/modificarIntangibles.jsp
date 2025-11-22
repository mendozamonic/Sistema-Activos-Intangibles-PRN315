<%-- 
    Document   : modificarIntangibles
    Created on : 13 nov 2025, 20:02:27
    Author     : monic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Intangible"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar intangible</title>
    
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/formulario.css">
</head>
<body>

    <jsp:include page="/navbar.jsp" />

    <%
        Intangible i = (Intangible) request.getAttribute("intangible");
    %>

    <div class="container">
        <h1>Editar intangible</h1>

        <form action="<%= request.getContextPath() %>/IntangibleControlador" method="post">
            <input type="hidden" name="accion" value="actualizar">
            <!-- enviamos también el ID -->
            <input type="hidden" name="idintangible" value="<%= i.getIdIntangible() %>">

            <!-- ID (solo lectura) -->
            <div>
                <label for="idintangible_mostrar">ID intangible</label>
                <input type="text" id="idintangible_mostrar"
                       value="<%= i.getIdIntangible() %>" disabled>
            </div>

            <!-- Versión -->
            <div>
                <label for="version">Versión</label>
                <input type="text" id="version" name="version"
                       value="<%= i.getVersion() %>" required>
            </div>

            <!-- Nombre intangible -->
            <div>
                <label for="nombre_intangible">Nombre del intangible</label>
                <input type="text" id="nombre_intangible" name="nombre_intangible"
                       value="<%= i.getNombre_intangible() %>" required>
            </div>

            <!-- Proveedor -->
            <div>
                <label for="nombre_proveedor">Proveedor</label>
                <input type="text" id="nombre_proveedor" name="nombre_proveedor"
                       value="<%= i.getNombre_proveedor() %>" required>
            </div>

            <!-- Tipo licencia -->
            <div>
                <label for="tipo_licencia_">Tipo de licencia</label>
                <select id="tipo_licencia_" name="tipo_licencia_">
                    <option value="Anual"   <%= "Anual".equals(i.getTipoLicencia()) ? "selected" : "" %>>Anual</option>
                    <option value="Mensual" <%= "Mensual".equals(i.getTipoLicencia()) ? "selected" : "" %>>Mensual</option>
                    <option value="Perpetua" <%= "Perpetua".equals(i.getTipoLicencia()) ? "selected" : "" %>>Perpetua</option>
                </select>
            </div>

            <!-- Código -->
            <div>
                <label for="codigo_">Código licencia</label>
                <input type="text" id="codigo_" name="codigo_"
                       value="<%= i.getCodigo() %>" required>
            </div>

            <!-- Costo -->
            <div>
                <label for="costo">Costo</label>
                <input type="number" id="costo" name="costo" step="0.01" min="0"
                       value="<%= i.getCosto() %>" required>
            </div>

            <!-- Vida útil -->
            <div>
                <label for="vida_util">Vida útil</label>
                <input type="text" id="vida_util" name="vida_util"
                       value="<%= i.getVidaUtil() %>" required>
            </div>

            <!-- Estado -->
            <div>
                <label for="estado_">Estado</label>
                <select id="estado_" name="estado_">
                    <option value="ACTIVO"   <%= "ACTIVO".equals(i.getEstado()) ? "selected" : "" %>>ACTIVO</option>
                    <option value="INACTIVO" <%= "INACTIVO".equals(i.getEstado()) ? "selected" : "" %>>INACTIVO</option>
                    <option value="VENCIDO"  <%= "VENCIDO".equals(i.getEstado()) ? "selected" : "" %>>VENCIDO</option>
                </select>
            </div>

            <!-- Modalidad amortización -->
            <div>
                <label for="modalidad_amortizacion">Modalidad de amortización</label>
                <select id="modalidad_amortizacion" name="modalidad_amortizacion">
                    <option value="ANUAL"   <%= "ANUAL".equals(i.getModalidad_amortizacion()) ? "selected" : "" %>>ANUAL</option>
                    <option value="MENSUAL" <%= "MENSUAL".equals(i.getModalidad_amortizacion()) ? "selected" : "" %>>MENSUAL</option>
                </select>
            </div>

            <!-- Fecha adquisición -->
            <div>
                <label for="fecha_adquisicion">Fecha de adquisición</label>
                <input type="date" id="fecha_adquisicion" name="fecha_adquisicion"
                       value="<%= i.getFechaAdquisicion() %>" required>
            </div>

            <!-- Fecha vencimiento -->
            <div>
                <label for="fecha_vencimiento">Fecha de vencimiento</label>
                <input type="date" id="fecha_vencimiento" name="fecha_vencimiento"
                       value="<%= i.getFechaVencimiento() %>" required>
            </div>

            <div class="acciones">
                <a class="btn btn-secundario"
                   href="<%= request.getContextPath() %>/IntangibleControlador?accion=listar">
                    Cancelar
                </a>
                <button type="submit" class="btn btn-primario">Guardar cambios</button>
            </div>
        </form>
    </div>
</body>
</html>

