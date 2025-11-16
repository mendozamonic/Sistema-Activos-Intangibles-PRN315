/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import datos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import modelo.Usuario;

/**
 *
 * @author lulic
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "Logout":
                    this.logout(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "Login":
                    this.login(request, response);
                    break;
                case "Registrar":
                    this.registrar(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);
        
        if (loggedIn) {
            // Try different possible paths for the menu
            try {
                request.getRequestDispatcher("/Usuario.jsp").forward(request, response);
            } catch (Exception e) {
                // If that fails, try paginas folder
                request.getRequestDispatcher("/paginas/Usuario.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idUsuario = request.getParameter("idUsuario");
        String contrasena = request.getParameter("contrasena");
        
        // Validate that fields are not empty
        if (idUsuario == null || idUsuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("mensajeError", "Por favor, complete todos los campos.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        Usuario usuarioLogin = new Usuario(idUsuario.trim(), contrasena);
        UsuarioDAO usuarioDao = new UsuarioDAO();
        Usuario usuarioValidado = usuarioDao.validar(usuarioLogin);
        
        if (usuarioValidado != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioValidado.getIdUsuario());
            session.setAttribute("nombre", usuarioValidado.getNombreCompleto());
            session.removeAttribute("mensajeError"); 
            session.removeAttribute("mensajeExito");
            
            // Use sendRedirect instead of forward for better URL handling
            response.sendRedirect(request.getContextPath() + "/Usuario.jsp");
        } else {
            HttpSession session = request.getSession(); 
            session.setAttribute("mensajeError", "Credenciales incorrectas. Intente de nuevo.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        String nuevoUsuario = request.getParameter("nuevoUsuario");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");
        
        HttpSession session = request.getSession();
        
        // Validate passwords match
        if (nuevaContrasena == null || confirmarContrasena == null || 
            !nuevaContrasena.equals(confirmarContrasena)) {
            session.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        // Validate fields are not empty
        if (nuevoUsuario == null || nuevoUsuario.trim().isEmpty() ||
            nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
            nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            session.setAttribute("mensajeError", "Todos los campos son obligatorios.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        // Validate length constraints
        String nuevoUsuarioTrimmed = nuevoUsuario.trim();
        String nombreCompletoTrimmed = nombreCompleto.trim();
        
        if (nuevoUsuarioTrimmed.length() > 50) {
            session.setAttribute("mensajeError", "El nombre de usuario no puede exceder 50 caracteres.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        if (nombreCompletoTrimmed.length() > 100) {
            session.setAttribute("mensajeError", "El nombre completo no puede exceder 100 caracteres.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        if (nuevaContrasena.length() > 255) {
            session.setAttribute("mensajeError", "La contraseña no puede exceder 255 caracteres.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        Usuario nuevoUsuarioObj = new Usuario(nuevoUsuarioTrimmed, nuevaContrasena, nombreCompletoTrimmed);
        UsuarioDAO usuarioDao = new UsuarioDAO();
        
        // Check if user already exists
        Usuario usuarioExistente = usuarioDao.buscarPorId(nuevoUsuarioTrimmed);
        if (usuarioExistente != null) {
            session.setAttribute("mensajeError", "El usuario ya existe. Por favor, elija otro nombre de usuario.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
            return;
        }
        
        // Insert new user
        boolean creado = usuarioDao.insertar(nuevoUsuarioObj);
        
        if (creado) {
            session.setAttribute("mensajeExito", "Cuenta creada exitosamente. Por favor, inicie sesión.");
            session.removeAttribute("mensajeError");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        } else {
            session.setAttribute("mensajeError", "Error al crear la cuenta. Verifique los datos e intente de nuevo.");
            response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate(); 
        }
        response.sendRedirect(request.getContextPath() + "/paginas/login.jsp");
    }
}