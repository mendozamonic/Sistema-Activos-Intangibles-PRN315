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
        String usuario = trim(request.getParameter("usuario"));
        String proveedor = trim(request.getParameter("proveedor"));
        String codigo = trim(request.getParameter("codigo"));
        String fechaDesde = trim(request.getParameter("desde"));
        String fechaHasta = trim(request.getParameter("hasta"));

        int page = parseInt(request.getParameter("page"), 1);
        int size = parseInt(request.getParameter("size"), 10);
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        int offset = (page - 1) * size;

        HistorialDAO.Filtros filtros = new HistorialDAO.Filtros();
        filtros.usuario = usuario;
        filtros.proveedor = proveedor;
        filtros.codigo = codigo;
        filtros.fechaDesde = fechaDesde;
        filtros.fechaHasta = fechaHasta;
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

            request.setAttribute("usuario", usuario);
            request.setAttribute("proveedor", proveedor);
            request.setAttribute("codigo", codigo);
            request.setAttribute("desde", fechaDesde);
            request.setAttribute("hasta", fechaHasta);

            request.getRequestDispatcher("/paginas/historial.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar historial", e);
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return def; }
    }
}


