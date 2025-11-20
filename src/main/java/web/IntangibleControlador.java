/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web;

import datos.IntangibleDAO;
import modelo.Intangible;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "IntangibleControlador", urlPatterns = {"/IntangibleControlador"})
public class IntangibleControlador extends HttpServlet {

    // =======================  GET  ==========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {

            case "listar":
                listarIntangibles(request, response);
                break;

            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;

            case "editar":
                mostrarFormularioEditar(request, response);
                break;

            case "eliminar":
                confirmarEliminar(request, response);
                break;

            default:
                listarIntangibles(request, response);
                break;
        }
    }

    // =======================  POST  ==========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        switch (accion) {

            case "insertar":
                insertarIntangible(request, response);
                break;

            case "actualizar":
                actualizarIntangible(request, response);
                break;

            case "eliminarDef":
                eliminarIntangible(request, response);
                break;

            default:
                listarIntangibles(request, response);
                break;
        }
    }

    // =======================  MÉTODOS PRIVADOS  ==========================

    /** LISTAR */
    private void listarIntangibles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            IntangibleDAO dao = new IntangibleDAO();
            List<Intangible> lista = dao.listarIntangibles();

            request.setAttribute("listaIntangibles", lista);
            request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/listadoIntangibles.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error listando intangibles: " + ex.getMessage(), ex);
        }
    }

    /** MOSTRAR FORMULARIO NUEVO */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/agregarIntangibles.jsp")
               .forward(request, response);
    }

    /** INSERTAR (desde agregarIntangibles.jsp) */
    private void insertarIntangible(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        // 1. Leer parámetros del formulario
        String id = request.getParameter("idintangible");
        String version = request.getParameter("version");
        String nombre = request.getParameter("nombre_intangible");
        String proveedor = request.getParameter("nombre_proveedor");
        String tipoLicencia = request.getParameter("tipo_licencia_");
        String codigo = request.getParameter("codigo_");
        String vidaUtil = request.getParameter("vida_util");
        String estado = request.getParameter("estado_");
        String fechaAdq = request.getParameter("fecha_adquisicion");
        String fechaVen = request.getParameter("fecha_vencimiento");
        String modalidad = request.getParameter("modalidad_amortizacion");

        double costo = 0;
        String costoParam = request.getParameter("costo");
        if (costoParam != null && !costoParam.isEmpty()) {
            costo = Double.parseDouble(costoParam);
        }

        // 2. Llenar objeto Intangible
        Intangible i = new Intangible();
        i.setIdIntangible(id);
        i.setVersion(version);
        i.setNombre_intangible(nombre);
        i.setNombre_proveedor(proveedor);
        i.setTipoLicencia(tipoLicencia);
        i.setCodigo(codigo);
        i.setCosto(costo);
        i.setVidaUtil(vidaUtil);
        i.setEstado(estado);
        i.setFechaAdquisicion(fechaAdq);
        i.setFechaVencimiento(fechaVen);
        i.setModalidad_amortizacion(modalidad);

        // 3. VALIDAR ANTES DE GUARDAR
        String errores = validarIntangible(i);
        if (errores != null) {
            // reenviar al formulario con mensaje y valores que el usuario ya puso
            request.setAttribute("errorValidacion", errores);
            request.setAttribute("intangible", i);
            request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/agregarIntangibles.jsp")
                   .forward(request, response);
            return; // IMPORTANTÍSIMO: detener aquí, no guardar
        }

        // 4. Si todo está bien, guardar en BD
        IntangibleDAO dao = new IntangibleDAO();
        dao.insertarIntangible(i);

        // 5. Volver al listado
        response.sendRedirect("IntangibleControlador?accion=listar");

    } catch (SQLException ex) {
        throw new ServletException("Error insertando intangible: " + ex.getMessage(), ex);
    }
    }

    /** MOSTRAR FORMULARIO EDITAR (desde listadoIntangibles.jsp) */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String id = request.getParameter("idintangible");

            IntangibleDAO dao = new IntangibleDAO();
            Intangible i = dao.obtenerIntangiblePorId(id);

            request.setAttribute("intangible", i);
            request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/modificarIntangibles.jsp")
                   .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error cargando intangible para edición", ex);
        }
    }

    /** ACTUALIZAR (desde modificarIntangibles.jsp) */
  private void actualizarIntangible(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        // 1. Leer parámetros del formulario
        String id = request.getParameter("idintangible");
        String version = request.getParameter("version");
        String nombre = request.getParameter("nombre_intangible");
        String proveedor = request.getParameter("nombre_proveedor");
        String tipoLicencia = request.getParameter("tipo_licencia_");
        String codigo = request.getParameter("codigo_");
        String vidaUtil = request.getParameter("vida_util");
        String estado = request.getParameter("estado_");
        String fechaAdq = request.getParameter("fecha_adquisicion");
        String fechaVen = request.getParameter("fecha_vencimiento");
        String modalidad = request.getParameter("modalidad_amortizacion");

        double costo = 0;
        String costoParam = request.getParameter("costo");
        if (costoParam != null && !costoParam.isEmpty()) {
            costo = Double.parseDouble(costoParam);
        }

        // 2. Llenar objeto Intangible
        Intangible i = new Intangible();
        i.setIdIntangible(id);
        i.setVersion(version);
        i.setNombre_intangible(nombre);
        i.setNombre_proveedor(proveedor);
        i.setTipoLicencia(tipoLicencia);
        i.setCodigo(codigo);
        i.setCosto(costo);
        i.setVidaUtil(vidaUtil);
        i.setEstado(estado);
        i.setFechaAdquisicion(fechaAdq);
        i.setFechaVencimiento(fechaVen);
        i.setModalidad_amortizacion(modalidad);

        // 3. VALIDA ANTES DE ACTUALIZAR
        String errores = validarIntangible(i);
        if (errores != null) {
            // reenvia al formulario con mensaje y valores
            request.setAttribute("errorValidacion", errores);
            request.setAttribute("intangible", i);
            request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/modificarIntangibles.jsp")
                   .forward(request, response);
            return;   // recordar que esto me permite no pasar si no hic cambios
        }

        // 4. OJO ACA NOS AYUDA ACTUALIZAR EN LA BASEEE
        IntangibleDAO dao = new IntangibleDAO();
        dao.modificarIntangible(i);

      
        response.sendRedirect("IntangibleControlador?accion=listar");

    } catch (SQLException ex) {
        throw new ServletException("Error actualizando intangible: " + ex.getMessage(), ex);
    }
}

    /** MOSTRAR PANTALLA DE CONFIRMACIÓN DE ELIMINAR */
    private void confirmarEliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("idintangible");
        request.setAttribute("idintangible", id);

        request.getRequestDispatcher("/WEB-INF/paginas/gestionIntangibles/eliminarIntangibles.jsp")
               .forward(request, response);
    }

    /** ELIMINAR DEFINITIVAMENTE  */
    private void eliminarIntangible(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String id = request.getParameter("idintangible");

            IntangibleDAO dao = new IntangibleDAO();
            dao.eliminarIntangible(id);

            response.sendRedirect("IntangibleControlador?accion=listar");

        } catch (SQLException ex) {
            throw new ServletException("Error eliminando intangible", ex);
        }
    }
    
private String validarIntangible(Intangible i) {
    StringBuilder errores = new StringBuilder();

    // 1) Costo > 0
    if (i.getCosto() <= 0) {
        errores.append("• El costo debe ser mayor que 0.<br>");
    }

    // 2) Fechas + relación con vida útil (en meses)
    try {
        LocalDate fa = LocalDate.parse(i.getFechaAdquisicion());   
        LocalDate fv = LocalDate.parse(i.getFechaVencimiento());

        // Fecha vencimiento posterior a adquisición
        if (!fv.isAfter(fa)) {
            errores.append("• La fecha de vencimiento debe ser posterior a la fecha de adquisición.<br>");
        } else {
            
            long meses = ChronoUnit.MONTHS.between(fa, fv);

            // Vida útil: número en meses ( "12" o "12 meses")
            int vidaNum = extraerNumero(i.getVidaUtil());

            if (vidaNum > 0) {
                String modalidad = i.getModalidad_amortizacion();

                // En ambos casos comparamos en MESES
                if ("MENSUAL".equalsIgnoreCase(modalidad)) {
                    if (meses != vidaNum) {
                        errores.append("• Con modalidad MENSUAL la vida útil (en meses) debe coincidir con los meses entre la fecha de adquisición y vencimiento (actualmente hay ")
                               .append(meses)
                               .append(" meses).<br>");
                    }
                } else if ("ANUAL".equalsIgnoreCase(modalidad)) {
                    if (meses != vidaNum) {
                        errores.append("• Con modalidad ANUAL la vida útil (en meses) debe coincidir con los meses entre la fecha de adquisición y vencimiento (actualmente hay ")
                               .append(meses)
                               .append(" meses).<br>");
                    }
                }
            }
        }

    } catch (DateTimeParseException e) {
        errores.append("• Formato de fechas inválido. Use el selector de calendario.<br>");
    }

 
    return errores.length() == 0 ? null : errores.toString();
}

    /**
     * Extrae el primer número entero de un texto, por ejemplo "12 meses" -> 12.
     */
    private int extraerNumero(String texto) {
        if (texto == null) {
            return 0;
        }
        Matcher m = Pattern.compile("(\\d+)").matcher(texto);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    @Override
    public String getServletInfo() {
        return "Controlador para la gestión de intangibles";
    }
}
