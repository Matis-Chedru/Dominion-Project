package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Navigatrice (Sailor)
 * <p>
 * +1 Action
 * Une fois durant ce tour, quand vous recevez une carte Durée (Duration),
 * vous pouvez la jouer.
 * Au début de votre prochain tour, +2 Pièces et vous pouvez écarter une carte
 * de votre main.
 */
public class Sailor extends Card {
    public Sailor() {
        super("Sailor", 4, CardType.ACTION, CardType.DURATION);
    }
    @Override
    public void play(Player p){
        p.incrementAction(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p){
        p.incrementMoney(2);
        Card c = p.chooseCardFromHand("Choissisez une carte à jeter (si vous voulez)",true);
        if(c!=null){
            p.getGame().moveToTrash(c);
        }
        setDurationEffectPlayed(true);
    }
}
