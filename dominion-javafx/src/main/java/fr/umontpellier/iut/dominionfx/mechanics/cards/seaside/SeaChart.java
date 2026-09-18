package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.concurrent.CompletableFuture;

/**
 * Carte marine (Sea Chart)
 * <p>
 * +1 Carte
 * +1 Action
 * Dévoilez la carte du haut de votre pioche. Si vous en avez un exemplaire
 * en jeu, prenez-la en main.
 */
public class SeaChart extends ActionCard {
    public SeaChart() {
        super("Sea Chart", 3);
        initselectedCardNameProperty();
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand();
        p.incrementActions(1);
        Card topCard = p.getCardFromDeck();
        if (topCard != null) {
            selectedCardNameProperty().set(topCard.getName());
            if (p.getCardsInPlay().stream().anyMatch(c -> c.hasSameNameAs(topCard))) {
                p.moveToHand(topCard);
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
