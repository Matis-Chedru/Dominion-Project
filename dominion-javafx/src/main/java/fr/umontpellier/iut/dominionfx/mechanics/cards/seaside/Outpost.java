package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Game;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Avant-poste (Outpost)
 * <p>
 * Piochez seulement 3 cartes pour votre prochaine main.
 * Jouez un tour supplémentaire après celui-ci (mais pas un troisième
 * consécutif).
 */
public class Outpost extends ActionCard {
    public Outpost() {
        super("Outpost", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.setNbCardsToDrawAtCleanup(3);
        setHasDurationEffect(true);
        Game game = p.getGame();
        if (game.getPreviousTurnPlayer() != p) {
            game.setSamePlayerShouldPlayExtraTurn(true);
        }
        return CompletableFuture.completedFuture(null);

    }

    @Override
    public CompletableFuture<Void> onCleanup(Player p) {
        // si le joueur ne peut pas rejouer un tour, alors Outpost devrait être
        // défaussé à la fin du tour suivant
        CompletableFuture<Void> future = super.onCleanup(p);
        setHasDurationEffect(false);
        return future;
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
