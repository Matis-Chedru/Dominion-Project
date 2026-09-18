package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

public class NativeVillageState extends OnGoingActionPhase {

    public NativeVillageState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Choose Yes to add to Mat or No to take from Mat");
        currentPlayer.setWaitForYesOrNo(true);
    }

    @Override
    public void answer(String choice) {
        currentPlayer.setWaitForYesOrNo(false);
        if (choice.equals("Yes")) {
            if (!currentPlayer.getDraw().isEmpty()) {
                Card c = currentPlayer.getCardFromDeck();
                currentPlayer.moveToNativeVillageMat(c);
            }
        } else {
            currentPlayer.moveToHand(currentPlayer.getCardsOnNativeVillageMat());
        }
        complete();
    }

    @Override
    public void skip() {
    }
}
