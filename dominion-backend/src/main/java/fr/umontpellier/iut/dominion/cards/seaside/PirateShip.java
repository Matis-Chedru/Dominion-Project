package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.Button;
import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;

/**
 * Carte Bateau pirate (Pirate Ship)
 * <p>
 * Choisissez : +1 Pièce par jeton Pièce sur votre plateau Bateau pirate ;
 * ou tous vos adversaires dévoilent les 2 premières cartes de leur pioche,
 * écartent un Trésor (Treasure) dévoilé de votre choix et défaussent le reste,
 * et si au moins un Trésor a été écarté, placez un jeton Pièce sur votre
 * plateau Bateau pirate.
 */
public class PirateShip extends AttackCard {
    private boolean b;

    public PirateShip() {
        super("Pirate Ship", 4, CardType.ACTION, CardType.ATTACK);
        b = false;
    }

    @Override
    public void play(Player p) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button coins = new Button("+1 pièce par jetons sur le bateau pirate", "coins");
        Button attack = new Button("Attaquer les adverssaire (+1 pièce sur Bateau pirate par carte treasury attaquer", "attack");
        buttons.add(coins);
        buttons.add(attack);
        String choix = p.chooseStringFromButtons("Choisissez votre action", buttons, false);
        if (choix.equals("coins")) {
            p.incrementMoney(p.getCoinsOnMat());
        } else {
            super.performAttack(p);
            if (b) {
                p.incrementCoinsOnMat(1);
            }
        }
        b = false;
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        ArrayList<Card> cards = new ArrayList<>();

        Card c1;
        for (int i = 0; i < 2; i++) {
            c1 = opponent.drawToHand();
            if (c1 != null) {
                cards.add(c1);
            }
        }
        ArrayList<Card> copie = new ArrayList<>(cards);
        ArrayList<Card> treasure = new ArrayList<>();
        for (Card c : cards) {
            if (c.hasType(CardType.TREASURE)) {
                treasure.add(c);
            }
        }
        if (!treasure.isEmpty()) {
            Card cho = attacker.chooseCardFromButtons("Choisissez la carte Treasury à écarter", treasure, true);
            if (cho != null) {
                opponent.getGame().moveToTrash(cho);
                cards.remove(cho);
                b = true;
            }
        }
        for (Card c : cards) {
            opponent.moveToDiscard(c);
        }
    }
}
