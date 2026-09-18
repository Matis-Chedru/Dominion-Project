package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.concurrent.CompletableFuture;

/**
 * Carte Sorcière de mer (Sea Hag)
 * <p>
 * Tous vos adversaires défaussent la carte du haut de leur pioche, puis 
 * reçoivent une Malédiction (Curse) sur leur pioche.
 */
public class SeaHag extends AttackCard {

    public SeaHag() {
        super("Sea Hag", 4);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        // rien à faire pour le joueur qui joue la carte
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        Card c = target.getCardFromDeck();
        if (c != null) {
            target.moveToDiscard(c);
        }
        Card curse = target.getCardFromSupply("Curse");
        if (curse != null) {
            target.gainToDraw(curse);
        }
        return CompletableFuture.completedFuture(null);
    }
}
