package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Argent (Silver)
 * <p>
 * 2 Pièces
 */
public class Silver extends Card {
    public Silver() {
        super("Silver", 3, CardType.TREASURE);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(2);
    }
}
