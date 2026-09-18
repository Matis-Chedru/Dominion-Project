package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Astrolabe
 * <p>
 * Maintenant et au début de votre prochain tour :
 * +1 Pièce
 * +1 Achat
 */
public class Astrolabe extends Card {
    public Astrolabe() {
        super("Astrolabe", 3, CardType.TREASURE, CardType.DURATION);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(1);
        p.incrementBuy(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.incrementMoney(1);
            p.incrementBuy(1);
            setDurationEffectPlayed(true);
        }
    }


}
