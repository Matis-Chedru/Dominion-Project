package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.Player;

/**
 * Carte Entrepôt (Warehouse)
 * <p>
 * +3 Cartes
 * +1 Action
 * Défaussez 3 cartes.
 */
public class Warehouse extends Card {
    public Warehouse() {
        super("Warehouse", 3, CardType.ACTION);
    }

    @Override
    public void play(Player p){
        p.incrementAction(1);
        p.drawToHand(3);
        p.putCardOnDiscard(false,3);

    }
}
