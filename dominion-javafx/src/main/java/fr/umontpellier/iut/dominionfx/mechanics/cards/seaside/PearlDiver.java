package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.PearlDiverState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Plongeur de perles (Pearl Diver)
 * <p>
 * +1 Carte
 * +1 Action
 * Consultez la carte du bas de votre pioche. Vous pouvez la placer sur le haut.
 */
public class PearlDiver extends ActionCard {

    public PearlDiver() {
        super("Pearl Diver", 2);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand(1);
        p.incrementActions(1);
        Card c = p.getBottomCardOfDeck();
        if (c != null) {
            c.moveTo(p.getGame().getTemporaryCards()); // retire de draw, place dans temporaryCards
            PearlDiverState phase = new PearlDiverState(p, c);
            p.setCurrentState(phase);
            return p.getCurrentState().getCompletionFuture();
        }
        return CompletableFuture.completedFuture(null);
    }
}
