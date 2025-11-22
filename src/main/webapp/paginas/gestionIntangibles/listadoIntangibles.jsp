<%-- 
    Document   : listadoIntangibles
    Author     : monic
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Intangible"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Activos Intangibles - Sistema de Gestión</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/listado.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
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
        
        .page-content {
            background: #ffffff;
            border-radius: 0 0 12px 12px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 2px 8px rgba(0, 0, 0, 0.06);
            padding: 35px 40px;
            border-top: none;
        }
        
        .barra-superior {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 25px;
            border-bottom: 2px solid #e9ecef;
        }
        
        .barra-superior h1 {
            font-size: 24px;
            font-weight: 700;
            color: #1e3a5f;
            margin: 0;
            letter-spacing: -0.3px;
        }
        
        .btn-primario {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            font-weight: 600;
            font-size: 14px;
            border-radius: 8px;
            text-decoration: none;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.2);
        }
        
        .btn-primario:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
        }
        
        .sin-datos {
            padding: 40px;
            font-style: italic;
            color: #6c757d;
            text-align: center;
            font-size: 16px;
            background-color: #f8f9fa;
            border-radius: 10px;
            margin: 20px 0;
            border: 1px solid #e9ecef;
        }
    </style>
</head>
<body>

    <%-- Barra de navegación general --%>
    <jsp:include page="/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <h1>
                <i class="bi bi-file-earmark-text"></i>
                Gestión de Activos Intangibles
            </h1>
        </div>
        
        <div class="page-content">
            <div class="barra-superior">
                <h1>Registro de Activos Intangibles</h1>

                <a class="btn btn-primario"
                   href="<%= request.getContextPath() %>/IntangibleControlador?accion=nuevo">
                    <i class="bi bi-plus-circle"></i> Registrar Nuevo Intangible
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
                               onclick="return confirm('¿Está seguro de que desea eliminar este intangible?');">
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

