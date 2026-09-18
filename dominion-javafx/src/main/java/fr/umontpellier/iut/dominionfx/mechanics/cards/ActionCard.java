package fr.umontpellier.iut.dominionfx.mechanics.cards;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;

/**
 * Les cartes Action
 */
public abstract class ActionCard extends Card {

    /**
     * Constructeur
     */
    public ActionCard(String name, int cost) {
        super(name, cost);
        addType(CardType.ACTION);
    }
}