package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.List;

public class GhostShipState extends OnGoingActionPhase {

    private int nbCardsToDiscard;
    private final Player target;

    public GhostShipState(Player currentPlayer, Player target, int nbCardsToDiscard) {
        super(currentPlayer);
        this.nbCardsToDiscard = nbCardsToDiscard;
        this.target = target;
        getGame().instructionProperty().setValue(target.getName() + ", discard down to 3 cards");
    }

    @Override
    public void temporaryCardWasChosen(String cardName) {
        List<String> availableChoices = getGame().getTemporaryCardsNames();
        if (!availableChoices.isEmpty() && availableChoices.contains(cardName)) {
            Card cardToPlay = getGame().getTemporaryCards().stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
            target.getHand().remove(cardToPlay);
            getGame().getTemporaryCards().remove(cardToPlay);
            nbCardsToDiscard--;
            if (nbCardsToDiscard == 0) {
                getGame().getTemporaryCards().clear();
                complete();
            }
        }
    }

    @Override
    public void skip() {
    }
}