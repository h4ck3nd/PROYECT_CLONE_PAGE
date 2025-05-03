<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clonar Página</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f7fa;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #333;
        }

        h1 {
            color: #4caf50;
            font-size: 2.5rem;
            margin-bottom: 30px;
        }

        form {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            transition: transform 0.3s ease;
        }

        form:hover {
            transform: scale(1.02);
        }

        input[type="text"] {
            width: 100%;
            padding: 12px 20px;
            margin: 12px 0;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border 0.3s ease;
        }

        input[type="text"]:focus {
            border-color: #4caf50;
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #4caf50;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 10px;
            text-align: center;
            align-content: center;
            align-items: center;
        }

        button:hover {
            background-color: #45a049;
        }

        .link-button {
            width: 100%;
            padding: 12px;
            background-color: #2196F3;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 30px;
            margin-left: 20px;
            text-decoration: none;
            display: block;
            text-align: center;
            align-content: center;
            align-items: center;
        }

        .link-button:hover {
            background-color: #1976d2;
        }
        .footer {
            position: fixed;
            bottom: 20px;
            font-size: 14px;
            color: #777;
        }
    </style>
</head>
<body>

    <div>
        <h1>🌐 Clonar página web</h1>

        <form action="clone" method="post">
            <input type="text" name="url" placeholder="URL de la página" required><br>
            <input type="text" name="nombre" placeholder="Nombre para la carpeta" required><br>
            <button type="submit">Clonar y Descargar</button>
        </form>

        <a href="sitios.jsp" class="link-button">Ver Sitios Clonados</a>
    </div>
    
	<div class="footer">
        <p>Powered by <b>YourSite</b> | <a href="index.jsp" style="color: #4caf50; text-decoration: none;">Volver</a></p>
    </div>
</body>
</html>
