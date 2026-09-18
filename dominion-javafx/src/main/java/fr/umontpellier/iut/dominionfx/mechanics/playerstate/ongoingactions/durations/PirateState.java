package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.List;

public class PirateState extends DurationState {

    public PirateState(Player currentPlayer, Card durationCard) {
        super(currentPlayer, durationCard);
        getGame().instructionProperty().setValue("Gain a Treasure costing up to 6");
    }

    @Override
    public void supplyCardWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getGame().getCardsFromSupplyMatchingCondition(c -> c.getCost() <= 6);
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card gainedCard = currentPlayer.getCardFromSupply(cardName);
            currentPlayer.gainToHand(gainedCard);
            complete();
        }
    }

    @Override
    public void skip() {}
}