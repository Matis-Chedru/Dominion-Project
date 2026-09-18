package fr.umontpellier.iut.dominionfx.mechanics.playerstate;

import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.List;

public class StartTurnState extends PlayerState {

    public StartTurnState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Action phase or skip");
    }

    @Override
    public void skip() {
        currentPlayer.endActionPhase();
        currentPlayer.endTreasurePhase();
        moveToNextPhase();
        complete();
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfPlayableCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            currentPlayer.switchToStateByCardType(cardName);
        }
    }
}
