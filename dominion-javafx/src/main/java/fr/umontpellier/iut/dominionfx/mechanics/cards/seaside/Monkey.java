package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Singe (Monkey)
 * <p>
 * Jusqu'à votre prochain tour, quand le joueur à votre droite reçoit une
 * carte, +1 Carte.
 * Au début de votre prochain tour, +1 Carte.
 */
public class Monkey extends ActionCard {
    private boolean isActive;

    public Monkey() {
        super("Monkey", 3);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void>  play(Player p) {
        isActive = true;
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> onPlayerGainCard(Player p, Card gainedCard, Player owner) {
        if (p == owner.getOtherPlayers().getLast() && isActive) {
            owner.drawToHand();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        isActive = false;
        p.drawToHand();
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
