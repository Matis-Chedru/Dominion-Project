package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.AmbassadorRevealState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Ambassadeur (Ambassador)
 * <p>
 * Dévoilez une carte de votre main.
 * Replacez, de votre main, à la réserve, jusqu'à 2 exemplaires de cette carte.
 * Ensuite, tous vos adversaires reçoivent un exemplaire de cette carte.
 */
public class Ambassador extends AttackCard {

    public Ambassador() {
        super("Ambassador", 3);
        initselectedCardNameProperty();
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        AmbassadorRevealState ambassadorRevealState = new AmbassadorRevealState(p, this);
        selectedCardNameProperty().bind(ambassadorRevealState.revealedCardNameProperty());
        p.setCurrentState(ambassadorRevealState);
        return getCompletionFuture();
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        Card c = target.getCardFromSupply(selectedCardNameProperty().getValue());
        if (c != null) {
            target.gainToDiscard(c);
        }
        complete();
        return getCompletionFuture();
    }
}
