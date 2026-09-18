package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

import java.util.List;

public class SalvagerState extends PlayerState {

    public SalvagerState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Trash a card from your hand");
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card cardToTrash = currentPlayer.getCardsInHand().stream()
                    .filter(card -> card.getName().equals(cardName))
                    .findFirst()
                    .orElseThrow();
            currentPlayer.incrementMoney(cardToTrash.getCost());
            currentPlayer.moveToTrash(cardToTrash);
            complete();
        }
    }
}
