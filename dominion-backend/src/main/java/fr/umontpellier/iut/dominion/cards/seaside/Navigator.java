package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;
import java.util.ArrayList;
import java.util.List;
import fr.umontpellier.iut.dominion.Button;


/**
 * Carte Navigateur (Navigator)
 * <p>
 * +2 Pièces
 * Consultez les 5 premières cartes de votre pioche.
 * Défaussez-les toutes ou replacez-les sur votre pioche dans l'ordre de
 * votre choix.
 */
public class Navigator extends Card {
    public Navigator() {
        super("Navigator", 4, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(2);
        List<Card> cards = p.getCardsFromDeck(5);
        if (cards.isEmpty()) {
            return;
        }
        String choice = p.chooseStringFromButtons("Voulez-vous défausser ces cartes ou les replacer sur votre pioche ?",
                List.of(new Button("Défausser", "y"), new Button("Replacer", "n")),
                false);

        if ("y".equals(choice)) {
            for (Card c : new ArrayList<>(cards)) {
                p.moveToDiscard(c);
            }
        } else {
            while (!cards.isEmpty()) {
                Card c = p.chooseCardFromButtons("Choisissez la prochaine carte à mettre sur votre pioche (elle sera en dessous des suivantes)",
                        cards,
                        false);
                p.moveToDraw(c);
                cards.remove(c);
            }
        }


    }
}
