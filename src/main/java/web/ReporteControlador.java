/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import datos.ReporteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Reporte;

@WebServlet(name="ReporteControlador", urlPatterns={"/ReporteControlador"})
public class ReporteControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar si se solicita exportación
        String formato = request.getParameter("formato");
        if (formato != null && (formato.equals("excel") || formato.equals("pdf"))) {
            exportarReporte(request, response, formato);
            return;
        }

        // Generar reporte para visualización
        String modo = request.getParameter("modo"); // mes | anio
        int mes = parseInt(request.getParameter("mes"), 1);
        int anio = parseInt(request.getParameter("anio"), LocalDate.now().getYear());

        try {
            ReporteDAO reporteDAO = new ReporteDAO();
            List<Reporte> reporte = reporteDAO.generarReporte(modo != null ? modo : "mes", mes, anio);
            
            request.setAttribute("reporte", reporte);
            request.getRequestDispatcher("/paginas/reportes/reporte.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al generar reporte: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta el reporte en formato Excel o PDF
     */
    private void exportarReporte(HttpServletRequest request, HttpServletResponse response, String formato)
            throws ServletException, IOException {

        try {
            String modo = request.getParameter("modo");
            int mes = parseInt(request.getParameter("mes"), 1);
            int anio = parseInt(request.getParameter("anio"), LocalDate.now().getYear());

            // Obtener datos del reporte usando el DAO
            ReporteDAO reporteDAO = new ReporteDAO();
            List<Reporte> reporte = reporteDAO.generarReporte(modo != null ? modo : "mes", mes, anio);

            String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            String titulo = "Reporte de Amortización - " + meses[mes] + " " + anio;

            if ("excel".equals(formato)) {
                exportarExcel(response, reporte, titulo, mes, anio);
            } else if ("pdf".equals(formato)) {
                exportarPDF(response, reporte, titulo, mes, anio);
            }

        } catch (SQLException e) {
            throw new ServletException("Error al exportar reporte: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta el reporte a Excel usando Apache POI
     */
    private void exportarExcel(HttpServletResponse response, List<Reporte> reporte, 
                               String titulo, int mes, int anio) throws IOException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Reporte de Amortización");

            // Crear estilo para encabezados
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);

            // Título
            org.apache.poi.ss.usermodel.Row titleRow = sheet.createRow(0);
            org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            org.apache.poi.ss.usermodel.CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Encabezados
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(2);
            String[] headers = {"Código", "Nombre", "Tipo", "Fecha Adq", "Costo", "Vida (años)",
                               "Amort. Anual", "Amort. Mensual", "Amort. Período", "Amort. Acum",
                               "Valor en Libros", "Cuotas Pend."};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 3;
            for (Reporte fila : reporte) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(fila.getCodigo() != null ? fila.getCodigo() : "");
                row.createCell(1).setCellValue(fila.getNombre() != null ? fila.getNombre() : "");
                row.createCell(2).setCellValue(fila.getTipo() != null ? fila.getTipo() : "");
                row.createCell(3).setCellValue(fila.getFecha() != null ? fila.getFecha() : "");
                row.createCell(4).setCellValue(fila.getCosto().doubleValue());
                row.createCell(5).setCellValue(fila.getVidaAnios());
                row.createCell(6).setCellValue(fila.getAmortAnual().doubleValue());
                row.createCell(7).setCellValue(fila.getAmortMensual().doubleValue());
                row.createCell(8).setCellValue(fila.getAmortPeriodo().doubleValue());
                row.createCell(9).setCellValue(fila.getAmortAcum().doubleValue());
                row.createCell(10).setCellValue(fila.getValorLibros().doubleValue());
                row.createCell(11).setCellValue(fila.getCuotasPend());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Configurar respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"reporte_amortizacion_" + mes + "_" + anio + ".xlsx\"");

            OutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();

        } catch (Exception e) {
            throw new IOException("Error generando Excel: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta el reporte a PDF usando iText
     */
    private void exportarPDF(HttpServletResponse response, List<Reporte> reporte,
                             String titulo, int mes, int anio) throws IOException {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"reporte_amortizacion_" + mes + "_" + anio + ".pdf\"");

            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

            // Título
            com.itextpdf.layout.element.Paragraph title = new com.itextpdf.layout.element.Paragraph(titulo)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            document.add(title);
            document.add(new com.itextpdf.layout.element.Paragraph("\n"));

            // Tabla
            float[] columnWidths = {1.5f, 2.5f, 1.5f, 1.2f, 1.2f, 1f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1f};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(columnWidths);

            // Encabezados
            String[] headers = {"Código", "Nombre", "Tipo", "Fecha", "Costo", "Vida", "Amort.Anual",
                               "Amort.Mens", "Amort.Per", "Amort.Acum", "Val.Libros", "Cuot.Pend"};
            for (String header : headers) {
                table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(header))
                        .setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(31, 41, 55))
                        .setFontColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255))
                        .setBold());
            }

            // Datos
            for (Reporte fila : reporte) {
                table.addCell(fila.getCodigo() != null ? fila.getCodigo() : "");
                table.addCell(fila.getNombre() != null ? fila.getNombre() : "");
                table.addCell(fila.getTipo() != null ? fila.getTipo() : "");
                table.addCell(fila.getFecha() != null ? fila.getFecha() : "");
                table.addCell(String.format("%.2f", fila.getCosto()));
                table.addCell(String.valueOf(fila.getVidaAnios()));
                table.addCell(String.format("%.2f", fila.getAmortAnual()));
                table.addCell(String.format("%.2f", fila.getAmortMensual()));
                table.addCell(String.format("%.2f", fila.getAmortPeriodo()));
                table.addCell(String.format("%.2f", fila.getAmortAcum()));
                table.addCell(String.format("%.2f", fila.getValorLibros()));
                table.addCell(String.valueOf(fila.getCuotasPend()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new IOException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return def; }
    }
}
