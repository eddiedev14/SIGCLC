# 📚 SIGCLC — API REST (Spring Boot + MongoDB)

**SIGCLC** (Sistema de Gestión del Club de Lectura Colectiva) es una API REST desarrollada en **Java (Spring Boot)** con **MongoDB**, diseñada para gestionar actividades, propuestas, reseñas y foros de un club de lectura. La aplicación sigue una arquitectura **en capas** inspirada en el patrón **MVC**, extendida con capas de **Service** y **Repository** para favorecer separación de responsabilidades y escalabilidad.

---

## 🎯 Objetivo
Proveer un backend modular y mantenible que facilite:
- Gestión de usuarios y roles.
- Registro y administración de libros.
- Propuestas y votaciones para lecturas colectivas.
- Registro de reuniones (presenciales/virtuales).
- Reseñas, valoraciones y comentarios.
- Retos de lectura y seguimiento de progreso.
- Foros temáticos y hilos de discusión.

---

## Arquitectura (visión general)
La arquitectura del proyecto es una combinación del patrón **MVT/MVC** con una estructura en capas que incluye explícitamente capas de **Service** y **Repository**.

- **Controller (capa de presentación / endpoints)**  
  Recibe las peticiones HTTP, valida entradas básicas y delega al Service correspondiente. Retorna respuestas HTTP (JSON) con códigos y cuerpos adecuados.

- **Service (capa de negocio)**  
  Contiene la lógica de negocio, orquesta llamadas a repositorios y aplica reglas del dominio (validaciones complejas, agregaciones, cálculos de negocio).

- **Repository (capa de persistencia)**  
  Encapsula el acceso a MongoDB mediante interfaces que extienden `MongoRepository` u otras abstracciones de Spring Data.

- **Model / Documentos (capa de dominio)**  
  Define las entidades (documentos) que se almacenan en MongoDB. La transferencia entre capas se realiza usando **DTOs** y **Mappers** para desacoplar modelo de persistencia y modelo de transporte.

Este enfoque garantiza que cada componente tenga responsabilidad única y facilite pruebas unitarias e integración.

---

## 👨‍💻 Tecnologías principales
- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data MongoDB**
- **Lombok**
- **MongoDB** (document store)
- **Herramientas recomendadas:** Visual Studio Code / IntelliJ IDEA

---

## Diseño de la base de datos (MongoDB)

El modelado está pensado para permitir extensibilidad y un nivel de validación moderado en documentos embebidos. A continuación, las colecciones principales y su propósito:

- **usuarios**  
  Almacena información del usuario (nombre completo, correo, teléfono, rol, etc.).

- **libros**  
  Información bibliográfica del libro (título, autor, género, año de publicación, sinopsis, ruta de portada, registrado_por).

- **propuestasLibros**  
  Propuestas de lectura colectiva, votaciones, estado de la propuesta (ej.: `en_votacion`, `seleccionada`, `no_seleccionada`) y datos del usuario proponente.

- **reuniones**  
  Encuentros asociados a una propuesta de lectura (fecha, hora, modalidad, espacio o URL, libros seleccionados, asistentes, archivos adjuntos).

- **resenias**  
  Reseñas realizadas por usuarios con: metadatos del redactor, referencia al libro, calificación del libro, opinión, archivos adjuntos y un arreglo `valoraciones` que almacena valoraciones individuales. El campo `valoracion` de la reseña se calcula como el promedio agregado de las valoraciones individuales.

- **retosLectura**  
  Retos con título, descripción, fechas, libros asociados, usuarios inscritos y progreso por usuario/libro.

- **foros** y **comentariosForos**  
  Foros temáticos donde `comentariosForos` puede tener `parentId` opcional para modelar hilos (relación recursiva para respuestas a comentarios).

---

## Autores
- Eddie Santiago Delgado Campo  
- Sebastián Leiton Goyes  
- Christian David Home Acero

Asignatura: **Bases de Datos II** — Programa: **Ingeniería Informática**

---