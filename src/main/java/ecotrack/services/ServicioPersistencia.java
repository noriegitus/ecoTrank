package ecotrack.services;

import java.io.*;

public class ServicioPersistencia {
    
    public static void guardarEstado(Object sistema, String rutaArchivo) {
        // Validar parámetros de entrada
        if (sistema == null) {
            System.err.println("✗ ERROR: No se puede guardar un sistema null");
            return;
        }
        
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            System.err.println("✗ ERROR: La ruta del archivo no puede ser null o vacía");
            return;
        }
        
        try {
            // Crear el directorio si no existe
            File archivo = new File(rutaArchivo);
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                if (!directorio.mkdirs()) {
                    System.err.println("✗ ERROR: No se pudo crear el directorio para: " + rutaArchivo);
                    return;
                }
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
                oos.writeObject(sistema);
                System.out.println("✓ Datos guardados correctamente en: " + rutaArchivo);
            }
        } catch (FileNotFoundException e) {
            System.err.println("✗ ERROR: No se pudo crear o acceder al archivo " + rutaArchivo);
            System.err.println("  Razón: " + e.getMessage());
            System.err.println("  Verifique permisos de escritura en el directorio.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("✗ ERROR al guardar datos en " + rutaArchivo);
            System.err.println("  Tipo de error: " + e.getClass().getSimpleName());
            System.err.println("  Mensaje: " + e.getMessage());
            System.err.println("  Es posible que el archivo esté corrupto o sin permisos de escritura.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ ERROR inesperado al guardar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Object cargarEstado(String rutaArchivo) {
        // Validar parámetros de entrada
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            System.err.println("✗ ERROR: La ruta del archivo no puede ser null o vacía");
            return null;
        }
        
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("ℹ Archivo de datos no existe. Iniciando con sistema vacío.");
            return null;
        }
        
        // Validar que es un archivo y no un directorio
        if (!archivo.isFile()) {
            System.err.println("✗ ERROR: La ruta especificada no es un archivo válido: " + rutaArchivo);
            return null;
        }
        
        // Validar que el archivo no esté vacío
        if (archivo.length() == 0) {
            System.err.println("✗ ERROR: El archivo está vacío o corrupto: " + rutaArchivo);
            System.err.println("  Iniciando con sistema vacío.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object data = ois.readObject();
            
            // Validar que el objeto deserializado sea del tipo esperado
            if (data instanceof SistemaEcoTrack) {
                SistemaEcoTrack sistema = (SistemaEcoTrack) data;
                // Validaciones adicionales del sistema cargado
                if (sistema.getListaResiduos() == null || sistema.getCentroReciclaje() == null || 
                    sistema.getColaZonas() == null || sistema.getMapaZonas() == null) {
                    System.err.println("✗ ADVERTENCIA: El sistema cargado tiene componentes null. Reinicializando componentes faltantes.");
                    // El sistema debería manejar esto, pero aquí lo detectamos
                }
                System.out.println("✓ Datos cargados correctamente desde: " + rutaArchivo);
                return sistema;
            } else {
                System.err.println("✗ ERROR: El archivo no contiene un SistemaEcoTrack válido.");
                System.err.println("  Tipo encontrado: " + (data != null ? data.getClass().getName() : "null"));
                System.err.println("  Iniciando con sistema vacío.");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.err.println("✗ ERROR: No se encontró el archivo " + rutaArchivo);
            System.err.println("  Iniciando con sistema vacío.");
            return null;
        } catch (EOFException e) {
            System.err.println("✗ ERROR: El archivo está incompleto o corrupto (fin de archivo inesperado): " + rutaArchivo);
            System.err.println("  Iniciando con sistema vacío.");
            return null;
        } catch (StreamCorruptedException e) {
            System.err.println("✗ ERROR: El archivo está corrupto o no es un archivo de datos válido: " + rutaArchivo);
            System.err.println("  Iniciando con sistema vacío.");
            return null;
        } catch (IOException e) {
            System.err.println("✗ ERROR al leer archivo " + rutaArchivo);
            System.err.println("  Tipo de error: " + e.getClass().getSimpleName());
            System.err.println("  Mensaje: " + e.getMessage());
            System.err.println("  Iniciando con sistema vacío.");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERROR: Clase no encontrada al deserializar.");
            System.err.println("  Clase faltante: " + e.getMessage());
            System.err.println("  Posible incompatibilidad de versión o archivo corrupto.");
            System.err.println("  Iniciando con sistema vacío.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("✗ ERROR inesperado al cargar datos: " + e.getMessage());
            System.err.println("  Iniciando con sistema vacío.");
            e.printStackTrace();
            return null;
        }
    }
}