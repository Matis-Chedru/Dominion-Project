package fr.umontpellier.iut.dominionfx.mechanics.cards.seaside;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.ActionCard;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Carte aux trésors (Treasure Map)
 * <p>
 * Écartez ceci et une Carte aux trésors de votre main. Si vous avez écarté
 * deux Cartes aux trésors, recevez 4 Ors (Gold) sur votre pioche.
 */
public class TreasureMap extends ActionCard {

    public TreasureMap() {
        super("Treasure Map", 4);
    }

    @Override
    public CompletableFuture<Void> play(Player p) {
        List<Card> trashedCards = new ArrayList<>();
        trashedCards.add(this);
        for (Card c: p.getCardsInHand()) {
            if (c.hasName("Treasure Map")) {
                trashedCards.add(c);
                break;
            }
        }
        p.moveToTrash(trashedCards);
        if (trashedCards.size() == 2) {
            List<Card> gainedGold = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Card gold = p.getCardFromSupply("Gold");
                if (gold == null) {
                    break;
                }
                gold.moveTo(gainedGold);
            }
            if (!gainedGold.isEmpty()) {
                for (Card c : new ArrayList<>(gainedGold)) {
                    p.gainToDraw(c);
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
