package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations.PirateState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Pirate
 * <p>
 * Au début de votre prochain tour, recevez en main un Trésor coûtant jusqu'à
 * 6 Pièces.
 * Quand un joueur reçoit un Trésor, vous pouvez jouer cette carte depuis votre
 * main.
 */
public class Pirate extends ActionCard {

    public Pirate() {
        super("Pirate", 5);
        addType(CardType.DURATION);
        addType(CardType.REACTION);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        // Rien à faire au moment où la carte est jouée
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean canReactToPlayerGainCard(Player p, Card gainedCard, Player owner) {
        return gainedCard.hasType(CardType.TREASURE);
    }

    @Override
    public CompletableFuture<Void> reactToPlayerGainCard(Player p, Card gainedCard, Player owner) {
        return owner.playCard(this);
//        owner.moveToInPlay(this);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        PirateState phase = new PirateState(p,this);
        p.setCurrentState(phase);
        return phase.getCompletionFuture();
    }
}