<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clonador de Sitios Web</title>
</head>
<body>
    <h1>Clonador de Sitios Web</h1>
    <form action="<%= request.getContextPath() %>/clone" method="POST">
        <label for="url">Ingresa la URL del sitio web:</label>
        <input type="url" id="url" name="url" required><br><br>

        <label for="nombre">Nombre para guardar la carpeta:</label>
        <input type="text" id="nombre" name="nombre" required><br><br>
        
        <hr>
		<a href="sitios.jsp">Ver mis sitios clonados</a>

        <button type="submit">Clonar y Descargar</button>
    </form>
</body>
</html>
