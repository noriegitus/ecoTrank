package ecotrack.main;

import ecotrack.logica.Residuo;
import ecotrack.services.SistemaEcoTrack;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SecondaryController implements SistemaInyectable {
    private SistemaEcoTrack sistema;

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
    }
}

