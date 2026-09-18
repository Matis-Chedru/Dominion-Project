package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain.LookoutState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Vigie (Lookout)
 * <p>
 * +1 Action
 * Consultez les 3 premières cartes des votre pioche. Écartez-en une.
 * Défaussez-en une. Placez la carte restante sur le haut de votre pioche.
 */
public class Lookout extends ActionCard {
    public Lookout() {
        super("Lookout", 3);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementActions(1);
        LookoutState phase1 = new LookoutState(p);
        p.setCurrentState(phase1);
        return phase1.getCompletionFuture();
    }

}

/*
p.incrementActions(1);
List<Card> topCards = p.drawCards(3);

// carte à écarter
Card c = p.chooseCardFromButtons(
        "%s: Choose a card to trash".formatted(this),
        topCards,
        false);
        if (c != null) {
        p.moveToTrash(c);
        }
// carte à défausser
c = p.chooseCardFromButtons(
                "%s: Choose a card to discard".formatted(this),
topCards,
        false);
        if (c != null) {
        p.moveToDiscard(c);
        }
                // carte à remettre sur le dessus de la pioche
                p.moveToDraw(topCards);
        return CompletableFuture.completedFuture(null);*/
