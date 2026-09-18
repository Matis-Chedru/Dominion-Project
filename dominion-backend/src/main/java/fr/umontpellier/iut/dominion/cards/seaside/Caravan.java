package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Caravane (Caravan)
 * <p>
 * +1 Carte
 * +1 Action
 * Au début de votre prochain tour, +1 Carte.
 */
public class Caravan extends Card {
    public Caravan() {
        super("Caravan", 4, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p) {
        p.incrementAction(1);
        p.drawToHand();
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.drawToHand();
            setDurationEffectPlayed(true);
        }
    }

}
