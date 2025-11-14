package com.backend.sigclc.Service.Archivos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IArchivosService {
    public String guardarArchivo(MultipartFile archivo, String carpetaDestino, List<String> tiposPermitidos);
    public boolean eliminarArchivo(String rutaArchivo);
    public String obtenerExtension(String nombreArchivo);
    public String obtenerExtensionSinPunto(String nombreArchivo);
}
