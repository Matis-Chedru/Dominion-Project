package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Village de pêcheurs (Fishing Village)
 * <p>
 * +2 Actions
 * +1 Pièce
 * Au début de votre prochain tour, +1 Action et +1 Pièce.
 */
public class FishingVillage extends Card {
    public FishingVillage() {
        super("Fishing Village", 3, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p){
        p.incrementAction(2);
        p.incrementMoney(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        if(!isDurationEffectPlayed()){
            p.incrementMoney(1);
            p.incrementAction(1);
            setDurationEffectPlayed(true);
        }
    }


}