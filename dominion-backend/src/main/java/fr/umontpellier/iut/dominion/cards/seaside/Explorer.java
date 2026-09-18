package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Carte Explorateur (Explorer)
 * <p>
 * Vous pouvez dévoiler une Province de votre main. Si vous le faites, recevez
 * un Or (Gold) en main. Sinon, recevez un Argent (Silver) en main.
 */
public class Explorer extends Card {
    public Explorer() {
        super("Explorer", 5, CardType.ACTION);
    }

    @Override
    public void play(Player p){
        Card rep = p.chooseCardFromHand("Voulez-vous dévoiler une carte Province ?", card -> card.hasName("Province"),true);
            if(rep!=null){
                p.moveToHand(p.getCardFromSupply("Gold"));
            }
            else{
                p.moveToHand(p.getCardFromSupply("Silver"));
            }
    }
}
