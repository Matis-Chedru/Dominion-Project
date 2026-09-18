package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.WarehouseState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Entrepôt (Warehouse)
 * <p>
 * +3 Cartes
 * +1 Action
 * Défaussez 3 cartes.
 */
public class Warehouse extends ActionCard {

    public Warehouse() {
        super("Warehouse", 3);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand(3);
        p.incrementActions(1);
        WarehouseState state = new WarehouseState(p);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }
}
