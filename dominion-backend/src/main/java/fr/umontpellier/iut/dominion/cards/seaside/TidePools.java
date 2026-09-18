package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Marée (Tide Pools)
 * <p>
 * +3 Cartes
 * +1 Action
 * Au début de votre prochain tour, défaussez 2 cartes.
 */
public class TidePools extends Card {
    public TidePools() {
        super("Tide Pools", 4, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        p.drawToHand(3);
        p.incrementAction(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()){
            p.putCardOnDiscard(false,2);
            setDurationEffectPlayed(true);
        }
    }
}
