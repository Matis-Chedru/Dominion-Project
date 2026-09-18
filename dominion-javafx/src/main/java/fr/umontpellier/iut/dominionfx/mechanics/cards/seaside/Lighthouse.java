package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Phare (Lighthouse)
 * <p>
 * +1 Action
 * Maintenant et au début de votre prochain tour, +1 Pièce.
 * D'ici là, les cartes Attaque jouées par vos adversaires ne vous affectent
 * pas.
 */
public class Lighthouse extends ActionCard {
    private boolean isProtection = false;

    public Lighthouse() {
        super("Lighthouse", 2);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementActions(1);
        p.incrementMoney(1);
        isProtection = true;
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.incrementMoney(1);
        isProtection = false;
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean isProtectionFromAttack() {
        return isProtection;
    }
}