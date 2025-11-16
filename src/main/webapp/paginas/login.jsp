<%-- 
    Document   : login
    Created on : 16 nov 2025, 08:34:22
    Author     : lulic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login - Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        
        body {
            background-color: #add8e3; 
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .login-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            padding: 40px;
            width: 100%;
            max-width: 450px;
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-header h2 {
            
            color: #4fc3f7; 
            font-weight: bold;
            margin-top: 15px;
        }
        .form-control:focus {
           
            border-color: #add8e3;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        
        .btn-login {
            background-color: #4fc3f7; 
            border: none;
            width: 100%;
            padding: 12px;
            font-weight: bold;
            margin-top: 10px;
            transition: all 0.3s;
        }
        .btn-login:hover {
            transform: translateY(-2px);     
            box-shadow: 0 5px 15px rgba(0, 123, 255, 0.4); 
        }
        .btn-toggle {
            background: transparent;
            border: none;
            color: #4fc3f7; 
            text-decoration: underline;
            cursor: pointer;
            padding: 0;
            margin-top: 15px;
        }
        .btn-toggle:hover {
            color: #0056b3; 
        }
        .alert {
            border-radius: 10px;
        }
        .icon-large {
           
            font-size: 4rem;
            color: #4fc3f7; 
        }
        #registroForm {
            display: none;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <i class="bi bi-person-circle icon-large"></i>
            <h2 class="mt-3" id="formTitle">Iniciar Sesión</h2>
            <p class="text-muted" id="formSubtitle">Ingrese sus credenciales</p>
        </div>
        
        <% 
            String mensajeError = (String) session.getAttribute("mensajeError");
            String mensajeExito = (String) session.getAttribute("mensajeExito");
            if (mensajeError != null) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill"></i> <%= mensajeError %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
                session.removeAttribute("mensajeError");
            }
            if (mensajeExito != null) {
        %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill"></i> <%= mensajeExito %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
                session.removeAttribute("mensajeExito");
            }
        %>
        
        <form id="loginForm" action="${pageContext.request.contextPath}/ServletControlador" method="POST">
            <input type="hidden" name="accion" value="Login">
            
            <div class="mb-3">
                <label for="idUsuario" class="form-label">
                    <i class="bi bi-person"></i> Usuario
                </label>
                <input type="text" 
                        class="form-control" 
                        id="idUsuario" 
                        name="idUsuario" 
                        placeholder="Ingrese su usuario"
                        required 
                        autofocus>
            </div>
            
            <div class="mb-3">
                <label for="contrasena" class="form-label">
                    <i class="bi bi-lock"></i> Contraseña
                </label>
                <input type="password" 
                        class="form-control" 
                        id="contrasena" 
                        name="contrasena" 
                        placeholder="Ingrese su contraseña"
                        required>
            </div>
            
            <button type="submit" class="btn btn-primary btn-login">
                <i class="bi bi-box-arrow-in-right"></i> Ingresar
            </button>
        </form>
        
        <form id="registroForm" action="${pageContext.request.contextPath}/ServletControlador" method="POST">
            <input type="hidden" name="accion" value="Registrar">
            
            <div class="mb-3">
                <label for="nuevoUsuario" class="form-label">
                    <i class="bi bi-person"></i> Usuario
                </label>
                <input type="text" 
                        class="form-control" 
                        id="nuevoUsuario" 
                        name="nuevoUsuario" 
                        placeholder="Elija un nombre de usuario"
                        required>
            </div>
            
            <div class="mb-3">
                <label for="nombreCompleto" class="form-label">
                    <i class="bi bi-person-badge"></i> Nombre Completo
                </label>
                <input type="text" 
                        class="form-control" 
                        id="nombreCompleto" 
                        name="nombreCompleto" 
                        placeholder="Ingrese su nombre completo"
                        required>
            </div>
            
            <div class="mb-3">
                <label for="nuevaContrasena" class="form-label">
                    <i class="bi bi-lock"></i> Contraseña
                </label>
                <input type="password" 
                        class="form-control" 
                        id="nuevaContrasena" 
                        name="nuevaContrasena" 
                        placeholder="Elija una contraseña"
                        required>
            </div>
            
            <div class="mb-3">
                <label for="confirmarContrasena" class="form-label">
                    <i class="bi bi-lock-fill"></i> Confirmar Contraseña
                </label>
                <input type="password" 
                        class="form-control" 
                        id="confirmarContrasena" 
                        name="confirmarContrasena" 
                        placeholder="Confirme su contraseña"
                        required>
            </div>
            
            <button type="submit" class="btn btn-success btn-login">
                <i class="bi bi-person-plus"></i> Crear Cuenta
            </button>
        </form>
        
        <div class="text-center mt-3">
            <button type="button" class="btn-toggle" id="toggleForm">
                <span id="toggleText"> Crear nueva cuenta </span>
            </button>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const loginForm = document.getElementById('loginForm');
        const registroForm = document.getElementById('registroForm');
        const toggleBtn = document.getElementById('toggleForm');
        const toggleText = document.getElementById('toggleText');
        const formTitle = document.getElementById('formTitle');
        const formSubtitle = document.getElementById('formSubtitle');
        
        toggleBtn.addEventListener('click', function() {
            if (loginForm.style.display === 'none') {
                
                loginForm.style.display = 'block';
                registroForm.style.display = 'none';
                formTitle.textContent = 'Iniciar Sesión';
                formSubtitle.textContent = 'Ingrese sus credenciales';
                toggleText.textContent = '¿No tienes cuenta? Crear una nueva';
            } else {
                // Show registration form
                loginForm.style.display = 'none';
                registroForm.style.display = 'block';
                formTitle.textContent = 'Crear Cuenta';
                formSubtitle.textContent = 'Complete el formulario para registrarse';
                toggleText.textContent = '¿Ya tienes cuenta? Iniciar sesión';
            }
        });
        
        
        document.getElementById('registroForm').addEventListener('submit', function(e) {
            const password = document.getElementById('nuevaContrasena').value;
            const confirmPassword = document.getElementById('confirmarContrasena').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Las contraseñas no coinciden. Por favor, verifique.');
                return false;
            }
        });
    </script>
</body>
</html>