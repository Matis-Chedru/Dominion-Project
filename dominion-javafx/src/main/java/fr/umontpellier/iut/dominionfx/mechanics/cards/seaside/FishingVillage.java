package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Village de pêcheurs (Fishing Village)
 * <p>
 * +2 Actions
 * +1 Pièce
 * Au début de votre prochain tour, +1 Action et +1 Pièce.
 */
public class FishingVillage extends ActionCard {
    public FishingVillage() {
        super("Fishing Village", 3);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementActions(2);
        p.incrementMoney(1);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.incrementActions(1);
        p.incrementMoney(1);
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}