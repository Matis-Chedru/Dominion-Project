package fr.umontpellier.iut.dominionfx.mechanics.cards.common;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.TreasureCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Cuivre (Copper)
 * <p>
 * 1 Pièce
 */
public class Copper extends TreasureCard {
    public Copper() {
        super("Copper", 0);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementMoney(1);
        return CompletableFuture.completedFuture(null);
    }
}
