package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte Phare (Lighthouse)
 * <p>
 * +1 Action
 * Maintenant et au début de votre prochain tour, +1 Pièce.
 * D'ici là, les cartes Attaque jouées par vos adversaires ne vous affectent
 * pas.
 */
public class Lighthouse extends Card {
    public Lighthouse() {
        super("Lighthouse", 2, CardType.ACTION, CardType.DURATION);
    }

    @Override
    public void play(Player p) {
        p.incrementAction(1);
        p.incrementMoney(1);
        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.incrementMoney(1);
            setDurationEffectPlayed(true);

        }
    }

    @Override
    public boolean immune() {
        return true;
    }
}