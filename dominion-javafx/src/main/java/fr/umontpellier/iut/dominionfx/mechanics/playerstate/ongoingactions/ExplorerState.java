package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.List;

public class ExplorerState extends OnGoingActionPhase {
    public ExplorerState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("You may reveal a Province or skip");
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getProvincesInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            currentPlayer.gainTreasure("Gold");
            complete();
        }
    }

    @Override
    public void skip() {
        currentPlayer.gainTreasure("Silver");
        complete();
    }
}
