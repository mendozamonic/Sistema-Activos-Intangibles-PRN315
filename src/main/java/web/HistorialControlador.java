/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web;

import datos.HistorialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import modelo.Historial;

@WebServlet(name = "HistorialControlador", urlPatterns = {"/HistorialControlador"})
public class HistorialControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        if ("listar".equalsIgnoreCase(accion)) {
            listar(request, response);
        } else {
            listar(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String proveedor = trim(request.getParameter("proveedor"));
        String codigo = trim(request.getParameter("codigo"));
        String nombreIntangible = trim(request.getParameter("nombreIntangible"));
        String tipoLicencia = trim(request.getParameter("tipoLicencia"));
        String idIntangible = trim(request.getParameter("idIntangible"));
        String fechaDesde = trim(request.getParameter("desde"));
        String fechaHasta = trim(request.getParameter("hasta"));
        Integer mes = parseInt(request.getParameter("mes"), null);
        Integer anio = parseInt(request.getParameter("anio"), null);

        int page = parseInt(request.getParameter("page"), 1);
        int size = parseInt(request.getParameter("size"), 10);
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        int offset = (page - 1) * size;

        HistorialDAO.Filtros filtros = new HistorialDAO.Filtros();
        filtros.proveedor = proveedor;
        filtros.codigo = codigo;
        filtros.nombreIntangible = nombreIntangible;
        filtros.tipoLicencia = tipoLicencia;
        filtros.idIntangible = idIntangible;
        filtros.fechaDesde = fechaDesde;
        filtros.fechaHasta = fechaHasta;
        filtros.mes = mes;
        filtros.anio = anio;
        filtros.offset = offset;
        filtros.limit = size;

        try {
            HistorialDAO dao = new HistorialDAO();
            int total = dao.contar(filtros);
            List<Historial> lista = dao.listar(filtros);

            int totalPages = (int) Math.ceil((double) total / size);

            request.setAttribute("lista", lista);
            request.setAttribute("total", total);
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalPages", totalPages);

            request.setAttribute("proveedor", proveedor);
            request.setAttribute("codigo", codigo);
            request.setAttribute("nombreIntangible", nombreIntangible);
            request.setAttribute("tipoLicencia", tipoLicencia);
            request.setAttribute("idIntangible", idIntangible);
            request.setAttribute("desde", fechaDesde);
            request.setAttribute("hasta", fechaHasta);
            request.setAttribute("mes", mes);
            request.setAttribute("anio", anio);

            request.getRequestDispatcher("/paginas/historial/historial.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar historial", e);
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException | NullPointerException e) { return def; }
    }
    
    private Integer parseInt(String s, Integer def) {
        if (s == null || s.trim().isEmpty()) return def;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return def; }
    }
}


