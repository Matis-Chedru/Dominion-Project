package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.Player;
import fr.umontpellier.iut.dominion.cards.AttackCard;
import fr.umontpellier.iut.dominion.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Carte Blocus (Blockade)
 * <p>
 * Recevez une carte coûtant jusqu'à 4 Pièces, en la mettant de côté.
 * Au début de votre prochain tour, prenez-la en main
 * Tant qu'elle est mise de côté, quand un autre joueur en reçoit un
 * exemplaire durant leur tour, il reçoit une Malédiction (Curse).
 */
public class Blockade extends AttackCard {

    private Card carteMiseDeCote;
    private List<Player> affectedPlayers; // Liste pour mémoriser qui était vulnérable

    public Blockade() {
        super("Blockade", 4, CardType.ACTION, CardType.DURATION, CardType.ATTACK);
        carteMiseDeCote = null;
        affectedPlayers = new ArrayList<>();
    }

    @Override
    public void play(Player p) {
        Card gained = p.chooseCardFromSupply("Choisissez une carte coûtant jusqu'à 4 à mettre de côté",
                c -> c.getCost() <= 4, false);

        if (gained != null) {
            carteMiseDeCote = gained;
            p.moveToSetAside(gained);
        }

        affectedPlayers.clear();
        for (Player opponent : p.getGame().getListPlayers()) {
            if (opponent != p && !opponent.immune()) {
                affectedPlayers.add(opponent);
            }
        }

        setDurationEffectPlayed(false);
    }

    @Override
    public void playDuration(Player p) {
        if (!isDurationEffectPlayed()) {
            if (carteMiseDeCote != null && p.getCardsSetAside().contains(carteMiseDeCote)) {
                p.moveToHand(carteMiseDeCote);
            }
            carteMiseDeCote = null;
            affectedPlayers.clear();
            setDurationEffectPlayed(true);
        }
    }

    @Override
    protected void applyAttackEffect(Player attacker, Player opponent) {
    }

    public boolean attaqueActiveContre(String nomCarte, Player cible) {
        return carteMiseDeCote != null && carteMiseDeCote.getName().equals(nomCarte) && !isDurationEffectPlayed() && affectedPlayers.contains(cible);
    }
}
