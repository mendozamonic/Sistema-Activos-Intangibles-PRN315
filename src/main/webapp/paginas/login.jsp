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

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            /* Fondo más formal y limpio */
            background: linear-gradient(135deg, #f0f2f5 0%, #e3e7ef 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 20px;
            position: relative;
        }
        
        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            /* Líneas muy suaves casi imperceptibles */
            background: url('data:image/svg+xml,<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg"><defs><pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse"><path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(15,23,42,0.04)" stroke-width="1"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
            opacity: 0.7;
        }

        .login-container {
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(15, 23, 42, 0.15);
            padding: 40px 36px;
            width: 100%;
            max-width: 480px;
            position: relative;
            overflow: hidden;
            border: 1px solid #d0d4dc;
        }

        .login-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            /* Barra superior azul corporativo */
            background: linear-gradient(90deg, #0d47a1 0%, #1565c0 100%);
        }

        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .login-header h2 {
            color: #0f172a;
            font-weight: 700;
            margin-top: 15px;
            font-size: 26px;
            letter-spacing: -0.5px;
        }

        .login-header p {
            color: #6b7280;
            font-size: 14px;
            margin-top: 8px;
        }

        .form-control {
            border: 1px solid #d1d5db;
            border-radius: 8px;
            padding: 10px 14px;
            font-size: 14px;
            transition: all 0.2s ease;
        }

        .form-control:focus {
            border-color: #0d47a1;
            box-shadow: 0 0 0 3px rgba(13, 71, 161, 0.15);
            outline: none;
        }

        .form-label {
            font-weight: 600;
            color: #374151;
            font-size: 14px;
            margin-bottom: 6px;
        }

        .btn-login {
            background: #0d47a1;
            border: none;
            width: 100%;
            padding: 14px;
            font-weight: 600;
            font-size: 15px;
            margin-top: 10px;
            transition: all 0.2s ease;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(13, 71, 161, 0.35);
            text-transform: uppercase;
            letter-spacing: 0.8px;
        }

        .btn-login:hover {
            background: #0b3b86;
            box-shadow: 0 6px 16px rgba(13, 71, 161, 0.45);
            transform: translateY(-1px);
        }

        .btn-success {
            background: #2e7d32;
            border: none;
            box-shadow: 0 4px 10px rgba(46, 125, 50, 0.3);
        }

        .btn-success:hover {
            background: #255d27;
            box-shadow: 0 6px 14px rgba(46, 125, 50, 0.4);
        }

        .btn-toggle {
            background: transparent;
            border: none;
            color: #0d47a1;
            text-decoration: none;
            cursor: pointer;
            padding: 0;
            margin-top: 15px;
            font-weight: 600;
            font-size: 14px;
            transition: color 0.2s ease;
        }

        .btn-toggle:hover {
            color: #0b3b86;
            text-decoration: underline;
        }

        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 2px 6px rgba(15, 23, 42, 0.12);
        }

        .icon-large {
            font-size: 60px;
            /* Icono azul sólido, sin degradado llamativo */
            color: #0d47a1;
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
                        maxlength="50"
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
                        maxlength="255"
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
                        maxlength="50"
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
                        maxlength="100"
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
                        maxlength="255"
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
                        maxlength="255"
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
                alert('Las contraseñas no coinciden. Por favor, verifíquelas.');
                return false;
            }
        });
    </script>

</body>

</html>
