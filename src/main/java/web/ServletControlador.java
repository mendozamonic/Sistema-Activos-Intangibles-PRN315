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
            request.getRequestDispatcher("/Usuario.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idUsuario = request.getParameter("idUsuario");
        String contrasena = request.getParameter("contrasena");
        
        Usuario usuarioLogin = new Usuario(idUsuario, contrasena);
        UsuarioDAO usuarioDao = new UsuarioDAO();
        Usuario usuarioValidado = usuarioDao.validar(usuarioLogin);
        
        if (usuarioValidado != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioValidado.getIdUsuario());
            session.setAttribute("nombre", usuarioValidado.getNombreCompleto());
            session.removeAttribute("mensajeError"); 
            session.removeAttribute("mensajeExito");
            
            request.getRequestDispatcher("/Usuario.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession(); 
            session.setAttribute("mensajeError", "Credenciales incorrectas. Intente de nuevo.");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
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
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            session.setAttribute("mensajeError", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
            return;
        }
        
        // Validate fields are not empty
        if (nuevoUsuario == null || nuevoUsuario.trim().isEmpty() ||
            nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
            nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            session.setAttribute("mensajeError", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
            return;
        }
        
        Usuario nuevoUsuarioObj = new Usuario(nuevoUsuario.trim(), nuevaContrasena, nombreCompleto.trim());
        UsuarioDAO usuarioDao = new UsuarioDAO();
        
        
        Usuario usuarioExistente = usuarioDao.buscarPorId(nuevoUsuario.trim());
        if (usuarioExistente != null) {
            session.setAttribute("mensajeError", "El usuario ya existe. Por favor, elija otro nombre de usuario.");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
            return;
        }
        
       
        boolean creado = usuarioDao.insertar(nuevoUsuarioObj);
        
        if (creado) {
            session.setAttribute("mensajeExito", "Cuenta creada exitosamente. Por favor, inicie sesión.");
            session.removeAttribute("mensajeError");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
        } else {
            session.setAttribute("mensajeError", "Error al crear la cuenta. Intente de nuevo.");
            request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate(); 
        }
        request.getRequestDispatcher("/paginas/login.jsp").forward(request, response);
    }
}