package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions.durations.SeaWitchState;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Sorcière marine (Sea Witch)
 * <p>
 * +2 Cartes
 * Tous vos adversaires reçoivent une Malédiction (Curse).
 * Au début de votre prochain tour, +2 Cartes, puis défaussez 2 cartes.
 */
public class SeaWitch extends AttackCard {
    public SeaWitch() {
        super("Sea Witch", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        p.drawToHand(2);
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        Card curse = target.getCardFromSupply("Curse");
        if (curse != null) {
            target.gainToDiscard(curse);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.drawToHand(2);
        SeaWitchState phase = new SeaWitchState(p, this);
        p.setCurrentState(phase);
        return phase.getCompletionFuture();
    }
}
