package fr.umontpellier.iut.dominionfx.mechanics.cards.common;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.TreasureCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Or (Gold)
 * <p>
 * 3 Pièces
 */
public class Gold extends TreasureCard {
    public Gold() {
        super("Gold", 6);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementMoney(3);
        p.incrementNbSilverOrGoldPlayed();
        return CompletableFuture.completedFuture(null);
    }
}
