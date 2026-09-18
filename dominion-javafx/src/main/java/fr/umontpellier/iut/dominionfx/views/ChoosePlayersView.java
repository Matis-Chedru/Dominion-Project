package fr.umontpellier.iut.dominionfx.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir les noms des joueurs de la partie.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class ChoosePlayersView extends Stage {

    @FXML
    private ComboBox<Integer> choixJoueur;

    @FXML
    TextField p1, p2, p3, p4, p5, p6;

    @FXML
    private Button debut;

    private final ObservableList<String> playersNames;

    public ChoosePlayersView() {
        playersNames = FXCollections.observableArrayList();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ChoosePlayer.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> playersNamesProperty() {
        return playersNames;
    }

    public String[] getPlayersNames() {
        return playersNames.toArray(new String[0]);
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setPlayersNamesDefinedListener(ListChangeListener<String> whenPlayersNamesAreDefined) {
        playersNames.addListener(whenPlayersNamesAreDefined);
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    @FXML
    protected void setPlayersNamesList() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNumberOfPlayers(); i++) {
            String name = getPlayerByIndex(i);
            if (name == null || name.isEmpty()) {
                tempNamesList.clear();
                break;
            } else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            playersNames.clear();
            playersNames.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNumberOfPlayers() {
        return choixJoueur.getValue();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     *
     * @param playerNumber : le numéro du participant
     */
    protected String getPlayerByIndex(int playerNumber) {
        switch (playerNumber) {
            case 1: return p1.getText();
            case 2: return p2.getText();
            case 3: return p3.getText();
            case 4: return p4.getText();
            case 5: return p5.getText();
            case 6: return p6.getText();
            default: return "";
        }
    }

    @FXML
    public void initialize() {
        for (int i = 2; i <= 6; i++) {
            choixJoueur.getItems().add(i);
        }
        choixJoueur.setValue(2);
        setPlayers(2);

        choixJoueur.valueProperty().addListener((observable, oldValue, newValue) -> {
            setPlayers((int) newValue);
        });

        debut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setPlayersNamesList();
            }
        });
    }


    public void setPlayers(int n) {
        p1.setDisable(false);
        p2.setDisable(false);
        p3.setDisable(n < 3);
        p4.setDisable(n < 4);
        p5.setDisable(n < 5);
        p6.setDisable(n < 6);
    }
}