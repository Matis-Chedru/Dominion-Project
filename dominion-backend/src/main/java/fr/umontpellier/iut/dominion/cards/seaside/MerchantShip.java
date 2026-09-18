package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Navire marchand (Merchant Ship)
 * <p>
 * Maintenant et au début de votre prochain tour, +Pièces.
 */
public class MerchantShip extends Card {
    public MerchantShip() {
        super("Merchant Ship", 5, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        p.incrementMoney(2);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()){
            p.incrementMoney(2);
            setDurationEffectPlayed(true);
        }
    }
}
