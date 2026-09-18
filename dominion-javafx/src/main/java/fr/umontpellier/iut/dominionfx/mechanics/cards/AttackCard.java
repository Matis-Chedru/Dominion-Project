package fr.umontpellier.iut.dominionfx.mechanics.cards;

import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Les cartes Attaque
 * Rmq : les cartes Attaque sont toutes des cartes Action
 */
public abstract class AttackCard extends ActionCard {

    protected final CompletableFuture<Void> attackCardFuture = new CompletableFuture<>();
    /**
     * Constructeur
     */
    public AttackCard(String name, int cost) {
        super(name, cost);
    }

    public abstract CompletableFuture<Void> action(Player p);

    public abstract CompletableFuture<Void> attack(Player p, Player target);

    public void complete() {
        attackCardFuture.complete(null);
    }

    public CompletableFuture<Void> getCompletionFuture() {
        return attackCardFuture;
    }

    public void afterAttack(Player p) {
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        return action(p)
                .thenCompose(v -> attackAll(p))
                .thenRun(() -> afterAttack(p))
                .thenRun(() -> p.getCurrentState().complete());
    }

    private CompletableFuture<Void> attackAll(Player p) {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (Player target : p.getOtherPlayers()) {
            if (!target.isProtectedFromAttack()) {
                future = future.thenCompose(v -> attack(p, target));
            }
        }
        return future;
    }

}