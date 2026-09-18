package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.Blockade;

import java.util.List;

public class BlockadeState extends OnGoingActionPhase {
    final private Blockade blockadeCard;

    public BlockadeState(Player currentPlayer, Blockade blockadeCard) {
        super(currentPlayer);
        this.blockadeCard = blockadeCard;
        getGame().instructionProperty().setValue("Gain a card costing up to 4");
    }

    @Override
    public void supplyCardWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getGame().getCardsFromSupplyMatchingCondition(c -> c.getCost() <= 4);
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            blockadeCard.endAction(cardName);
            complete();
        }
    }

    @Override
    public void skip() {
    }
}