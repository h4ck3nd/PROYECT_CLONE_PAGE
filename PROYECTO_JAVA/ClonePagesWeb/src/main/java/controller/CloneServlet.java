package controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clone")
public class CloneServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        String nombre = request.getParameter("nombre");

        if (url == null || url.isEmpty() || nombre == null || nombre.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<html><body><h2>Error: URL o nombre no proporcionado.</h2><a href='index.jsp'>Volver</a></body></html>");
            return;
        }

        long startTime = System.currentTimeMillis();

        String sitesBasePath = getServletContext().getRealPath("/sites/");
        File outputFolder = new File(sitesBasePath, nombre);
        if (!outputFolder.exists()) outputFolder.mkdirs();

        String htmlContent = fetchHtmlContent(url);
        Document doc = Jsoup.parse(htmlContent, url);

        Elements elements = doc.select("[src], [href]");
        for (Element el : elements) {
            String attr = el.hasAttr("href") ? "href" : "src";
            String value = el.attr(attr);
            if (value.startsWith("/") && !value.substring(1).contains("/")) {
                el.attr(attr, value.substring(1));
            } else if (value.startsWith("/")) {
                el.attr(attr, value.substring(1));
            }
        }

        File indexFile = new File(outputFolder, "index.html");
        FileUtils.writeStringToFile(indexFile, doc.html(), "UTF-8");

        List<String> resources = getResources(doc);
        for (String resourceUrl : resources) {
            try {
                URL resUrl = new URL(resourceUrl);
                String relativePath = resUrl.getPath();
                if (relativePath.startsWith("/")) relativePath = relativePath.substring(1);
                File resFile = new File(outputFolder, relativePath);
                resFile.getParentFile().mkdirs();
                FileUtils.writeByteArrayToFile(resFile, downloadResource(resourceUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(baos)) {
            zipFolder(outputFolder, nombre, zipOutput);
            zipOutput.finish();
        }

        // Guardar el ZIP en disco para el DownloadServlet
        File zipFile = new File(sitesBasePath, nombre + ".zip");
        FileUtils.writeByteArrayToFile(zipFile, baos.toByteArray());

        long endTime = System.currentTimeMillis();
        long seconds = Math.max(1, (endTime - startTime) / 1000); // mínimo 1 segundo para evitar división por cero

        // Respuesta HTML con barra de progreso
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Clonando...</title>");
        out.println("<style>");
        out.println("body{text-align:center;font-family:sans-serif;}");
        out.println(".bar{width:60%;margin:auto;background:#ccc;border-radius:10px;overflow:hidden;height:30px;}");
        out.println(".fill{width:0;height:100%;background:#4caf50;transition:width 1s;}");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h2>🛠 Clonando y preparando tu descarga...</h2>");
        out.println("<img src='https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExenBtNzZzZ2dtc3Zqem9yMXRhbnZlMHhkZGF4OHY2dXk4NjdjajN4ZCZlcD12MV8zYXRrNXVrZ2V0ZzRxMjdkZWhnOHF2aXAyZ2M2eQ/g0lMZVfKt4wvm/giphy.gif' width='150'><br><br>");
        out.println("<div class='bar'><div class='fill' id='fill'></div></div>");
        out.println("<p id='text'></p>");
        out.println("<script>");
        out.println("let secs = " + seconds + ";");
        out.println("let i = 0;");
        out.println("let bar = document.getElementById('fill');");
        out.println("let txt = document.getElementById('text');");
        out.println("let interval = setInterval(() => {");
        out.println("  i++;");
        out.println("  bar.style.width = (i * 100 / secs) + '%';");
        out.println("  txt.innerText = `Esperando... ${secs - i} segundos restantes`;");
        out.println("  if (i >= secs) {");
        out.println("    clearInterval(interval);");
        out.println("    txt.innerText = '¡Hecho! Iniciando descarga...';");
        out.println("    window.location.href = 'download?file=" + nombre + "';");
        out.println("  }");
        out.println("}, 1000);");
        out.println("</script>");
        out.println("<br><a href='sitios.jsp'><button style='margin-top:20px;padding:10px 20px;font-size:16px;'>Ver sitios clonados</button></a>");
        out.println("</body></html>");

    }

    private String fetchHtmlContent(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        }
    }

    private byte[] downloadResource(String resourceUrl) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(resourceUrl);
            HttpResponse response = client.execute(request);
            return EntityUtils.toByteArray(response.getEntity());
        }
    }

    private List<String> getResources(Document doc) {
        List<String> resources = new ArrayList<>();
        doc.select("img[src]").forEach(e -> resources.add(e.absUrl("src")));
        doc.select("link[rel=stylesheet][href]").forEach(e -> resources.add(e.absUrl("href")));
        doc.select("script[src]").forEach(e -> resources.add(e.absUrl("src")));
        return resources;
    }

    private void zipFolder(File sourceFolder, String basePath, ZipArchiveOutputStream zipOut) throws IOException {
        for (File file : sourceFolder.listFiles()) {
            String entryName = basePath + "/" + file.getName();
            if (file.isDirectory()) {
                zipFolder(file, entryName, zipOut);
            } else {
                zipOut.putArchiveEntry(new ZipArchiveEntry(entryName));
                try (FileInputStream fis = new FileInputStream(file)) {
                    zipOut.write(fis.readAllBytes());
                }
                zipOut.closeArchiveEntry();
            }
        }
    }
}
