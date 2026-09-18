package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Navire marchand (Merchant Ship)
 * <p>
 * Maintenant et au début de votre prochain tour, +Pièces.
 */
public class MerchantShip extends ActionCard {
    public MerchantShip() {
        super("Merchant Ship", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementMoney(2);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.incrementMoney(2);
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
