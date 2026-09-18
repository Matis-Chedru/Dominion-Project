package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.Button;
import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;

/**
 * Carte Village indigène (Native Village)
 * <p>
 * +2 Actions
 * Choisissez : placez la carte du haut de votre pioche, face cachée, sur votre
 * plateau Village indigène (vous pouvez consulter ces cartes à tout moment);
 * ou prenez en main toutes les cartes du plateau.
 */
public class NativeVillage extends Card {
    public NativeVillage() {
        super("Native Village", 2, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        p.incrementAction(2);

        ArrayList<Button> choix = new ArrayList<>();
        choix.add(new Button("placez la carte du haut de votre pioche, face cachée, " +
                "sur votre plateau Village indigène", "add"));
        choix.add(new Button("prenez en main toutes les cartes du plateau village indigène", "take"));

        String c = p.chooseStringFromButtons("Choisissez l'action que vous réalisez", choix, false);
        if (c.equals("add")) {
            p.moveToNativeVillage(p.getCardFromDeck());
        } else {
            for (Card card : p.getCardsOnNativeVillageMat()) {
                p.moveToHand(card);
            }
        }
    }
}
