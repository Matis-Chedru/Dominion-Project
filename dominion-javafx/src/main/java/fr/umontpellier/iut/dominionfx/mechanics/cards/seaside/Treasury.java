package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.TreasuryState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Trésorerie (Treasury)
 * <p>
 * +1 Carte
 * +1 Action
 * +1 Pièce
 * À la fin de votre phase Achat, si vous n'avez pas reçu de carte Victoire
 * durant celle-ci, vous pouvez placer cette carte sur votre pioche.
 */
public class Treasury extends ActionCard {

    public Treasury() {
        super("Treasury", 5);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand(1);
        p.incrementActions(1);
        p.incrementMoney(1);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> onCleanup(Player p) {
        if (p.getCardsBoughtThisTurn().stream().noneMatch(c -> c.hasType(CardType.VICTORY))) {
            TreasuryState phase = new TreasuryState(p, this);
            p.setCurrentState(phase);
            return phase.getCompletionFuture();
        } else {
            return superCleanup(p);
        }
    }

    public CompletableFuture<Void> superCleanup(Player p) {
        return super.onCleanup(p);
    }
}