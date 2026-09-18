package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.ExplorerState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Explorateur (Explorer)
 * <p>
 * Vous pouvez dévoiler une Province de votre main. Si vous le faites, recevez
 * un Or (Gold) en main. Sinon, recevez un Argent (Silver) en main.
 */
public class Explorer extends ActionCard {

    public Explorer() {
        super("Explorer", 5);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        ExplorerState state = new ExplorerState(p);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }
}
