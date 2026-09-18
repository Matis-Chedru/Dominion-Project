package fr.umontpellier.iut.dominion.cards.common;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Cuivre (Copper)
 * <p>
 * 1 Pièce
 */
public class Copper extends Card {
    public Copper() {
        super("Copper", 0, CardType.TREASURE);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(1);
    }
}
