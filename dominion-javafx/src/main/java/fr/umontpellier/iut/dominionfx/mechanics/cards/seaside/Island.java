package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.IslandState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Île (Island)
 * <p>
 * 2 VP
 * Placez cette carte et une carte de votre main sur votre plateau Île (Island
 * Mat).
 */
public class Island extends ActionCard {
    public Island() {
        super("Island", 4);
        addType(CardType.VICTORY);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        if (p.getCardsInHand().isEmpty()) {
            p.moveToIslandMat(this);
            return CompletableFuture.completedFuture(null);
        }
        IslandState state = new IslandState(p);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }

    @Override
    public int getVictoryValue() {
        return 2;
    }
}
