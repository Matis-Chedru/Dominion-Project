package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.List;

public class SmugglersState extends OnGoingActionPhase {

    private final List<String> availableChoices;

    public SmugglersState(Player currentPlayer, List<String> availableChoices) {
        super(currentPlayer);
        this.availableChoices = availableChoices;
        getGame().instructionProperty().setValue("Choose a card to gain");
    }

    @Override
    public void supplyCardWasChosen(String cardName) {
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card gainedCard = currentPlayer.getCardFromSupply(cardName);
            currentPlayer.gainToDiscard(gainedCard);
            complete();
        }
    }

    @Override
    public void skip() {}
}
