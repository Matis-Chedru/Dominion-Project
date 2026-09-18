package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.Haven;

import java.util.List;

public class HavenState extends OnGoingActionPhase {

    private final Haven haven;

    public HavenState(Player currentPlayer, Haven haven) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Set aside a card from your hand");
        this.haven = haven;
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            haven.endPlay(currentPlayer, cardName);
        }
    }

    @Override
    public void skip() {
    }
}