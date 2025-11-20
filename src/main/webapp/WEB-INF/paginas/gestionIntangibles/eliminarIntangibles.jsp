<%-- 
    Document   : eliminarIntangibles
    Created on : 13 nov 2025, 20:02:39
    Author     : monic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Eliminar intangible</title>
    
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/eliminar.css">
</head>
<body>

    <jsp:include page="/navbar.jsp" />

    <div class="container">
        <h1>Eliminar intangible</h1>

        <%
            String id = (String) request.getAttribute("idintangible");
        %>

        <p>
            ¿Está seguro que desea eliminar el intangible con ID:
            <span class="id-box"><%= id %></span> ?
        </p>

        <p>Esta acción no se puede deshacer.</p>

        <div class="acciones">
            <a class="btn btn-cancelar"
               href="<%= request.getContextPath() %>/IntangibleControlador?accion=listar">
                Cancelar
            </a>

            <form action="<%= request.getContextPath() %>/IntangibleControlador" method="post" style="margin:0;">
                <input type="hidden" name="accion" value="eliminarDef">
                <input type="hidden" name="idintangible" value="<%= id %>">
                <button type="submit" class="btn btn-eliminar">Eliminar definitivamente</button>
            </form>
        </div>
    </div>
</body>
</html>