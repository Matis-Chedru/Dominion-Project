package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.DominionIHM;
import fr.umontpellier.iut.dominionfx.IPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
/**
 * Cette classe définit les éléments à afficher lorsque la partie est terminée.
 * Elle peut proposer de recommencer une nouvelle partie.
 */
public class ScoresView extends VBox {

    private DominionIHM ihm;

    @FXML
    private VBox scoresBox;

    @FXML
    private Button quitButton;

    public ScoresView(DominionIHM ihm) {
        this.ihm = ihm;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Scores.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        quitButton.setOnMouseClicked(e -> ihm.stopGame());

        List<IPlayer> players = DominionIHM.getGame().getPlayers();

        for (IPlayer player : players) {
            Label scoreLabel = new Label(player.getName() + " : " + player.getVictoryPoints() + " points de victoire");
            scoreLabel.getStyleClass().add("texte-standard");
            scoresBox.getChildren().add(scoreLabel);
        }
    }
}