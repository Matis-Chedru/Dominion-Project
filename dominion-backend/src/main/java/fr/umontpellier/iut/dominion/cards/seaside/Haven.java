package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Havre (Haven)
 * <p>
 * +1 Carte
 * +1 Action
 * Mettez de côté une carte de votre main face cachée (sous cette carte).
 * Au début de votre prochain tour, prenez-la en main.
 */
public class Haven extends Card {
    public Haven() {
        super("Haven", 2, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        p.drawToHand();
        p.incrementAction(1);
        Card c = p.chooseCardFromHand("Choississez une carte à garder au prochain tour",false);
        p.moveToSetAside(c);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()) {
            p.moveToHand(p.getCardsSetAside().getFirst());
            setDurationEffectPlayed(true);

        }
    }
}
