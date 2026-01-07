package ecotrack.services;

import java.io.*;

public class ServicioPersistencia {
    
    public static void guardarEstado(Object sistema, String rutaArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(sistema);
            System.out.println("Guardado en: " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object cargarEstado(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}