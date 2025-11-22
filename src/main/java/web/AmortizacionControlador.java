/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import datos.DetalleAmortizacionDAO;
import datos.IntangibleDAO;
import datos.HistorialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.DetalleAmortizacion;
import modelo.Intangible;
import modelo.Historial;

@WebServlet(name = "AmortizacionControlador", urlPatterns = {"/AmortizacionControlador"})
public class AmortizacionControlador extends HttpServlet {

    private static final RoundingMode RM = RoundingMode.HALF_UP;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarAmortizaciones(request, response);
                break;
            case "procesarAutomatico":
                mostrarProcesamientoAutomatico(request, response);
                break;
            case "verPorIntangible":
                verAmortizacionesPorIntangible(request, response);
                break;
            case "editar":
                // Funcionalidad deshabilitada: las amortizaciones son de solo lectura
                response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                    java.net.URLEncoder.encode("La edición de amortizaciones no está permitida para mantener la integridad contable.", "UTF-8"));
                break;
            case "eliminar":
                // Funcionalidad deshabilitada: las amortizaciones son de solo lectura
                response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                    java.net.URLEncoder.encode("La eliminación de amortizaciones no está permitida para mantener la integridad contable.", "UTF-8"));
                break;
            default:
                listarAmortizaciones(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        switch (accion) {
            case "procesarAutomatico":
                procesarAmortizacionesAutomaticas(request, response);
                break;
            case "actualizar":
                // Funcionalidad deshabilitada: las amortizaciones son de solo lectura
                response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                    java.net.URLEncoder.encode("La actualización de amortizaciones no está permitida para mantener la integridad contable.", "UTF-8"));
                break;
            default:
                listarAmortizaciones(request, response);
                break;
        }
    }

    /**
     * Lista todas las amortizaciones registradas
     */
    private void listarAmortizaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            List<DetalleAmortizacion> lista = dao.listarTodas();

            request.setAttribute("listaAmortizaciones", lista);
            request.getRequestDispatcher("/paginas/amortizaciones/listadoAmortizaciones.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error listando amortizaciones: " + ex.getMessage(), ex);
        }
    }

    /**
     * Muestra la página de procesamiento automático de amortizaciones
     */
    private void mostrarProcesamientoAutomatico(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener mes y año de los parámetros, o usar el actual por defecto
            String mesParam = request.getParameter("mes");
            String anioParam = request.getParameter("anio");
            
            LocalDate ahora = LocalDate.now();
            int mesSeleccionado = mesParam != null ? parseInt(mesParam, ahora.getMonthValue()) : ahora.getMonthValue();
            int anioSeleccionado = anioParam != null ? parseInt(anioParam, ahora.getYear()) : ahora.getYear();

            IntangibleDAO intangibleDAO = new IntangibleDAO();
            DetalleAmortizacionDAO amortizacionDAO = new DetalleAmortizacionDAO();
            List<Intangible> intangibles = intangibleDAO.listarIntangibles();

            // Calcular qué amortizaciones se pueden registrar para el mes/año seleccionado
            List<Intangible> intangiblesPendientes = new ArrayList<>();
            int totalPendientes = 0;

            for (Intangible intangible : intangibles) {
                if (intangible.getFechaAdquisicion() != null && !intangible.getFechaAdquisicion().isEmpty()) {
                    try {
                        LocalDate fechaAdq = LocalDate.parse(intangible.getFechaAdquisicion());
                        YearMonth mesAdq = YearMonth.from(fechaAdq);
                        YearMonth mesSeleccionadoYM = YearMonth.of(anioSeleccionado, mesSeleccionado);

                        // Verificar que el mes seleccionado sea válido (después o igual a la fecha de adquisición)
                        if (!mesSeleccionadoYM.isBefore(mesAdq)) {
                            // Verificar que no exista ya una amortización para este mes
                            if (!amortizacionDAO.existeAmortizacion(intangible.getIdIntangible(), mesSeleccionado, anioSeleccionado)) {
                                // Verificar que no haya excedido las cuotas
                                int cuotasRegistradas = amortizacionDAO.contarCuotasAmortizadas(intangible.getIdIntangible());
                                int vidaAnios = parseVidaAnios(intangible.getVidaUtil());
                                int totalMeses = vidaAnios * 12;

                                if (cuotasRegistradas < totalMeses) {
                                    intangiblesPendientes.add(intangible);
                                    totalPendientes++;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Ignorar intangibles con fechas inválidas
                    }
                }
            }

            request.setAttribute("intangiblesPendientes", intangiblesPendientes);
            request.setAttribute("mesSeleccionado", mesSeleccionado);
            request.setAttribute("anioSeleccionado", anioSeleccionado);
            request.setAttribute("totalPendientes", totalPendientes);

            String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            request.setAttribute("mesNombre", meses[mesSeleccionado]);
            request.setAttribute("mesActual", ahora.getMonthValue());
            request.setAttribute("anioActual", ahora.getYear());

            request.getRequestDispatcher("/paginas/amortizaciones/procesamientoAutomatico.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error preparando procesamiento automático: " + ex.getMessage(), ex);
        }
    }

    /**
     * Procesa automáticamente las amortizaciones del mes actual
     */
    private void procesarAmortizacionesAutomaticas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String mesStr = request.getParameter("mes");
            String anioStr = request.getParameter("anio");

            int mes = parseInt(mesStr, LocalDate.now().getMonthValue());
            int anio = parseInt(anioStr, LocalDate.now().getYear());

            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            IntangibleDAO intangibleDAO = new IntangibleDAO();
            List<Intangible> intangibles = intangibleDAO.listarIntangibles();

            int registradas = 0;
            int omitidas = 0;
            List<String> errores = new ArrayList<>();

            for (Intangible intangible : intangibles) {
                try {
                    // Validar fecha de adquisición
                    LocalDate fechaAdq = null;
                    if (intangible.getFechaAdquisicion() != null && !intangible.getFechaAdquisicion().isEmpty()) {
                        fechaAdq = LocalDate.parse(intangible.getFechaAdquisicion());
                    }

                    if (fechaAdq == null) {
                        omitidas++;
                        continue;
                    }

                    YearMonth mesAdq = YearMonth.from(fechaAdq);
                    YearMonth mesAmortizacion = YearMonth.of(anio, mes);

                    // Validar que el mes sea válido
                    if (mesAmortizacion.isBefore(mesAdq)) {
                        omitidas++;
                        continue;
                    }

                    // Validar que no exista ya una amortización
                    if (dao.existeAmortizacion(intangible.getIdIntangible(), mes, anio)) {
                        omitidas++;
                        continue;
                    }

                    // Calcular valores
                    BigDecimal costo = BigDecimal.valueOf(intangible.getCosto());
                    int vidaAnios = parseVidaAnios(intangible.getVidaUtil());
                    int totalMeses = vidaAnios * 12;

                    // Validar cuotas
                    int cuotasRegistradas = dao.contarCuotasAmortizadas(intangible.getIdIntangible());
                    if (cuotasRegistradas >= totalMeses) {
                        omitidas++;
                        continue;
                    }

                    BigDecimal amortMensual = costo.divide(BigDecimal.valueOf(totalMeses), 2, RM);
                    BigDecimal amortAnual = costo.divide(BigDecimal.valueOf(vidaAnios), 2, RM);

                    // Registrar amortización
                    int numeroCuota = dao.obtenerSiguienteNumeroCuota(intangible.getIdIntangible());
                    LocalDate fechaCuota = LocalDate.of(anio, mes, 1);

                    DetalleAmortizacion detalle = new DetalleAmortizacion();
                    detalle.setIdDetalle(dao.generarIdDetalle());
                    detalle.setIdIntangible(intangible.getIdIntangible());
                    detalle.setNumero_cuota(numeroCuota);
                    detalle.setMonto(amortMensual.doubleValue());
                    detalle.setAmortizacionMensual(amortMensual.doubleValue());
                    detalle.setAmortizacionAnual(amortAnual.doubleValue());
                    detalle.setFechaCuota(fechaCuota.toString());
                    detalle.setFechaRegistro(fechaCuota.toString());
                    detalle.setMes(mes);
                    detalle.setAnio(anio);

                    if (dao.insertarAmortizacion(detalle)) {
                        // Guardar en historial usando los mismos cálculos
                        try {
                            HistorialDAO historialDAO = new HistorialDAO();
                            Historial historial = historialDAO.calcularValoresHistorial(detalle.getIdDetalle(), detalle.getIdIntangible());
                            if (historial != null) {
                                historialDAO.guardarHistorial(historial);
                            }
                        } catch (Exception e) {
                            // Si falla guardar historial, no afecta el registro de amortización
                            System.err.println("Error guardando historial: " + e.getMessage());
                        }
                        registradas++;
                    } else {
                        omitidas++;
                    }

                } catch (Exception e) {
                    omitidas++;
                    errores.add(intangible.getNombre_intangible() + ": " + e.getMessage());
                }
            }

            String mensaje = String.format(
                "Procesamiento automático completado: %d amortización(es) registrada(s) para %s %d, %d omitida(s) (ya existían o no aplicaban).",
                registradas, getMesNombre(mes), anio, omitidas);

            if (!errores.isEmpty()) {
                mensaje += " Error(es): " + String.join(", ", errores.subList(0, Math.min(3, errores.size())));
            }

            response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                                java.net.URLEncoder.encode(mensaje, "UTF-8"));

        } catch (SQLException ex) {
            throw new ServletException("Error en procesamiento automático: " + ex.getMessage(), ex);
        }
    }

    private String getMesNombre(int mes) {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];
    }


    /**
     * Muestra las amortizaciones de un intangible específico
     */
    private void verAmortizacionesPorIntangible(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idIntangible = request.getParameter("idintangible");

            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            IntangibleDAO intangibleDAO = new IntangibleDAO();

            Intangible intangible = intangibleDAO.obtenerIntangiblePorId(idIntangible);
            List<DetalleAmortizacion> amortizaciones = dao.listarPorIntangible(idIntangible);

            request.setAttribute("intangible", intangible);
            request.setAttribute("amortizaciones", amortizaciones);
            request.getRequestDispatcher("/paginas/amortizaciones/amortizacionesPorIntangible.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error obteniendo amortizaciones: " + ex.getMessage(), ex);
        }
    }

    /**
     * Muestra el formulario para editar una amortización
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idDetalle = request.getParameter("iddetalle");

            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            DetalleAmortizacion detalle = dao.obtenerPorId(idDetalle);

            if (detalle == null) {
                request.setAttribute("error", "La amortización no fue encontrada.");
                listarAmortizaciones(request, response);
                return;
            }

            IntangibleDAO intangibleDAO = new IntangibleDAO();
            Intangible intangible = intangibleDAO.obtenerIntangiblePorId(detalle.getIdIntangible());
            List<Intangible> intangibles = intangibleDAO.listarIntangibles();

            request.setAttribute("detalle", detalle);
            request.setAttribute("intangible", intangible);
            request.setAttribute("intangibles", intangibles);
            request.getRequestDispatcher("/paginas/amortizaciones/editarAmortizacion.jsp")
                    .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Error cargando amortización para edición: " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza una amortización existente
     */
    private void actualizarAmortizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idDetalle = request.getParameter("iddetalle");
            String idIntangible = request.getParameter("idintangible");
            String mesStr = request.getParameter("mes");
            String anioStr = request.getParameter("anio");
            String montoStr = request.getParameter("monto");

            if (idDetalle == null || idDetalle.trim().isEmpty()) {
                request.setAttribute("error", "El ID de amortización no es válido.");
                mostrarFormularioEditar(request, response);
                return;
            }

            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            DetalleAmortizacion detalleExistente = dao.obtenerPorId(idDetalle);

            if (detalleExistente == null) {
                request.setAttribute("error", "La amortización no fue encontrada.");
                listarAmortizaciones(request, response);
                return;
            }

            int mes = parseInt(mesStr, detalleExistente.getMes());
            int anio = parseInt(anioStr, detalleExistente.getAnio());
            double monto = parseDouble(montoStr, detalleExistente.getMonto());

            // Validar que no exista otra amortización para el mismo mes/año (excluyendo la actual)
            if (dao.existeAmortizacionExcluyendo(idIntangible, mes, anio, idDetalle)) {
                request.setAttribute("error", "Ya existe otra amortización registrada para este intangible en el mes " + mes + "/" + anio + ".");
                mostrarFormularioEditar(request, response);
                return;
            }

            // Obtener información del intangible para recalcular si es necesario
            IntangibleDAO intangibleDAO = new IntangibleDAO();
            Intangible intangible = intangibleDAO.obtenerIntangiblePorId(idIntangible);

            if (intangible == null) {
                request.setAttribute("error", "El intangible no fue encontrado.");
                mostrarFormularioEditar(request, response);
                return;
            }

            // Recalcular valores si el monto cambió o si se cambió el intangible
            BigDecimal costo = BigDecimal.valueOf(intangible.getCosto());
            int vidaAnios = parseVidaAnios(intangible.getVidaUtil());
            int totalMeses = vidaAnios * 12;

            BigDecimal amortMensual = costo.divide(BigDecimal.valueOf(totalMeses), 2, RM);
            BigDecimal amortAnual = costo.divide(BigDecimal.valueOf(vidaAnios), 2, RM);

            // Actualizar objeto
            detalleExistente.setIdIntangible(idIntangible);
            detalleExistente.setMes(mes);
            detalleExistente.setAnio(anio);
            detalleExistente.setMonto(monto);
            detalleExistente.setAmortizacionMensual(amortMensual.doubleValue());
            detalleExistente.setAmortizacionAnual(amortAnual.doubleValue());

            LocalDate fechaCuota = LocalDate.of(anio, mes, 1);
            detalleExistente.setFechaCuota(fechaCuota.toString());
            detalleExistente.setFechaRegistro(fechaCuota.toString()); // Usar fecha_cuota como referencia

            // Guardar cambios
            boolean exito = dao.actualizarAmortizacion(detalleExistente);

            if (exito) {
                response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                    java.net.URLEncoder.encode("Amortización actualizada exitosamente.", "UTF-8"));
            } else {
                request.setAttribute("error", "Ocurrió un error al actualizar la amortización.");
                mostrarFormularioEditar(request, response);
            }

        } catch (SQLException ex) {
            throw new ServletException("Error actualizando amortización: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            request.setAttribute("error", "Error: " + ex.getMessage());
            try {
                mostrarFormularioEditar(request, response);
            } catch (Exception e) {
                throw new ServletException("Error: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Elimina una amortización
     */
    private void eliminarAmortizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idDetalle = request.getParameter("iddetalle");

            DetalleAmortizacionDAO dao = new DetalleAmortizacionDAO();
            dao.eliminarAmortizacion(idDetalle);

            response.sendRedirect("AmortizacionControlador?accion=listar&mensaje=" +
                java.net.URLEncoder.encode("Amortización eliminada exitosamente.", "UTF-8"));

        } catch (SQLException ex) {
            throw new ServletException("Error eliminando amortización: " + ex.getMessage(), ex);
        }
    }

    /**
     * Parsea la vida útil a años
     */
    private int parseVidaAnios(String vida) {
        if (vida == null) return 1;
        String s = vida.toLowerCase().trim();
        String num = s.replaceAll("\\D+", "");
        if (num.isEmpty()) return 1;

        int n = Integer.parseInt(num);

        if (s.contains("mes")) {
            return Math.max(1, (int) Math.ceil(n / 12.0));
        }
        return Math.max(1, n);
    }

    /**
     * Parsea un string a entero con valor por defecto
     */
    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Parsea un string a double con valor por defecto
     */
    private double parseDouble(String s, double def) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }

}

