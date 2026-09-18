package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.IGame;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class GameView extends HBox {

    private IGame game;

    @FXML
    private Label instruction;

    @FXML
    private Button skipButton;

    @FXML
    private CurrentPlayerView currentPlayerPane;

    @FXML
    private SupplyPileView supplyPileView;

    @FXML
    private OtherPlayersView otherPlayersView;

    public GameView(IGame game) {
        this.game = game;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            createBindings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBindings() {
        instruction.textProperty().bind(game.instructionProperty());
        skipButton.setOnMouseClicked(defaultSkipHandler);
        currentPlayerPane.bindCurrentPlayer(game.currentPlayerProperty());
        otherPlayersView.setGame(game);
    }



    private EventHandler<? super MouseEvent> defaultSkipHandler = mouseEvent -> {
        game.skipWasChosen();
    };
}