package fr.umontpellier.iut.dominionfx.mechanics.cards.common;

import fr.umontpellier.iut.dominionfx.mechanics.cards.VictoryCard;

/**
 * Carte Province
 * <p>
 * 6 VP
 */
public class Province extends VictoryCard {
    public Province() {
        super("Province", 8);
    }

    @Override
    public int getVictoryValue() {
        return 6;
    }

    @Override
    public boolean canBeInPlay() {
        return false;
    }
}