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
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        h1 {
            background-color: #4a90e2;
            color: white;
            margin: 0;
            padding: 20px 0;
        }

        .container {
            margin: 30px auto;
            width: 90%;
            max-width: 800px;
        }

        .card {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            margin: 15px 0;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .card a {
            text-decoration: none;
            color: #4a90e2;
            font-weight: bold;
            font-size: 18px;
        }

        .card a:hover {
            color: #2b6cb0;
        }

        .no-sites {
            color: #777;
            font-style: italic;
        }

        .back-link {
            display: inline-block;
            margin-top: 30px;
            text-decoration: none;
            color: #4a90e2;
            font-weight: bold;
        }

        .back-link:hover {
            color: #2b6cb0;
        }
    </style>
</head>
<body>
    <h1>🌐 Mis Sitios Clonados</h1>
    <div class="container">
        <% if (folders != null && folders.length > 0) {
            for (File folder : folders) {
        %>
            <div class="card">
                <a href="sites/<%= folder.getName() %>/index.html" target="_blank">
                    📁 <%= folder.getName() %>
                </a>
            </div>
        <%  }
        } else { %>
            <p class="no-sites">No hay sitios clonados todavía.</p>
        <% } %>

        <a class="back-link" href="index.jsp">← Volver al formulario</a>
    </div>
</body>
</html>
