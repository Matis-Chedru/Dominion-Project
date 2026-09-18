package fr.umontpellier.iut.dominion.cards.seaside;

import fr.umontpellier.iut.dominion.CardType;
import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrebandiers (Smugglers)
 * <p>
 * Recevez un exemplaire d'une carte coûtant jusqu'à 6 Pièces que le joueur
 * à votre droite a reçues à son dernier tour.
 */
public class Smugglers extends Card {
    public Smugglers() {
        super("Smugglers", 3, CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        int tailleListe = p.getGame().getListPlayers().size();
        int indexJoueurDroite = (p.getIndex() - 1 + tailleListe) % tailleListe;
        Player joueurDroite = p.getGame().getListPlayers().get(indexJoueurDroite);
        List<Card> cartesGagnees = joueurDroite.getCardsGainedThisTurn();
        List<String> choixPossibles = cartesGagnees.stream()
                .filter(c -> c.getCost() <= 6)
                .map(c -> "SUPPLY:" + c.getName())
                .distinct()
                .filter(nom -> p.getGame().getCardFromSupply(nom.split(":")[1]) != null)
                .collect(Collectors.toList());
        String choix = p.choose("Choisissez une carte à copier :", choixPossibles, new ArrayList<>(), false);

        if (choix != null && !choix.isEmpty()) {
            String nomCarte = choix.split(":")[1];
            Card gainedCard = p.getGame().getCardFromSupply(nomCarte);
            if (gainedCard != null) {
                p.gain(gainedCard);
            }
        }
    }
}
