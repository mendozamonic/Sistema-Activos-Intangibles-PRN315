<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar intangible</title>
    
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/formulario.css">
</head>
<body>

    <jsp:include page="/navbar.jsp" />

    <div class="container">
        <h1>Registrar nuevo intangible</h1>
        <%
    String errorValidacion = (String) request.getAttribute("errorValidacion");
%>

<% if (errorValidacion != null) { %>
    <div class="error-mensaje">
        <strong>Hay errores en los datos ingresados:</strong><br>
        <%= errorValidacion %>
    </div>
<% } %>

        <form action="<%= request.getContextPath() %>/IntangibleControlador" method="post">
            <!-- IMPORTANTE: esta accion la usa el servlet -->
            <input type="hidden" name="accion" value="insertar">

            <!-- ID -->
            <div>
                <label for="idintangible">ID intangible</label>
                <input type="text" id="idintangible" name="idintangible" required>
            </div>

            <!-- Versión -->
            <div>
                <label for="version">Versión</label>
                <input type="text" id="version" name="version" required>
            </div>

            <!-- Nombre intangible -->
            <div>
                <label for="nombre_intangible">Nombre del intangible</label>
                <input type="text" id="nombre_intangible" name="nombre_intangible" required>
            </div>

            <!-- Proveedor -->
            <div>
                <label for="nombre_proveedor">Proveedor</label>
                <input type="text" id="nombre_proveedor" name="nombre_proveedor" required>
            </div>

            <!-- Tipo licencia -->
            <div>
                <label for="tipo_licencia_">Tipo de licencia</label>
                <select id="tipo_licencia_" name="tipo_licencia_">
                    <option value="Anual">Anual</option>
                    <option value="Mensual">Mensual</option>
                    <option value="Perpetua">Perpetua</option>
                </select>
            </div>

            <!-- Código -->
            <div>
                <label for="codigo_">Código licencia</label>
                <input type="text" id="codigo_" name="codigo_" required>
            </div>

            <!-- Costo -->
            <div>
                <label for="costo">Costo</label>
                <input type="number" id="costo" name="costo" step="0.01" min="0" required>
            </div>

            <!-- Vida útil -->
            <div>
                <label for="vida_util">Vida útil</label>
                <input type="text" id="vida_util" name="vida_util" placeholder="Ej: 12 meses" required>
            </div>

            <!-- Estado -->
            <div>
                <label for="estado_">Estado</label>
                <select id="estado_" name="estado_">
                    <option value="ACTIVO">ACTIVO</option>
                    <option value="INACTIVO">INACTIVO</option>
                    <option value="VENCIDO">VENCIDO</option>
                </select>
            </div>

            <!-- Modalidad amortización -->
            <div>
                <label for="modalidad_amortizacion">Modalidad de amortización</label>
                <select id="modalidad_amortizacion" name="modalidad_amortizacion">
                    <option value="ANUAL">ANUAL</option>
                    <option value="MENSUAL">MENSUAL</option>
                </select>
            </div>

            <!-- Fecha adquisición -->
            <div>
                <label for="fecha_adquisicion">Fecha de adquisición</label>
                <input type="date" id="fecha_adquisicion" name="fecha_adquisicion" required>
            </div>

            <!-- Fecha vencimiento -->
            <div>
                <label for="fecha_vencimiento">Fecha de vencimiento</label>
                <input type="date" id="fecha_vencimiento" name="fecha_vencimiento" required>
            </div>

            <div class="acciones">
                <a class="btn btn-secundario"
                   href="<%= request.getContextPath() %>/IntangibleControlador?accion=listar">
                    Cancelar
                </a>
                <button type="submit" class="btn btn-primario">Guardar</button>
            </div>
        </form>
    </div>
</body>
</html>

