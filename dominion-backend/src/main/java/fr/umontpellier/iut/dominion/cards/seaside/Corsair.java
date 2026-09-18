package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Carte Corsaire (Corsair)
 * <p>
 * +2 Pièces
 * Au début de votre prochain tour, +1 Carte. D'ici là, chacun de vos
 * adversaires écarte le premier Argent ou Or qu'il joue à chaque tour.
 */
public class Corsair extends AttackCard {

    private List<Player> adversairesAffectes;

    public Corsair() {
        super("Corsair", 5, CardType.ACTION, CardType.DURATION, CardType.ATTACK);
        adversairesAffectes = new ArrayList<>();
    }

    @Override
    public void play(Player p) {
        p.incrementMoney(2);
        adversairesAffectes.clear();
        performAttack(p);
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
        adversairesAffectes.add(opponent);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            p.drawToHand();
            adversairesAffectes.clear();
            setDurationEffectPlayed(true);
        }
    }

    public boolean isPlayerAffected(Player p) {
        return adversairesAffectes.contains(p);
    }
}
