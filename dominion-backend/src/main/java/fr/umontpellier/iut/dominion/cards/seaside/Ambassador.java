package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Ambassadeur (Ambassador)
 * <p>
 * Dévoilez une carte de votre main.
 * Replacez, de votre main, à la réserve, jusqu'à 2 exemplaires de cette carte.
 * Ensuite, tous vos adversaires reçoivent un exemplaire de cette carte.
 */
public class Ambassador extends Card {
    public Ambassador() {
        super("Ambassador", 3, CardType.ACTION, CardType.ATTACK);
    }
    @Override
    public void play(Player p) {
        Card revealedCard = p.chooseCardFromHand("Choisissez une carte a reveler", false);

        if (revealedCard != null) {
            String cardName = revealedCard.getName();

            for (int i = 0; i < 2; i++) {
                Card cardToReturn = p.chooseCardFromHand(
                        "Remettez une copie de " + cardName + " sur la reserve",
                        c -> c.getName().equals(cardName),
                        true
                );

                if (cardToReturn != null) {
                    p.getGame().replaceCardInSupplyPile(cardToReturn);
                } else {
                    break;
                }
            }

            for (Player otherPlayer : p.getGame().getListPlayers()) {
                if (otherPlayer != p && !otherPlayer.immune()) {
                    Card supplyCard = p.getGame().getCardFromSupply(cardName);
                    if (supplyCard != null) {
                        otherPlayer.moveToDiscard(supplyCard);
                    }
                }
            }
        }
    }
}

