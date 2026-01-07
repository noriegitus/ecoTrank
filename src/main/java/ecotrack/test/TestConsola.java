package ecotrack.test;

import ecotrack.estructuras.Iterator;
import ecotrack.logica.Residuo;
import ecotrack.logica.Zona;
import ecotrack.services.ComparadorPorPeso; 
import ecotrack.services.SistemaEcoTrack;
import java.util.Comparator;

public class TestConsola {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DEL SISTEMA ECOTRACK ===");
        
        // 1. Instanciar el sistema
        SistemaEcoTrack sistema = new SistemaEcoTrack();

        // ---------------------------------------------------------------
        // PRUEBA 1: CREACIÓN DE ZONAS Y MAPA HASH
        // ---------------------------------------------------------------
        System.out.println("\n[1] Creando Zonas...");
        Zona zNorte = new Zona("Norte");
        Zona zSur = new Zona("Sur");
        Zona zCentro = new Zona("Centro");

        sistema.agregarZona(zNorte);
        sistema.agregarZona(zSur);
        sistema.agregarZona(zCentro);

        // Verificación rápida del Mapa Hash (HashMap)
        if (sistema.getMapaZonas().containsKey("Norte")) {
            System.out.println("✅ Mapa de zonas funcionando: Se encontró 'Norte' en O(1).");
        } else {
            System.out.println("❌ ERROR: El mapa de zonas falló.");
        }

        // ---------------------------------------------------------------
        // PRUEBA 2: AGREGAR RESIDUOS (LISTA CIRCULAR + ACTUALIZACIÓN DE ZONA)
        // ---------------------------------------------------------------
        System.out.println("\n[2] Agregando Residuos...");
        
        // Agregamos residuos desordenados en peso
        sistema.agregarResiduo(new Residuo("R01", "Botellas", "Plástico", 50.0, "Norte", 1));
        sistema.agregarResiduo(new Residuo("R02", "Cartón", "Papel", 10.0, "Sur", 2));
        sistema.agregarResiduo(new Residuo("R03", "Vidrio Roto", "Vidrio", 80.0, "Norte", 3)); // Norte tiene mucha basura
        sistema.agregarResiduo(new Residuo("R04", "Latas", "Metal", 20.0, "Centro", 1));

        // Verificamos si la zona Norte actualizó su pendiente
        if (zNorte.getP_Pendiente() == 130) { // 50 + 80
            System.out.println("✅ Lógica de Zona actualizada correctamente (Pendiente: 130).");
        } else {
            System.out.println("❌ ERROR: La zona Norte no sumó el peso correctamente. Actual: " + zNorte.getP_Pendiente());
        }

        // ---------------------------------------------------------------
        // PRUEBA 3: ITERADOR BIDIRECCIONAL (LISTA CIRCULAR)
        // ---------------------------------------------------------------
        System.out.println("\n[3] Probando Iterador Bidireccional...");
        
        // Obtenemos TU iterador personalizado
        Iterator<Residuo> it = sistema.getListaResiduos().iterator();
        
        System.out.print("Hacia adelante: ");
        while (it.hasNext()) {
            Residuo r = it.next();
            System.out.print(r.getNombre() + " -> ");
        }
        System.out.println("(Fin vuelta)");

        // Probamos hacia atrás (Si tu lista es circular, previous debería funcionar)
        System.out.print("Hacia atrás: ");
        if (it.hasPrevious()) {
            System.out.print(it.previous().getNombre() + " -> "); // Debería ser el último
            if (it.hasPrevious()) System.out.print(it.previous().getNombre()); // Penúltimo
        }
        System.out.println();

        // ---------------------------------------------------------------
        // PRUEBA 4: ORDENAMIENTO (BUBBLE SORT)
        // ---------------------------------------------------------------
        System.out.println("\n[4] Probando Ordenamiento por Peso (Bubble Sort)...");
        System.out.println("Antes de ordenar (El primero era Botellas 50.0): " + sistema.getListaResiduos().get(0).getNombre());
        
        // Usamos un comparador inline o el archivo ComparadorPorPeso
        sistema.getListaResiduos().sort(new Comparator<Residuo>() {
            @Override
            public int compare(Residuo o1, Residuo o2) {
                return Double.compare(o1.getPeso(), o2.getPeso());
            }
        });
        
        System.out.println("Después de ordenar (El primero debe ser Cartón 10.0): " + sistema.getListaResiduos().get(0).getNombre() + " (" + sistema.getListaResiduos().get(0).getPeso() + ")");

        // ---------------------------------------------------------------
        // PRUEBA 5: COLA DE PRIORIDAD (ZONA MÁS CRÍTICA)
        // ---------------------------------------------------------------
        System.out.println("\n[5] Probando Cola de Prioridad...");
        // La utilidad es (Recolectado - Pendiente). 
        // Norte tiene pendiente 130 -> Utilidad -130 (La más urgente)
        // Sur tiene pendiente 10 -> Utilidad -10
        
        Zona zonaAtender = sistema.getColaZonas().despacharSiguienteVehiculo();
        System.out.println("El camión debe ir a: " + zonaAtender.getNombreZona());
        
        if (zonaAtender.getNombreZona().equals("Norte")) {
            System.out.println("✅ Correcto: La zona Norte es la más crítica.");
        } else {
            System.out.println("❌ ERROR: Se despachó a " + zonaAtender.getNombreZona() + " en lugar de Norte.");
        }

        // ---------------------------------------------------------------
        // PRUEBA 6: RECOLECCIÓN Y PILA (CENTRO RECICLAJE)
        // ---------------------------------------------------------------
        System.out.println("\n[6] Recolectando residuos de Norte...");
        sistema.recolectarResiduosDeZona(zonaAtender);
        
        // Verificamos que la lista ya no tenga residuos del Norte
        boolean hayDelNorte = false;
        for(Residuo r : sistema.getListaResiduos()) {
            if(r.getZona().equals("Norte")) hayDelNorte = true;
        }
        
        if (!hayDelNorte) System.out.println("✅ Residuos de Norte eliminados de la lista circular.");
        else System.out.println("❌ ERROR: Aún quedan residuos del Norte en la calle.");

        // Verificamos el Centro de Reciclaje (PILA LIFO)
        System.out.println("Verificando Centro de Reciclaje (Pila)...");
        Residuo ultimo = sistema.getCentroReciclaje().procesarResiduo();
        if (ultimo != null) {
            System.out.println("✅ Se procesó del centro: " + ultimo.getNombre());
        } else {
            System.out.println("❌ ERROR: El centro de reciclaje está vacío.");
        }
        
        // ---------------------------------------------------------------
        // PRUEBA 7: PERSISTENCIA
        // ---------------------------------------------------------------
        System.out.println("\n[7] Probando Guardado...");
        sistema.guardarDatos();
        System.out.println("✅ Datos guardados. Prueba finalizar.");
    }
}