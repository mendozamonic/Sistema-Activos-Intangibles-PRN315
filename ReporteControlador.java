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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="ReporteControlador", urlPatterns={"/ReporteControlador"})
public class ReporteControlador extends HttpServlet {

    private static final RoundingMode RM = RoundingMode.HALF_UP;

    private BigDecimal div2(BigDecimal a, int b) {
        if (a == null || b <= 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(BigDecimal.valueOf(b), 2, RM);
    }

    // Vida útil: 12 meses = 1 año, 18 meses = 2 años, “12” = 12 años
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

    private int mesesTranscurridos(LocalDate fechaAdq, int repYear, int repMonth, int vidaAnios) {
        if (fechaAdq == null) return 0;

        YearMonth compra = YearMonth.from(fechaAdq);
        YearMonth reporte = YearMonth.of(repYear, repMonth);

        if (reporte.isBefore(compra)) return 0;

        long diff = ChronoUnit.MONTHS.between(compra, reporte) + 1; 
        int totalVidaMeses = vidaAnios * 12;

        if (diff < 0) diff = 0;
        if (diff > totalVidaMeses) diff = totalVidaMeses;

        return (int) diff;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String modo = request.getParameter("modo"); // mes | anio
        int mes = parseInt(request.getParameter("mes"), 1);
        int anio = parseInt(request.getParameter("anio"), LocalDate.now().getYear());

        List<Intangible> lista;
        try {
            lista = new IntangibleDAO().listarIntangibles();
        } catch (SQLException e) {
            throw new ServletException("Error al listar intangibles", e);
        }

        // Lista donde enviaremos los resultados ya calculados
        List<ReporteFila> reporte = new ArrayList<>();

        for (Intangible x : lista) {

            LocalDate fechaAdq = null;
            try {
                if (x.getFechaAdquisicion() != null)
                    fechaAdq = LocalDate.parse(x.getFechaAdquisicion());
            } catch (Exception ignored) {}

            BigDecimal costo = BigDecimal.valueOf(x.getCosto());
            int vidaAnios = parseVidaAnios(x.getVidaUtil());
            int totalMeses = vidaAnios * 12;

            BigDecimal amortAnual = div2(costo, vidaAnios);
            BigDecimal amortMensual = div2(costo, totalMeses);

            int mTrans;
            BigDecimal amortPeriodo;

            if ("anio".equalsIgnoreCase(modo)) {

                int mHastaFin = mesesTranscurridos(fechaAdq, anio, 12, vidaAnios);
                int mHastaPrev = mesesTranscurridos(fechaAdq, anio - 1, 12, vidaAnios);
                int mEnAnio = Math.max(0, mHastaFin - mHastaPrev);

                mTrans = mHastaFin;
                amortPeriodo = amortMensual.multiply(BigDecimal.valueOf(mEnAnio)).setScale(2, RM);

            } else {

                mTrans = mesesTranscurridos(fechaAdq, anio, mes, vidaAnios);
                amortPeriodo = (mTrans > 0 && mTrans <= totalMeses) ? amortMensual : BigDecimal.ZERO;
            }

            BigDecimal amortAcum = amortMensual
                    .multiply(BigDecimal.valueOf(mTrans))
                    .setScale(2, RM);

            if (amortAcum.compareTo(costo) > 0) {
                amortAcum = costo;
            }

            BigDecimal valorLibros = costo.subtract(amortAcum);
            if (valorLibros.signum() < 0) valorLibros = BigDecimal.ZERO;

            int cuotasPend = Math.max(0, totalMeses - mTrans);

            // AGREGAR FILA
            reporte.add(new ReporteFila(
                    x.getCodigo(),
                    x.getNombre_intangible(),
                    x.getTipoLicencia(),
                    x.getFechaAdquisicion(),
                    costo,
                    vidaAnios,
                    amortAnual,
                    amortMensual,
                    amortPeriodo,
                    amortAcum,
                    valorLibros,
                    cuotasPend,
                    valorLibros
            ));
        }

        request.setAttribute("reporte", reporte);
        request.getRequestDispatcher("reporte.jsp").forward(request, response);
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return def; }
    }

    // Clase interna para transportar datos al JSP
    public static class ReporteFila {
        public String codigo, nombre, tipo, fecha;
        public BigDecimal costo, amortAnual, amortMensual, amortPeriodo, amortAcum, valorLibros, remanente;
        public int vidaAnios, cuotasPend;

        public ReporteFila(String codigo, String nombre, String tipo, String fecha, BigDecimal costo,
                           int vidaAnios, BigDecimal amortAnual, BigDecimal amortMensual,
                           BigDecimal amortPeriodo, BigDecimal amortAcum, BigDecimal valorLibros,
                           int cuotasPend, BigDecimal remanente) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.tipo = tipo;
            this.fecha = fecha;
            this.costo = costo;
            this.vidaAnios = vidaAnios;
            this.amortAnual = amortAnual;
            this.amortMensual = amortMensual;
            this.amortPeriodo = amortPeriodo;
            this.amortAcum = amortAcum;
            this.valorLibros = valorLibros;
            this.cuotasPend = cuotasPend;
            this.remanente = remanente;
        }
    }
}
