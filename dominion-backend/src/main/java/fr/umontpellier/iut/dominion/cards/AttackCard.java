package fr.umontpellier.iut.dominion.cards;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;

public abstract class AttackCard extends Card {

    public AttackCard(String name, int cost, CardType... types) {
        super(name, cost, types);
    }

    /**
     * Applique l'effet d'attaque de la carte à tous les adversaires qui ne sont pas immunisés.
     * @param attacker
     */
    protected void performAttack(Player attacker) {
        for (Player opponent : attacker.getGame().getListPlayers()) {
            if (opponent != attacker && !opponent.immune()) {
                applyAttackEffect(attacker, opponent);
            }
        }
    }

    /**
     * Applique l'effet spécifique de l'attaque sur un adversaire.
     * @param attacker
     * @param opponent
     */
    protected abstract void applyAttackEffect(Player attacker, Player opponent);
}