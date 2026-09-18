package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Carte Corsaire (Corsair)
 * <p>
 * +2 Pièces
 * Au début de votre prochain tour, +1 Carte. D'ici là, chacun de vos
 * adversaires écarte le premier Argent ou Or qu'il joue à chaque tour.
 */
public class Corsair extends AttackCard {
    private final List<Player> attackedPlayers = new ArrayList<>();

    public Corsair() {
        super("Corsair", 5);
        addType(CardType.DURATION);
    }

    @Override
    public CompletableFuture<Void> action(Player p) {
        p.incrementMoney(2);
        attackedPlayers.clear();
        setHasDurationEffect(true);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> attack(Player p, Player target) {
        attackedPlayers.add(target);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void onPlayerPlayCard(Player p, Card playedCard, Player owner) {
        if (attackedPlayers.contains(p)
                && (playedCard.hasName("Silver") || playedCard.hasName("Gold"))
                && p.getNbSilverOrGoldPlayed() == 1) {
            p.moveToTrash(playedCard);
        }
    }

    @Override
    public CompletableFuture<Void> atStartOfTurn(Player p) {
        p.drawToHand();
        setHasDurationEffect(false);
        return CompletableFuture.completedFuture(null);
    }
}
