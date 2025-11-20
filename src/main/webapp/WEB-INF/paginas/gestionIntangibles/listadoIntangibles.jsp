<%-- 
    Document   : listadoIntangibles
    Author     : monic
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Intangible"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listado de intangibles</title>
    
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/listado.css">
</head>
<body>

    <%-- Barra de navegación general --%>
    <jsp:include page="/navbar.jsp" />

    <div class="container">
        <div class="barra-superior">
            <h1>Gestión de intangibles</h1>

            <a class="btn btn-primario"
               href="<%= request.getContextPath() %>/IntangibleControlador?accion=nuevo">
                + Registrar intangible
            </a>
        </div>

        <%
            // Recuperamos la lista enviada por el servlet
            List<Intangible> lista = (List<Intangible>) request.getAttribute("listaIntangibles");
        %>

        <%
            if (lista == null || lista.isEmpty()) {
        %>
            <div class="sin-datos">
                No hay intangibles registrados.
            </div>
        <%
            } else {
        %>

        <%-- Barra de búsqueda (solo se muestra si hay datos) --%>
        <div class="barra-busqueda">
            <input type="text" 
                   id="inputBusqueda" 
                   class="input-busqueda" 
                   placeholder="Buscar por ID, nombre, proveedor, código, estado...">
            <span class="icono-busqueda">🔍</span>
        </div>

        <div class="tabla-contenedor">
            <table id="tablaIntangibles">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Versión</th>
                        <th>Nombre intangible</th>
                        <th>Proveedor</th>
                        <th>Tipo licencia</th>
                        <th>Código</th>
                        <th>Costo</th>
                        <th>Vida útil</th>
                        <th>Estado</th>
                        <th>Fecha adquisición</th>
                        <th>Fecha vencimiento</th>
                        <th>Modalidad amortización</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="tbodyIntangibles">
                <%
                    for (Intangible i : lista) {
                %>
                    <tr>
                        <td><%= i.getIdIntangible() %></td>
                        <td><%= i.getVersion() %></td>
                        <td><%= i.getNombre_intangible() %></td>
                        <td><%= i.getNombre_proveedor() %></td>
                        <td><%= i.getTipoLicencia() %></td>
                        <td><%= i.getCodigo() %></td>
                        <td><%= i.getCosto() %></td>
                        <td><%= i.getVidaUtil() %></td>
                        <td><%= i.getEstado() %></td>
                        <td><%= i.getFechaAdquisicion() %></td>
                        <td><%= i.getFechaVencimiento() %></td>
                        <td><%= i.getModalidad_amortizacion() %></td>
                        <td class="acciones">
                            <a class="btn btn-accion btn-editar"
                               href="<%= request.getContextPath() %>/IntangibleControlador?accion=editar&idintangible=<%= i.getIdIntangible() %>">
                                Editar
                            </a>
                            <a class="btn btn-accion btn-eliminar"
                               href="<%= request.getContextPath() %>/IntangibleControlador?accion=eliminar&idintangible=<%= i.getIdIntangible() %>"
                               onclick="return confirm('¿Seguro que deseas eliminar este intangible?');">
                                Eliminar
                            </a>
                           
                        </td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <%
            } // fin else
        %>

        <%-- Mensaje cuando no hay resultados en la búsqueda --%>
        <div id="sin-resultados" class="sin-datos" style="display: none;">
            No se encontraron resultados para la búsqueda.
        </div>
    </div>

    <script>
        // Función para filtrar la tabla
        function filtrarTabla() {
            const input = document.getElementById('inputBusqueda');
            const filtro = input.value.toLowerCase();
            const tabla = document.getElementById('tablaIntangibles');
            const tbody = document.getElementById('tbodyIntangibles');
            const filas = tbody.getElementsByTagName('tr');
            const sinResultados = document.getElementById('sin-resultados');
            
            let resultadosEncontrados = 0;

            // Recorrer todas las filas de la tabla
            for (let i = 0; i < filas.length; i++) {
                const fila = filas[i];
                const celdas = fila.getElementsByTagName('td');
                let coincide = false;

                // Buscar en todas las celdas excepto la última (Acciones)
                for (let j = 0; j < celdas.length - 1; j++) {
                    const textoCelda = celdas[j].textContent || celdas[j].innerText;
                    if (textoCelda.toLowerCase().indexOf(filtro) > -1) {
                        coincide = true;
                        break;
                    }
                }

                // Mostrar u ocultar la fila según si coincide
                if (coincide) {
                    fila.style.display = '';
                    resultadosEncontrados++;
                } else {
                    fila.style.display = 'none';
                }
            }

            // Mostrar mensaje si no hay resultados
            const tablaContenedor = tabla.closest('.tabla-contenedor');
            if (resultadosEncontrados === 0 && filtro !== '') {
                sinResultados.style.display = 'block';
                if (tablaContenedor) {
                    tablaContenedor.style.display = 'none';
                }
            } else {
                sinResultados.style.display = 'none';
                if (tablaContenedor) {
                    tablaContenedor.style.display = 'block';
                }
            }
        }

        // Agregar evento al campo de búsqueda
        document.addEventListener('DOMContentLoaded', function() {
            const inputBusqueda = document.getElementById('inputBusqueda');
            if (inputBusqueda) {
                inputBusqueda.addEventListener('keyup', filtrarTabla);
                inputBusqueda.addEventListener('input', filtrarTabla);
            }
        });
    </script>
</body>
</html>