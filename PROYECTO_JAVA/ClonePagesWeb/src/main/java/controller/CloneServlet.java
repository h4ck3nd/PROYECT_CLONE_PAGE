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
            response.getWriter().write("URL o nombre de carpeta no proporcionado.");
            return;
        }

        // Directorio destino: /webapp/sites/[nombre]
        String sitesBasePath = getServletContext().getRealPath("/sites/");
        File outputFolder = new File(sitesBasePath, nombre);
        if (!outputFolder.exists()) outputFolder.mkdirs();

        // Obtener y procesar el HTML
        String htmlContent = fetchHtmlContent(url);
        Document doc = Jsoup.parse(htmlContent, url);

        // Corregir rutas relativas
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

        // Guardar index.html
        File indexFile = new File(outputFolder, "index.html");
        FileUtils.writeStringToFile(indexFile, doc.html(), "UTF-8");

        // Descargar recursos
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

        // Crear ZIP para descargar
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(baos)) {
            zipFolder(outputFolder, nombre, zipOutput);
            zipOutput.finish();
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre + ".zip\"");
        response.getOutputStream().write(baos.toByteArray());
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
