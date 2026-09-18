package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Quai (Wharf)
 * <p>
 * Maintenant et au début de votre prochain tour : +2 Cartes et +1 Achat.
 */
public class Wharf extends Card {
    public Wharf() {
        super("Wharf", 5, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        p.drawToHand(2);
        p.incrementBuy(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()){
            p.drawToHand(2);
            p.incrementBuy(1);
            setDurationEffectPlayed(true);
        }
    }
}
