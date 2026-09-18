package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Bazar (Bazaar)
 * <p>
 * +1 Carte
 * +2 Actions
 * +1 Pièce
 */
public class Bazaar extends ActionCard {
    public Bazaar() {
        super("Bazaar", 5);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand();
        p.incrementActions(2);
        p.incrementMoney(1);
        return CompletableFuture.completedFuture(null);
    }
}
