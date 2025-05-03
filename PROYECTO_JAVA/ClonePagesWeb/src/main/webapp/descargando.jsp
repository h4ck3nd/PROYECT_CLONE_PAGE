<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String fileName = (String) session.getAttribute("fileName");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clonando sitio...</title>
    <style>
        body {
            text-align: center;
            font-family: Arial, sans-serif;
            margin-top: 80px;
        }
        #progressBarContainer {
            width: 60%;
            margin: 20px auto;
            background: #ddd;
            border-radius: 10px;
        }
        #progressBar {
            height: 30px;
            width: 0%;
            background: #4CAF50;
            border-radius: 10px;
        }
        #message {
            font-size: 18px;
            margin-bottom: 20px;
        }
        button {
            display: none;
            margin-top: 20px;
            padding: 10px 25px;
            font-size: 16px;
            background-color: #007bff;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
        }
    </style>
</head>
<body>

<h2 id="message">☕ Tomate un café mientras se clona tu sitio...</h2>

<div id="progressBarContainer">
    <div id="progressBar"></div>
</div>

<a href="sitios.jsp"><button id="btnSitios">Ver sitios clonados</button></a>

<script>
    let progress = 0;
    const bar = document.getElementById('progressBar');
    const message = document.getElementById('message');
    const button = document.getElementById('btnSitios');

    const interval = setInterval(() => {
        progress += 2;
        bar.style.width = progress + '%';

        if (progress >= 100) {
            clearInterval(interval);
            message.textContent = "✅ Clonación completada. Iniciando descarga...";
            button.style.display = "inline-block";

            // Inicia la descarga automáticamente
            window.location.href = "download";
        }
    }, 100); // 5 segundos total
</script>

</body>
</html>
