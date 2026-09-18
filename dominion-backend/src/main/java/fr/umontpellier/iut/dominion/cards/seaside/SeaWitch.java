package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.Player;

/**
 * Carte Sorcière marine (Sea Witch)
 * <p>
 * +2 Cartes
 * Tous vos adversaires reçoivent une Malédiction (Curse).
 * Au début de votre prochain tour, +2 Cartes, puis défaussez 2 cartes.
 */
public class SeaWitch extends AttackCard {

    public SeaWitch() {
        super("Sea Witch", 5, CardType.ACTION, CardType.DURATION, CardType.ATTACK);
    }

    @Override
    public void play(Player p) {
        p.drawToHand(2);
        performAttack(p);
        setDurationEffectPlayed(false);
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        Card curse = attacker.getGame().getCardFromSupply("Curse");
        if (curse != null) {
            opponent.moveToDiscard(curse);
        }
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.drawToHand(2);
            p.putCardOnDiscard(false, 2);
            setDurationEffectPlayed(true);
        }
    }
}
