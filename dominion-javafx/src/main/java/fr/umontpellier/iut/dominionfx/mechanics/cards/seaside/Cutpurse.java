package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Coupeur de bourse (Cutpurse)
 * <p>
 * +2 Pièces
 * Tous vos adversaires défaussent un Cuivre (Copper) (ou dévoilent une main
 * sans Cuivre).
 */
public class Cutpurse extends AttackCard {
    public Cutpurse() {
        super("Cutpurse", 4);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        p.incrementMoney(2);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        target.getCardsInHand().stream().filter(c -> c.hasName("Copper")).findFirst().ifPresent(target::moveToDiscard);
        return CompletableFuture.completedFuture(null);
    }
}
