package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.List;

public class TidePoolsState extends DurationState {

    private int nbCardsToDiscard;

    public TidePoolsState(Player currentPlayer, Card durationCard) {
        super(currentPlayer, durationCard);
        this.nbCardsToDiscard = 2;
        getGame().instructionProperty().setValue("Discard 2 cards");
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card cardToDiscard = currentPlayer.getCardsInHand().stream().filter(c -> c.getName().equals(cardName)).findFirst().orElseThrow();
            currentPlayer.moveToDiscard(cardToDiscard);
            nbCardsToDiscard -= 1;
            if (nbCardsToDiscard == 0) {
                super.skip();
            }
            else
                getGame().instructionProperty().setValue("Discard 1 card");
        }
    }

    @Override
    public void skip() {
    }
}