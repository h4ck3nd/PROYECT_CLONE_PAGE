package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("file");
        if (fileName == null || fileName.isEmpty()) {
            response.getWriter().write("Nombre de archivo no especificado.");
            return;
        }

        String filePath = getServletContext().getRealPath("/sites/" + fileName + ".zip");
        File downloadFile = new File(filePath);

        if (!downloadFile.exists()) {
            response.getWriter().write("Archivo no encontrado.");
            return;
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".zip\"");

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
