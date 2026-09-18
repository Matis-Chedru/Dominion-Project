package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.NavigatorState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Navigateur (Navigator)
 * <p>
 * +2 Pièces
 * Consultez les 5 premières cartes de votre pioche.
 * Défaussez-les toutes ou replacez-les sur votre pioche dans l'ordre de
 * votre choix.
 */
public class Navigator extends ActionCard {

    public Navigator() {
        super("Navigator", 4);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementMoney(2);
        if (p.getDraw().isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        NavigatorState phase = new NavigatorState(p);
        p.setCurrentState(phase);
        return phase.getCompletionFuture();
    }
}