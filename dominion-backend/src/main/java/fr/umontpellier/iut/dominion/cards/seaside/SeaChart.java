package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte marine (Sea Chart)
 * <p>
 * +1 Carte
 * +1 Action
 * Dévoilez la carte du haut de votre pioche. Si vous en avez un exemplaire
 * en jeu, prenez-la en main.
 */
public class SeaChart extends Card {
    public SeaChart() {
        super("Sea Chart", 3, CardType.ACTION);
    }

    public void play(Player p){
        p.drawToHand();
        p.incrementAction(1);
        Card c = p.getCardFromDeck();
        if(c!=null) {
            for (Card card : p.getCardsInPlay()) {
                if (card.getName().equals(c.getName())) {
                    p.moveToHand(c);
                    break;
                }
            }
        }
    }
}
