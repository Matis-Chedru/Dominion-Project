package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Or (Gold)
 * <p>
 * 3 Pièces
 */
public class Gold extends Card {
    public Gold() {
        super("Gold", 6, CardType.TREASURE);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(3);
    }
}
