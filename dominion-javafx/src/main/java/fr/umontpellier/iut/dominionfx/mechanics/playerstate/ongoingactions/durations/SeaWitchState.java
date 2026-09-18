package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.SeaWitch;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.OnGoingActionPhase;

import java.util.List;

public class SeaWitchState extends OnGoingActionPhase {

    private int nbCardsToDiscard;
    private final SeaWitch seaWitch;

    public SeaWitchState(Player currentPlayer, SeaWitch seaWitch) {
        super(currentPlayer);
        this.nbCardsToDiscard = 2;
        this.seaWitch = seaWitch;
        getGame().instructionProperty().setValue("Discard 2 cards");
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        List<String> availableChoices = currentPlayer.getNamesOfCardsInHand();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card cardToDiscard = currentPlayer.getCardFromHand(cardName);
            currentPlayer.moveToDiscard(cardToDiscard);
            nbCardsToDiscard--;
            if (nbCardsToDiscard == 0) {
                seaWitch.setHasDurationEffect(false);
                complete();
            }
            else
                getGame().instructionProperty().setValue("Discard %d card%s".formatted(nbCardsToDiscard, nbCardsToDiscard > 1 ? "s" : ""));
        }
    }

    @Override
    public void skip() {
    }
}