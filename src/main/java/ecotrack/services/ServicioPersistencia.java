package ecotrack.services;

import java.io.*;

public class ServicioPersistencia {
    
    public static void guardarEstado(Object sistema, String rutaArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(sistema);
            System.out.println("✓ Datos guardados correctamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("✗ ERROR al guardar datos en " + rutaArchivo + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Object cargarEstado(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("ℹ Archivo de datos no existe. Iniciando con sistema vacío.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object data = ois.readObject();
            System.out.println("✓ Datos cargados correctamente desde: " + rutaArchivo);
            return data;
        } catch (IOException e) {
            System.err.println("✗ ERROR al leer archivo " + rutaArchivo + ": " + e.getMessage());
            System.err.println("  Iniciando con sistema vacío.");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERROR: Clase no encontrada al deserializar. Posible incompatibilidad de versión.");
            System.err.println("  Iniciando con sistema vacío.");
            e.printStackTrace();
            return null;
        }
    }
}