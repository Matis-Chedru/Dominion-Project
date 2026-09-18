package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.SupplyPile;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Embargo
 * <p>
 * +2 Pièces
 * Écartez ceci pour placer un jeton Embargo sur une pile de la réserve.
 * (Pendant le reste de la partie, quand un joueur achète une carte de cette
 * pile, il reçoit une Malédiction (Curse).)
 */
public class Embargo extends Card {
    public Embargo() {
        super("Embargo", 2, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(2);
        p.getGame().moveToTrash(this);
        String pileChoisie = p.choose("Choisissez une pile pour y placer un jeton Embargo :",
                p.getGame().getAvailableSupplyCards().stream()
                        .map(c -> "SUPPLY:" + c.getName())
                        .toList(),
                new java.util.ArrayList<>(),
                false);

        if (!pileChoisie.isEmpty()) {
            String nomCarte = pileChoisie.split(":")[1];
            SupplyPile pile = p.getGame().getSupply(nomCarte);
            if (pile != null) {
                pile.addEmbargoToken();
            }
        }
    }
}
