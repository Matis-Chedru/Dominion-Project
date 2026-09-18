package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class AmbassadorRevealState extends OnGoingActionPhase {

    private final StringProperty revealedCardName;
    private final AttackCard ambassadorCard;

    public AmbassadorRevealState(Player currentPlayer, AttackCard ambassadorCard) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Reveal a card from your hand");
        revealedCardName = new SimpleStringProperty();
        this.ambassadorCard = ambassadorCard;
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            revealedCardName.setValue(cardName);
            currentPlayer.setCurrentState(new AmbassadorReturnToSupplyState(currentPlayer, cardName, ambassadorCard));
        }
    }

    public StringProperty revealedCardNameProperty() {
        return revealedCardName;
    }

    public void skip() {
    }
}