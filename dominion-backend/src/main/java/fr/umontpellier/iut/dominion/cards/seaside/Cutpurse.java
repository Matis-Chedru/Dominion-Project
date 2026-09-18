package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Coupeur de bourse (Cutpurse)
 * <p>
 * +2 Pièces
 * Tous vos adversaires défaussent un Cuivre (Copper) (ou dévoilent une main
 * sans Cuivre).
 */
public class Cutpurse extends AttackCard {

    public Cutpurse() {
        super("Cutpurse", 4, CardType.ACTION, CardType.ATTACK);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(2);
        performAttack(p);
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        Card copper = opponent.getCardsInHand().stream()
                .filter(c -> c.getName().equals("Copper"))
                .findFirst()
                .orElse(null);

        if (copper != null) {
            opponent.moveToDiscard(copper);
        } else {
            opponent.log("Montre ses cartes : " + opponent.getCardsInHand());
        }
    }
}



