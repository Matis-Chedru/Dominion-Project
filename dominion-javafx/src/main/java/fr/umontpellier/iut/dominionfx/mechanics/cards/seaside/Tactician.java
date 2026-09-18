package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Tacticien (Tactician)
 * <p>
 * Si vous avez au moins une carte en main, défaussez votre main, et au debut
 * de votre prochain tour, +5 Cartes, +1 Action, et +1 Achat.
 */
public class Tactician extends ActionCard {
    public Tactician() {
        super("Tactician", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        if (!p.getCardsInHand().isEmpty()) {
            p.moveToDiscard(p.getCardsInHand());
            setHasDurationEffect(true);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.drawToHand(5);
        p.incrementActions(1);
        p.incrementBuys(1);
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
