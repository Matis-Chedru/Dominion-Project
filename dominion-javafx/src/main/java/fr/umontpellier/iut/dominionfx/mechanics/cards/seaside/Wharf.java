package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Quai (Wharf)
 * <p>
 * Maintenant et au début de votre prochain tour : +2 Cartes et +1 Achat.
 */
public class Wharf extends ActionCard {

    public Wharf() {
        super("Wharf", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand(2);
        p.incrementBuys(1);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.drawToHand(2);
        p.incrementBuys(1);
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
