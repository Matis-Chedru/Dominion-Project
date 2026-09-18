package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.List;

public class EmbargoState extends OnGoingActionPhase {

    public EmbargoState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Choose a supply pile");
    }

    @Override
    public void supplyCardWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getGame().getAllSupplyPileNames();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            currentPlayer.getGame().addEmbargoToken(cardName);
            complete();
        }
    }

    @Override
    public void skip() {}
}
