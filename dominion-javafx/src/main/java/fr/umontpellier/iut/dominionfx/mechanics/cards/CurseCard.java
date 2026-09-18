package fr.umontpellier.iut.dominionfx.mechanics.cards;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Les cartes Malédiction
 */
public abstract class CurseCard extends Card {

    /**
     * Constructeur
     */
    public CurseCard(String name, int cost) {
        super(name, cost);
        addType(CardType.CURSE);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean canBeInPlay() {
        return false;
    }
}