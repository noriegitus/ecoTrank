package ecotrack.services;
import java.io.*;


public class ServicioPersistencia {

    public static void guardarEstado(Object sistema, String rutaArchivo) {
        try {
            // Crear carpetas si no existen
            File archivo = new File(rutaArchivo);
            if (archivo.getParentFile() != null) archivo.getParentFile().mkdirs();

            // Guardar el objeto
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
                oos.writeObject(sistema);
                System.out.println("Datos guardados en: " + rutaArchivo);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public static SistemaEcoTrack cargarEstado(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object data = ois.readObject();
            if (data instanceof SistemaEcoTrack) {
                return (SistemaEcoTrack) data;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
        }
        return null;
    }
}