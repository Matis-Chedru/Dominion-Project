package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Tacticien (Tactician)
 * <p>
 * Si vous avez au moins une carte en main, défaussez votre main, et au debut
 * de votre prochain tour, +5 Cartes, +1 Action, et +1 Achat.
 */
public class Tactician extends Card {
    public Tactician() {
        super("Tactician", 5, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        if(!p.getCardsInHand().isEmpty()){
            for (Card c : p.getCardsInHand()){
                p.moveToDiscard(c);
            }
            setDurationEffectPlayed(false);
        }
        else {
            setDurationEffectPlayed(true);
        }
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()){
            p.incrementAction(1);
            p.incrementBuy(1);
            p.drawToHand(5);
            setDurationEffectPlayed(true);
        }
    }
}
