package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations.SailorStartOfTurnState;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.SailorAndDurationGainedState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Navigatrice (Sailor)
 * <p>
 * +1 Action
 * Une fois durant ce tour, quand vous recevez une carte Durée (Duration),
 * vous pouvez la jouer.
 * Au début de votre prochain tour, +2 Pièces et vous pouvez écarter une carte
 * de votre main.
 */
public class Sailor extends ActionCard {
    private boolean canPlayDuration = false;

    public Sailor() {
        super("Sailor", 4);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void>  play(Player p) {
        canPlayDuration = true;
        p.incrementActions(1);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> onCleanup(Player p) {
        canPlayDuration = false;
        return super.onCleanup(p);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.incrementMoney(2);
        SailorStartOfTurnState phase = new SailorStartOfTurnState(p, this);
        p.setCurrentState(phase);
        return phase.getCompletionFuture();
    }

    @Override
    public CompletableFuture<Void> onPlayerGainCard(Player p, Card gainedCard, Player owner) {
        if (canPlayDuration && gainedCard.hasType(CardType.DURATION) && p == owner) {
            SailorAndDurationGainedState phase = new SailorAndDurationGainedState(p, gainedCard, this);
            p.setCurrentState(phase);
            return phase.getCompletionFuture();
        }
        return CompletableFuture.completedFuture(null);
    }

    public void cannotPlayDurationAnyMore() {
        canPlayDuration = false;
    }
}