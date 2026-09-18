package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;

/**
 * Carte Vigie (Lookout)
 * <p>
 * +1 Action
 * Consultez les 3 premières cartes des votre pioche. Écartez-en une.
 * Défaussez-en une. Placez la carte restante sur le haut de votre pioche.
 */
public class Lookout extends Card {
    public Lookout() {
        super("Lookout", 3, CardType.ACTION);
    }

    @Override
    public void play(Player p){
        p.incrementAction(1);
        ArrayList<Card> cartes = p.getCardsFromDeck(3);
        Card c1 = p.chooseCardFromButtons("Choississez une carte à jeter", cartes,false);
        p.getGame().moveToTrash(c1);
        Card c2 = p.chooseCardFromButtons("Choississez une carte à défausser, celle non sélectionner sera dans la pioche", cartes, false);
        p.moveToDiscard(c2);
        p.moveToDraw(cartes.getFirst());
    }
}
