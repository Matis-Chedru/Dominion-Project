package fr.umontpellier.iut.dominionfx.views;

import fr.umontpellier.iut.dominionfx.ICard;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * Cette classe représente la vue d'une carte.
 */
public class CardView extends Button {

    public CardView(ICard card) {
        String filename = card.getName().replace(" ", "") + ".jpg";
        String imagePath = "images/cards/" + filename;

        InputStream stream = getClass().getClassLoader().getResourceAsStream(imagePath);

        if (stream != null) {
            Image image = new Image(stream);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            setGraphic(imageView);
            setStyle("-fx-background-color: transparent; -fx-padding: 2; -fx-cursor: hand;");
        } else {
            setText(card.getName());
        }
    }
}