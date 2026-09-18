package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Malédiction (Curse)
 * <p>
 * -1 VP
 */
public class Curse extends Card {
    public Curse() {
        super("Curse", 0, CardType.CURSE);
    }

    @Override
    public int getVictoryValue() {
        return -1;
    }
}