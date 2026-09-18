package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.HavenState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Havre (Haven)
 * <p>
 * +1 Carte
 * +1 Action
 * Mettez de côté une carte de votre main face cachée (sous cette carte).
 * Au début de votre prochain tour, prenez-la en main.
 */
public class Haven extends ActionCard {
    private Card cardSetAside;

    public Haven() {
        super("Haven", 2);
        addType(CardType.DURATION);
        initselectedCardNameProperty();
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.drawToHand();
        p.incrementActions(1);
        HavenState state = new HavenState(p, this);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }

    public void endPlay(Player p, String cardName) {
        Card card = p.getCardFromHand(cardName);
        p.moveToSetAside(card);
        this.cardSetAside = card;
        selectedCardNameProperty().set(cardName);
        setHasDurationEffect(true);
        p.getCurrentState().complete();
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.moveToHand(cardSetAside);
        cardSetAside = null;
        selectedCardNameProperty().set(null);
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}

