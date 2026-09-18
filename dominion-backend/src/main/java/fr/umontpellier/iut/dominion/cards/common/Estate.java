package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Domaine (Estate)
 * <p>
 * 1 VP
 */
public class Estate extends Card {
    public Estate() {
        super("Estate", 2, CardType.VICTORY);
    }

    @Override
    public int getVictoryValue() {
        return 1;
    }
}