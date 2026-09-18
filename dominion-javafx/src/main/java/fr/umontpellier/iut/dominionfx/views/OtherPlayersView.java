package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.ICard;
import fr.umontpellier.iut.dominionfx.IGame;
import fr.umontpellier.iut.dominionfx.IPlayer;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class OtherPlayersView extends HBox {

    public OtherPlayersView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OtherPlayers.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGame(IGame game) {
        this.getChildren().clear();
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);

        for (IPlayer player : game.getPlayers()) {
            VBox playerBox = new VBox();
            playerBox.setAlignment(Pos.TOP_CENTER);
            playerBox.setSpacing(5);
            playerBox.setPadding(new Insets(10));
            playerBox.getStyleClass().add("other-player-box");

            playerBox.setMinSize(130, 150);
            playerBox.setMaxSize(130, 150);

            Label nameLabel = new Label(player.getName());
            nameLabel.getStyleClass().add("other-player-name");

            Label statsLabel = new Label();
            statsLabel.textProperty().bind(Bindings.concat(
                    "Hand : ", Bindings.size(player.getHand()),
                    "\nDraw : ", Bindings.size(player.getDraw()),
                    "\nDiscard : ", Bindings.size(player.getDiscard())
            ));
            statsLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-alignment: center;");
            statsLabel.setAlignment(Pos.CENTER);

            HBox inPlayPane = new HBox();
            inPlayPane.setAlignment(Pos.CENTER);
            inPlayPane.setSpacing(2);
            inPlayPane.setStyle("-fx-scale-x: 0.5; -fx-scale-y: 0.5;");

            player.getInPlay().addListener((ListChangeListener<ICard>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (ICard card : change.getAddedSubList()) {
                            CardView cardView = new CardView(card);
                            cardView.setUserData(card);
                            inPlayPane.getChildren().add(cardView);
                        }
                    }
                    if (change.wasRemoved()) {
                        for (ICard card : change.getRemoved()) {
                            inPlayPane.getChildren().removeIf(node -> node.getUserData() == card);
                        }
                    }
                }
            });

            playerBox.getChildren().addAll(nameLabel, statsLabel, inPlayPane);
            playerBox.visibleProperty().bind(game.currentPlayerProperty().isNotEqualTo(player));
            playerBox.managedProperty().bind(playerBox.visibleProperty());

            this.getChildren().add(playerBox);
        }
    }
}