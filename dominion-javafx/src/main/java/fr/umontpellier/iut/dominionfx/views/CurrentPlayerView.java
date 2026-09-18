package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.DominionIHM;
import fr.umontpellier.iut.dominionfx.ICard;
import fr.umontpellier.iut.dominionfx.IPlayer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CurrentPlayerView extends VBox {

    private ObjectProperty<? extends IPlayer> currentPlayer;

    @FXML private Label playerName;
    @FXML private HBox handPane;
    @FXML private HBox gamePane;

    // Nouveaux conteneurs pour les Mats
    @FXML private HBox islandPane;
    @FXML private HBox nativePane;

    @FXML private Label money;
    @FXML private Label draw;
    @FXML private Label discard;
    @FXML private Label actions;
    @FXML private Label buys;
    @FXML private Label pirateship;

    @FXML private Button playTreasure;
    @FXML private HBox yesNoPane;
    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private HBox temporaryPane;
    @FXML private VBox temporarySection;

    public CurrentPlayerView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/CurrentPlayer.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ListChangeListener<ICard> handListener = change -> updatePane(change, handPane, true);
    private ListChangeListener<ICard> inPlayListener = change -> updatePane(change, gamePane, false);
    private ListChangeListener<ICard> islandListener = change -> updatePane(change, islandPane, false);
    private ListChangeListener<ICard> nativeListener = change -> updatePane(change, nativePane, false);

    private void updatePane(ListChangeListener.Change<? extends ICard> change, HBox pane, boolean isClickable) {
        while (change.next()) {
            if (change.wasAdded()) {
                for (ICard card : change.getAddedSubList()) {
                    CardView cardView = new CardView(card);
                    cardView.setUserData(card);
                    if (isClickable || pane == temporaryPane) {
                        cardView.setOnMouseClicked(event -> {
                            if (pane == temporaryPane) {
                                notifyTemporaryChoice(card.getName());
                            }
                            else if (currentPlayer != null && currentPlayer.get() != null) {
                                currentPlayer.get().cardInHandWasChosen(card.getName());
                            }
                        });
                    }
                    pane.getChildren().add(cardView);
                }
            }
            if (change.wasRemoved()) {
                for (ICard card : change.getRemoved()) {
                    pane.getChildren().removeIf(node -> node.getUserData() == card);
                }
            }
        }
    }

    private ChangeListener<IPlayer> currentPlayerChangeListener = (observable, oldValue, newValue) -> {
        if (oldValue != null) {
            oldValue.getHand().removeListener(handListener);
            oldValue.getInPlay().removeListener(inPlayListener);
            oldValue.getIslandMat().removeListener(islandListener);
            oldValue.getNativeVillageMat().removeListener(nativeListener);

            money.textProperty().unbind();
            actions.textProperty().unbind();
            buys.textProperty().unbind();
            draw.textProperty().unbind();
            discard.textProperty().unbind();
            pirateship.textProperty().unbind();
            if (yesNoPane != null) yesNoPane.visibleProperty().unbind();

            playerName.setText("");
            handPane.getChildren().clear();
            gamePane.getChildren().clear();
            islandPane.getChildren().clear();
            nativePane.getChildren().clear();
            if (yesNoPane != null) yesNoPane.setVisible(false);
        }

        if (newValue != null) {
            playerName.setText(newValue.getName());
            money.textProperty().bind(newValue.moneyProperty().asString("Money : %d"));
            actions.textProperty().bind(Bindings.concat(" Actions : ", newValue.numberOfActionsProperty()));
            buys.textProperty().bind(Bindings.concat(" Buys : ", newValue.numberOfBuysProperty()));
            draw.textProperty().bind(Bindings.concat(" Draw : ", Bindings.size(newValue.getDraw())));
            discard.textProperty().bind(Bindings.concat(" Discard : ", Bindings.size(newValue.getDiscard())));
            pirateship.textProperty().bind(Bindings.concat("Pirate Ship : ", newValue.pirateShipCounterProperty()));

            if (yesNoPane != null) yesNoPane.visibleProperty().bind(newValue.waitForYesOrNoProperty());

            refreshPane(handPane, newValue.getHand(), true);
            refreshPane(gamePane, newValue.getInPlay(), false);
            refreshPane(islandPane, newValue.getIslandMat(), true);
            refreshPane(nativePane, newValue.getNativeVillageMat(), true);

            newValue.getHand().addListener(handListener);
            newValue.getInPlay().addListener(inPlayListener);
            newValue.getIslandMat().addListener(islandListener);
            newValue.getNativeVillageMat().addListener(nativeListener);
        }
    };

    private void refreshPane(HBox pane, Iterable<? extends ICard> cards, boolean isClickable) {
        pane.getChildren().clear();
        for (ICard card : cards) {
            CardView cv = new CardView(card);
            cv.setUserData(card);
            if (isClickable) {
                cv.setOnMouseClicked(event -> {
                    if (currentPlayer != null && currentPlayer.get() != null) {
                        currentPlayer.get().cardInHandWasChosen(card.getName());
                    }
                });
            }
            pane.getChildren().add(cv);
        }
    }

    public void bindCurrentPlayer(ObjectProperty<? extends IPlayer> currentPlayerProperty) {
        this.currentPlayer = currentPlayerProperty;
        this.currentPlayer.addListener(currentPlayerChangeListener);
        if (this.currentPlayer.get() != null) {
            currentPlayerChangeListener.changed(currentPlayerProperty, null, this.currentPlayer.get());
        }
    }

    public void createBindings() {
        playTreasure.setOnMouseClicked(putTreasureInPlay);
        if (yesNoPane != null) {
            yesNoPane.managedProperty().bind(yesNoPane.visibleProperty());
            yesButton.setOnMouseClicked(e -> {
                if (currentPlayer != null && currentPlayer.get() != null) currentPlayer.get().answer("Yes");
            });
            noButton.setOnMouseClicked(e -> {
                if (currentPlayer != null && currentPlayer.get() != null) currentPlayer.get().answer("No");
            });
            DominionIHM.getGame().temporaryCardsProperty().addListener((ListChangeListener<ICard>) change -> {
                updatePane(change, temporaryPane, true);
                boolean hasCards = !DominionIHM.getGame().temporaryCardsProperty().isEmpty();
                temporarySection.setVisible(hasCards);
                temporarySection.setManaged(hasCards);
            });
        }
    }

    public void initialize() {
        createBindings();
    }

    public void notifyTemporaryChoice(String cardName) {
        DominionIHM.getGame().temporaryCardWasChosen(cardName);
    }

    private EventHandler<? super MouseEvent> putTreasureInPlay = mouseEvent -> {
        if (currentPlayer != null && currentPlayer.get() != null) {
            currentPlayer.get().playTreasuresWasChosen();
        }
    };
}