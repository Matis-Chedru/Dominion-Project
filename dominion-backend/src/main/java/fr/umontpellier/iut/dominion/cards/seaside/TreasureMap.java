package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Carte aux trésors (Treasure Map)
 * <p>
 * Écartez ceci et une Carte aux trésors de votre main. Si vous avez écarté
 * deux Cartes aux trésors, recevez 4 Ors (Gold) sur votre pioche.
 */
public class TreasureMap extends Card {
    public TreasureMap() {
        super("Treasure Map", 4, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        if (!p.getCardsInHand().isEmpty()) {
            boolean avoirAutreCarte = false;
            for (Card ca : p.getCardsInHand()) {
                if (ca.getName().equals("Treasure Map")) {
                    avoirAutreCarte = true;
                    p.getGame().moveToTrash(ca);
                    break;
                }
            }
            if (avoirAutreCarte) {
                for (int i = 0; i < 4; i++) {
                    Card gold = p.getCardFromSupply("Gold");
                    if (gold != null) {
                        p.moveToDraw(gold);
                    }
                }
            }
        }
        p.getGame().moveToTrash(this);
    }
}
