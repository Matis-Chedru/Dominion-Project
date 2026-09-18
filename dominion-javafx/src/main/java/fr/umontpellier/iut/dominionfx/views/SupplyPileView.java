package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.DominionIHM;
import fr.umontpellier.iut.dominionfx.mechanics.SupplyPile;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class SupplyPileView extends VBox {

    @FXML
    private TilePane treasurePiles;
    @FXML
    private TilePane kingdomPiles;
    @FXML
    private TilePane victoryPiles;
    @FXML
    private TilePane cursePiles;

    public SupplyPileView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SupplyPile.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBinding(){
        for(SupplyPile pile : DominionIHM.getGame().getSupplyPiles()){
            switch (pile.getName()){
                case "Copper", "Silver", "Gold":
                    treasurePiles.getChildren().add(createNodeInSupply(pile));
                    break;
                case "Estate", "Duchy", "Province":
                    victoryPiles.getChildren().add(createNodeInSupply(pile));
                    break;
                case "Curse":
                    cursePiles.getChildren().add(createNodeInSupply(pile));
                    break;
                default:
                    kingdomPiles.getChildren().add(createNodeInSupply(pile));
                    break;
            }
        }
    }

    public Node createNodeInSupply(SupplyPile pile) {
        CardView card = new CardView(pile.getFirst());
        card.setScaleX(1.1);
        card.setScaleY(1.1);

        card.setOnAction(e ->
                DominionIHM.getGame().supplyCardWasChosen(pile.getName()));

        Label nb = new Label();
        nb.getStyleClass().add("nbStyle");
        nb.textProperty().bind(pile.sizeProperty().asString());
        Rectangle rec = new Rectangle(30, 30);
        rec.getStyleClass().add("recNb");
        StackPane sp = new StackPane();
        sp.getChildren().addAll(rec, nb);

        Label embargoLabel = new Label();
        embargoLabel.getStyleClass().add("embargoStyle");
        embargoLabel.textProperty().bind(pile.nbEmbargoTokensProperty().asString());
        embargoLabel.visibleProperty().bind(pile.nbEmbargoTokensProperty().greaterThan(0));

        StackPane sp2 = new StackPane(card, sp, embargoLabel);
        StackPane.setAlignment(embargoLabel, javafx.geometry.Pos.TOP_LEFT);

        sp.setMouseTransparent(true);
        embargoLabel.setMouseTransparent(true);

        card.setOnMouseEntered(e -> {
            card.setScaleX(2.5);
            card.setScaleY(2.5);
            sp2.setViewOrder(-1);
            sp2.getParent().setViewOrder(-1);
        });

        card.setOnMouseExited(e -> {
            card.setScaleX(1.1);
            card.setScaleY(1.1);
            sp2.setViewOrder(0);
            sp2.getParent().setViewOrder(0);
        });

        return sp2;
    }

//    private EventHandler<? super MouseEvent> hoverKingdom = mouseEvent -> {
//        mouseEvent.getTarget().
//    };

    @FXML
    public void initialize(){
        createBinding();
    }
}