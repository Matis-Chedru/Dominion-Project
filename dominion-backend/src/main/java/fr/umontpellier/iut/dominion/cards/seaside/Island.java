package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.Player;

/**
 * Carte Île (Island)
 * <p>
 * 2 VP
 * Placez cette carte et une carte de votre main sur votre plateau Île (Island
 * Mat).
 */
public class Island extends Card {
    public Island() {
        super("Island", 4, CardType.ACTION, CardType.VICTORY);
    }

    @Override
    public void play(Player p){
        Card c = p.chooseCardFromHand("Choissisez une carte à défausser avec Island",false);
        if (!(c==null)){
            p.putCardOnIsland(c);
            p.putCardOnIsland(this);
        }
    }

    @Override
    public int getVictoryValue(){
        return 2;
    }
}
