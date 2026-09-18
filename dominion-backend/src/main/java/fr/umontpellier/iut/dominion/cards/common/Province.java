package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Province
 * <p>
 * 6 VP
 */
public class Province extends Card {
    public Province() {
        super("Province", 8, CardType.VICTORY);
    }

    @Override
    public int getVictoryValue() {
        return 6;
    }
}