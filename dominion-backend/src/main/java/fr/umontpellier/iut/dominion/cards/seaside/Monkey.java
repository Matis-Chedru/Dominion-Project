package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Singe (Monkey)
 * <p>
 * Jusqu'à votre prochain tour, quand le joueur à votre droite reçoit une
 * carte, +1 Carte.
 * Au début de votre prochain tour, +1 Carte.
 */
public class Monkey extends Card {
    public Monkey() {
        super("Monkey", 3, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p) {
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.drawToHand();
            setDurationEffectPlayed(true);
        }
    }
}
