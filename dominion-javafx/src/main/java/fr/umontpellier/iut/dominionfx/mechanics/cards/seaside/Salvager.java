package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.SalvagerState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Sauveteur (Salvager)
 * <p>
 * +1 Achat
 * Écartez une carte de votre main. +1 Pièce par Pièce de son coût.
 */
public class Salvager extends ActionCard {
    public Salvager() {
        super("Salvager", 4);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementBuys(1);
        if (p.getCardsInHand().isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        SalvagerState state = new SalvagerState(p);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }
}