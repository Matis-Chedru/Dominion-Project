package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.SmugglersState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Contrebandiers (Smugglers)
 * <p>
 * Recevez un exemplaire d'une carte coûtant jusqu'à 6 Pièces que le joueur
 * à votre droite a reçues à son dernier tour.
 */
public class Smugglers extends ActionCard {
    public Smugglers() {
        super("Smugglers", 3);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        List<String> possibleCardNames = p.getOtherPlayers().getLast().getCardsGainedThisTurn().stream()
                .filter(c -> c.getCost() <= 6)
                .map(Card::getName)
                .distinct()
                .toList();
        if (!possibleCardNames.isEmpty()) {
            SmugglersState state = new SmugglersState(p, possibleCardNames);
            p.setCurrentState(state);
            return state.getCompletionFuture();
        } else
            return CompletableFuture.completedFuture(null);
    }
}