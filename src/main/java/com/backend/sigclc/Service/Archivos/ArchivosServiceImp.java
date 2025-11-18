package com.backend.sigclc.Service.Archivos;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ArchivosServiceImp implements IArchivosService {

    private final Path uploadsRoot = Paths.get("uploads");

    /**
     * Guarda un archivo en el sistema de archivos validando su tipo.
     * 
     * @param archivo MultipartFile recibido desde el cliente
     * @param carpetaDestino Subcarpeta dentro de /uploads donde se guardará (ej: "portadas", "reuniones", etc.)
     * @param tiposPermitidos Lista de extensiones válidas (ej: List.of(".jpg", ".jpeg", ".png"))
     * @return Ruta relativa del archivo guardado
     */
    public String guardarArchivo(MultipartFile archivo, String carpetaDestino, List<String> tiposPermitidos) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado ningún archivo para guardar.");
        }

        try {
            // Verifica que la carpeta exista
            Path destinoCarpeta = uploadsRoot.resolve(carpetaDestino);
            if (!Files.exists(destinoCarpeta)) {
                Files.createDirectories(destinoCarpeta);
            }

            // Validar extensión del archivo
            String originalName = archivo.getOriginalFilename();
            String extension = obtenerExtension(originalName);

            if (!tiposPermitidos.contains(extension)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Extensión no permitida. Solo se permiten: " + String.join(", ", tiposPermitidos)
                );
            }

            // Asignar nombre único y guardar
            String nombreArchivo = UUID.randomUUID() + extension;
            Path rutaDestino = destinoCarpeta.resolve(nombreArchivo);

            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            return "uploads/" + carpetaDestino + "/" + nombreArchivo;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo", e);
        }
    }

    /**
     * Elimina un archivo físico en base a su ruta relativa.
     * 
     * @param rutaArchivo Ruta relativa, por ejemplo "uploads/portadas/abc123.jpg"
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean eliminarArchivo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            return false;
        }

        try {
            Path ruta = Paths.get(rutaArchivo);
            if (!ruta.isAbsolute()) {
                ruta = Paths.get("").toAbsolutePath().resolve(rutaArchivo);
            }

            if (Files.exists(ruta)) {
                Files.delete(ruta);
                return true;
            }

            return false;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el archivo", e);
        }
    }

    /**
     * Extrae la extensión de un nombre de archivo (con punto incluido y en minúsculas)
     */
    public String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo no tiene una extensión válida.");
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".")).toLowerCase();
    }

    public String obtenerExtensionSinPunto(String nombreArchivo) {
        String extensionConPunto = obtenerExtension(nombreArchivo);
        return extensionConPunto.substring(1);
    }
}
