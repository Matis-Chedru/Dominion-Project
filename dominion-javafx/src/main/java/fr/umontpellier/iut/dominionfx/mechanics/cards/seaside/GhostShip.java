package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.GhostShipState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Vaisseau fantôme (Ghost Ship)
 * <p>
 * +2 Cartes
 * Tous vos adversaires ayant au moins 4 cartes en main placent des cartes
 * de leur main sur leur pioche jusqu'à avoir 3 cartes en main.
 */
public class GhostShip extends AttackCard {

    public GhostShip() {
        super("Ghost Ship", 5);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        p.drawToHand(2);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        if (!target.isProtectedFromAttack()) {
            int nbCardsToDiscard = target.getHand().size() - 3;
            if (nbCardsToDiscard > 0) {
                p.getGame().getTemporaryCards().addAll(target.getHand());
                GhostShipState phase = new GhostShipState(p, target, nbCardsToDiscard);
                p.setCurrentState(phase);
                return p.getCurrentState().getCompletionFuture();
            } else {
                complete();
            }
        } else {
            complete();
        }
        return attackCardFuture;
    }
}
