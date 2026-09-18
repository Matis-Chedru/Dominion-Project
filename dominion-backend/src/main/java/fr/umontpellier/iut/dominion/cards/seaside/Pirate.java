package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Pirate
 * <p>
 * Au début de votre prochain tour, recevez en main un Trésor coûtant jusqu'à
 * 6 Pièces.
 * Quand un joueur reçoit un Trésor, vous pouvez jouer cette carte depuis votre
 * main.
 */
public class Pirate extends Card {
    public Pirate() {
        super("Pirate", 5, CardType.ACTION, CardType.DURATION, CardType.REACTION);
    }

    @Override
    public void play(Player p) {
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            Card treasure = p.chooseCardFromSupply(
                    "Choisissez un trésor coûtant jusqu'à 6 à ajouter à votre main :",
                    c -> c.hasType(CardType.TREASURE) && c.getCost() <= 6,
                    false
            );

            if (treasure != null) {
                p.moveToHand(treasure);
            }

            setDurationEffectPlayed(true);
        }
    }
}
