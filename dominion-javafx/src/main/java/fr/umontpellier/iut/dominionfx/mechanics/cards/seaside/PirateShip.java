package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.PirateShipAttackState;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.PirateShipChoiceState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Carte Bateau pirate (Pirate Ship)
 * <p>
 * Choisissez : +1 Pièce par jeton Pièce sur votre plateau Bateau pirate ;
 * ou tous vos adversaires dévoilent les 2 premières cartes de leur pioche,
 * écartent un Trésor (Treasure) dévoilé de votre choix et défaussent le reste,
 * et si au moins un Trésor a été écarté, placez un jeton Pièce sur votre
 * plateau Bateau pirate.
 */
public class PirateShip extends AttackCard {
    private boolean isAttacking = false;
    private boolean didTrashTreasure = false;

    public PirateShip() {
        super("Pirate Ship", 4);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        didTrashTreasure = false;
        PirateShipChoiceState phase = new PirateShipChoiceState(p, this);
        p.setCurrentState(phase);
        return phase.getCompletionFuture();
    }

    @Override
    public  CompletableFuture<Void> attack(Player p, Player target) {
        if (isAttacking) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            List<Card> revealedCards = target.drawCards(2);
            if (revealedCards.stream().anyMatch(c -> c.hasType(CardType.TREASURE))) {
                // si au moins un trésor, choisir un trésor à écarter
                PirateShipAttackState phase = new PirateShipAttackState(p, target, revealedCards, this);
                p.setCurrentState(phase);
                future = phase.getCompletionFuture();
            }
            future = future.thenRun(() -> target.moveToDiscard(revealedCards))
                    .thenRun(this::complete);
            return future;
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void afterAttack(Player p) {
        if (didTrashTreasure) {
            p.incrementPirateShipCounter();
        }
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setDidTrashTreasure(boolean didTrashTreasure) {
        this.didTrashTreasure = didTrashTreasure;
    }
}
