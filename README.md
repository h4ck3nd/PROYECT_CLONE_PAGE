# 🕸️ Clonador de Sitios Web en Java (Tomcat + Maven)

Este proyecto es una aplicación web desarrollada en Java con Maven y desplegada en un servidor Apache Tomcat. Su principal funcionalidad es **clonar sitios web estáticos** a partir de una URL.

## ✨ Características

- 🌐 Introduce una URL y clona el sitio (HTML, CSS, JS, imágenes...).
- 🗂️ Guarda el sitio clonado en la carpeta `webapp/sites/nombre-sitio`.
- 📦 Genera un archivo `.zip` del sitio y lo descarga automáticamente.
- 📁 Muestra una lista de todos los sitios clonados accesibles localmente desde el navegador.

## 📌 Flujo de uso

1. Usuario ingresa una URL en un formulario.
2. El servidor descarga el HTML y los recursos.
3. Ajusta rutas (`/css.css` → `css.css` si es local).
4. Guarda la estructura en `/sites/nombre`.
5. Muestra enlace de descarga `.zip` y lista de sitios clonados.

## 🔗 Accesos útiles

- Formulario: `http://localhost:8080/ClonePagesWeb/index.jsp`
- Sitios clonados: `http://localhost:8080/ClonePagesWeb/sitios.jsp`

---

## 🛠 Tecnologías usadas

- Java 21
- Maven
- Tomcat 9.0
- JSoup (parseo HTML)
- Apache HttpClient (descarga recursos)
- Apache Commons IO / Compress (ZIP)

