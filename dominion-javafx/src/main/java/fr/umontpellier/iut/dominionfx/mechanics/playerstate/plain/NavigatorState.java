package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

public class NavigatorState extends PlayerState {

    private boolean discardChoice;

    public NavigatorState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Do you want to discard all cards?");
        for (int i = 0; i < 5; i++) {
            Card c = currentPlayer.getCardFromDeck();
            if (c != null) c.moveTo(getGame().getTemporaryCards()); // retire de draw, place dans temporaryCards
        }
        discardChoice = true;
        currentPlayer.setWaitForYesOrNo(true);
    }

    @Override
    public void answer(String choice) {
        currentPlayer.setWaitForYesOrNo(false);
        discardChoice = false;
        if (choice.equals("Yes")) {
            currentPlayer.moveToDiscard(getGame().getTemporaryCards()); // auto-vide temporaryCards
            complete();
        } else {
            getGame().instructionProperty().setValue("Put cards back on deck (last on top)");
        }
    }

    @Override
    public void temporaryCardWasChosen(String cardName) {
        if (discardChoice) return;
        Card cardToPlay = getGame().getTemporaryCards().stream()
                .filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
        if (cardToPlay != null) {
            currentPlayer.moveToDraw(cardToPlay); // auto-retire de temporaryCards
            if (getGame().getTemporaryCards().isEmpty()) {
                complete();
            }
        }
    }
}
