package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.Button;
import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Carte Plongeur de perles (Pearl Diver)
 * <p>
 * +1 Carte
 * +1 Action
 * Consultez la carte du bas de votre pioche. Vous pouvez la placer sur le haut.
 */
public class PearlDiver extends Card {
    public PearlDiver() {
        super("Pearl Diver", 2, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        p.drawToHand();
        p.incrementAction(1);
        if (p.getCardsInDraw().isEmpty()) {
            return;
        }
        Card bottomCard = p.getCardFromDrawBottom();
        String choice = p.choose("Voulez vous mettre la carte " + bottomCard.getName() + " sur le dessus de la pioche ?",
                new ArrayList<>(),
                new ArrayList<>(List.of(new Button("Yes", "y"), new Button("No", "n"))),
                false);
        if ("BUTTON:y".equals(choice)) {
            p.moveToDraw(bottomCard);
        }
    }
}
