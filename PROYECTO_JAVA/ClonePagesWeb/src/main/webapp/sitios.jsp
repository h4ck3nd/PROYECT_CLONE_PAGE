<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String sitesPath = application.getRealPath("/sites");
    File sitesDir = new File(sitesPath);
    File[] folders = sitesDir.listFiles(File::isDirectory);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Sitios Clonados</title>
</head>
<body>
    <h1>Lista de Sitios Clonados</h1>
    <ul>
        <% if (folders != null) {
            for (File folder : folders) {
        %>
            <li><a href="sites/<%= folder.getName() %>/index.html" target="_blank"><%= folder.getName() %></a></li>
        <%  }
        } else { %>
            <li>No hay sitios clonados todavía.</li>
        <% } %>
    </ul>
    <br>
    <a href="index.jsp">← Volver al formulario</a>
</body>
</html>
