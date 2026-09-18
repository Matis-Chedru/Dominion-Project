package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.NativeVillageState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Village indigène (Native Village)
 * <p>
 * +2 Actions
 * Choisissez : placez la carte du haut de votre pioche, face cachée, sur votre
 * plateau Village indigène (vous pouvez consulter ces cartes à tout moment);
 * ou prenez en main toutes les cartes du plateau.
 */
public class NativeVillage extends ActionCard {
    public NativeVillage() {
        super("Native Village", 2);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        p.incrementActions(2);
        NativeVillageState state = new NativeVillageState(p);
        p.setCurrentState(state);
        return state.getCompletionFuture();
    }
}
