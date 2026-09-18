package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Avant-poste (Outpost)
 * <p>
 * Piochez seulement 3 cartes pour votre prochaine main.
 * Jouez un tour supplémentaire après celui-ci (mais pas un troisième
 * consécutif).
 */
public class Outpost extends Card {
    public Outpost() {
        super("Outpost", 5, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p) {
        p.setOutpostPlayed(true);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            setDurationEffectPlayed(true);
        }
    }
}
