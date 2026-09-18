package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Sorcière de mer (Sea Hag)
 * <p>
 * Tous vos adversaires défaussent la carte du haut de leur pioche, puis 
 * reçoivent une Malédiction (Curse) sur leur pioche.
 */
public class SeaHag extends AttackCard {

    public SeaHag() {
        super("Sea Hag", 4, CardType.ACTION, CardType.ATTACK);
    }

    @Override
    public void play(Player p) {
        performAttack(p);
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        Card topCard = opponent.getCardFromDeck();
        if (topCard != null) {
            opponent.moveToDiscard(topCard);
        }

        Card curse = attacker.getGame().getCardFromSupply("Curse");
        if (curse != null) {
            opponent.moveToDraw(curse);
        }
    }
}
