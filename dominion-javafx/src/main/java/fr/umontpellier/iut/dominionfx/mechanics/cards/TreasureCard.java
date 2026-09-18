package fr.umontpellier.iut.dominionfx.mechanics.cards;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;

/**
 * Les cartes Trésor
 */
public abstract class TreasureCard extends Card {

    /**
     * Constructeur
     */
    public TreasureCard(String name, int cost) {
        super(name, cost);
        addType(CardType.TREASURE);
    }
}