package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Vaisseau fantôme (Ghost Ship)
 * <p>
 * +2 Cartes
 * Tous vos adversaires ayant au moins 4 cartes en main placent des cartes
 * de leur main sur leur pioche jusqu'à avoir 3 cartes en main.
 */
public class GhostShip extends AttackCard {
    public GhostShip() {
        super("Ghost Ship", 5, CardType.ACTION, CardType.ATTACK);
    }

    @Override
    public void play(Player p) {
        p.drawToHand(2);
        performAttack(p);
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        while (opponent.getCardsInHand().size() > 3) {
            Card c = opponent.chooseCardFromHand("Choisissez une carte à remettre sur votre pioche", false);
            if (c != null) {
                opponent.moveToDraw(c);
            }
        }
    }
}
