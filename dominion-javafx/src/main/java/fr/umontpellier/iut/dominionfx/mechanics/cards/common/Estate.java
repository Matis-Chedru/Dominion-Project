package fr.umontpellier.iut.dominionfx.mechanics.cards.common;

import fr.umontpellier.iut.dominionfx.mechanics.cards.VictoryCard;

/**
 * Carte Domaine (Estate)
 * <p>
 * 1 VP
 */
public class Estate extends VictoryCard {
    public Estate() {
        super("Estate", 2);
    }

    @Override
    public int getVictoryValue() {
        return 1;
    }

    @Override
    public boolean canBeInPlay() {
        return false;
    }
}