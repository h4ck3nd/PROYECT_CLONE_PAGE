# ⚙️ Guía Técnica para Desarrolladores - Clonador Web Java

Este proyecto usa el stack Java EE (Servlets) con Maven en Eclipse IDE y está preparado para ser ejecutado en un servidor Apache Tomcat.

## 📁 Estructura del Proyecto

ClonePagesWeb/

├── src/

│ └── controller/

│ └── CloneServlet.java

├── webapp/

│ ├── index.jsp

│ ├── sitios.jsp

│ └── sites/ ← Carpeta donde se clonan los sitios

├── pom.xml

REALMENTE SE GUARDAN LOS SITIOS EN (C:\Users\<USER>\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\\<NAME_PROYECT>\sites)

## 📦 Dependencias Maven

- `jsoup` - Parseo del HTML
- `httpclient` - Descargar recursos
- `commons-io` - Operaciones de archivos
- `commons-compress` - ZIP dinámico
- `javax.servlet-api` - Servlets (scope `provided`)

## 🧑‍💻 Cómo importar el proyecto en Eclipse

1. Abre Eclipse.
2. **File > Import > Maven > Existing Maven Projects**.
3. Selecciona el proyecto y pulsa _Finish_.
4. Asegúrate de tener **Tomcat 9 configurado**.
5. Haz _Run on Server_ sobre el proyecto.

## 🛠️ Notas técnicas

- El servlet principal es `CloneServlet.java`.
- Se usa `@WebServlet("/clone")` para manejar POST desde el formulario.
- El ZIP se genera **en memoria** y no se guarda físicamente.
- Los archivos clonados se escriben en `webapp/sites/[nombre]`.

## 📎 Mejores prácticas implementadas

- Separación de responsabilidades (HTML, recursos, lógica).
- Rutas relativas ajustadas para navegación local.
- Estructura de carpetas replicada fielmente.
