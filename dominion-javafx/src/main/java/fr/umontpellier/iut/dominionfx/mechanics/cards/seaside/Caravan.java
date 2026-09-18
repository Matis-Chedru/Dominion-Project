package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Caravane (Caravan)
 * <p>
 * +1 Carte
 * +1 Action
 * Au début de votre prochain tour, +1 Carte.
 */
public class Caravan extends ActionCard {
    public Caravan() {
        super("Caravan", 4);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand();
        p.incrementActions(1);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.drawToHand();
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
