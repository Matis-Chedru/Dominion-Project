package fr.umontpellier.iut.dominion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import fr.umontpellier.iut.dominion.SupplyPile;


import fr.umontpellier.iut.dominion.BaseTestClass;
import org.junit.jupiter.api.Test;

import fr.umontpellier.iut.dominion.cards.Card;

public class CardsTestEtudiants extends BaseTestClass {
    @Test
    public void Blockade_With_Lighthouse_But_Lighthouse_Played_After_Blockade() {
        setup2pGame("Blockade", "Lighthouse", "Haven");
        Card blockade = getCardFromSupply("Blockade");
        Card lighthouse = getCardFromSupply("Lighthouse");
        Card[] havens = getCardsFromSupply("Haven", 2);
        Card curse = getCardFromSupply("Curse");
        Card[] gold = getCardsFromSupply("Gold", 5);
        blockade.moveTo(p1.hand);
        lighthouse.moveTo(p2.hand);
        gold[0].moveTo(p2.hand);
        gold[1].moveTo(p2.hand);
        gold[2].moveTo(p2.hand);

        playTurn("HAND:Blockade", "SUPPLY:Haven", "");
        assertPlayerState(p1, 0, 0, 1);
        assertCardInLocation(blockade, p1.inPlay);
        assertCardInLocation(havens[0], p1.cardsSetAside);
        assertCardInLocation(havens[1], getSupply("Haven"));
        assertCardInLocation(curse, getSupply("Curse"));
        endTurn();
        assertCardInLocation(blockade, p1.inPlay);


        playTurn("HAND:Lighthouse", "HAND:Gold", "HAND:Gold", "SUPPLY:Haven");
        assertPlayerState(p2, 5, 1, 0);
        assertCardInLocation(lighthouse, p2.inPlay);
        assertCardInLocation(havens[1], p2.discard);
        assertCardInLocation(gold[0], p2.inPlay);
        assertCardInLocation(gold[1], p2.inPlay);
        assertCardInLocation(curse, p2.discard);
        endTurn();
        assertCardInLocation(lighthouse, p2.inPlay);


        playTurn("");
        assertCardInLocation(havens[0], p1.hand);
        endTurn();
        assertFalse(p1.discard.contains(blockade));
    }


    @Test
    public void Blockade_On_Curse(){
        setup2pGame("Blockade");
        Card blockade = getCardFromSupply("Blockade");
        blockade.moveTo(p1.hand);
        SupplyPile curse = getSupply("Curse");

        playTurn("HAND:Blockade", "SUPPLY:Curse", "");
        endTurn();
        playTurn("SUPPLY:Curse");
        endTurn();
        assertTrue(curse.isEmpty());

    }

    @Test
    public void testSeaHag() {
        setup2pGame("Sea Hag");
        Card seahag = getCardFromSupply("Sea Hag");
        Card silver = getCardFromSupply("Silver");
        Card curse = getCardFromSupply("Curse");

        silver.moveTo(p2.draw);
        seahag.moveTo(p1.hand);

        playTurn("HAND:Sea Hag", "");

        assertCardInLocation(silver, p2.discard);
        assertCardInLocation(curse, p2.draw);

        endTurn();

    }

    @Test
    void test_explorer_with_province_revealed() {
        setup2pGame("Explorer");
        Card explorer = getCardFromSupply("Explorer");
        Card province = getCardFromSupply("Province");

        explorer.moveTo(p1.hand);
        province.moveTo(p1.hand);

        playTurn("HAND:Explorer", "HAND:Province", "");

        Card gold = p1.hand.getLast();
        assertEquals("Gold", gold.getName());

        endTurn();
    }

    @Test
    void test_explorer_has_province_but_chooses_not_to_revealed() {
        setup2pGame("Explorer");
        Card explorer = getCardFromSupply("Explorer");
        Card province = getCardFromSupply("Province");

        explorer.moveTo(p1.hand);
        province.moveTo(p1.hand);

        playTurn("HAND:Explorer", "", "");

        Card gainedCard = p1.hand.getLast();
        assertEquals("Silver", gainedCard.getName());
        endTurn();
    }

    @Test
    void test_explorer_no_province_in_hand() {
        setup2pGame("Explorer");
        Card explorer = getCardFromSupply("Explorer");
        explorer.moveTo(p1.hand);

        p1.hand.removeIf(c -> c.getName().equals("Province"));

        playTurn("HAND:Explorer", "", "");

        assertEquals("Silver", p1.hand.getLast().getName());
        endTurn();
    }

    @Test
    void test_embargo() {
        setup2pGame("Embargo");
        Card embargo = getCardFromSupply("Embargo");
        embargo.moveTo(p1.hand);

        playTurn("HAND:Embargo", "BUTTON:y", "SUPPLY:Silver", "");

        assertPlayerState(p1, 2, 0, 1);
        assertCardInLocation(embargo, trashedCards);

        endTurn();
    }

    @Test
    void test_embargo_buy_cursed_pile() {
        setup2pGame("Embargo");
        Card embargo = getCardFromSupply("Embargo");
        Card gold = getCardFromSupply("Gold");

        embargo.moveTo(p1.hand);
        playTurn("HAND:Embargo","BUTTON:y", "SUPPLY:Silver", "");
        endTurn();

        gold.moveTo(p2.hand);
        playTurn("HAND:Gold", "SUPPLY:Silver");
        assertPlayerState(p2, 0, 1, 0);
        endTurn();

        boolean hasCurse = p2.discard.stream().anyMatch(c -> c.getName().equals("Curse"));
        assertTrue(hasCurse, "Le joueur 2 aurait dû recevoir une malédiction en achetant sur une pile sous Embargo");
    }

    @Test
    void test_embargo_buy_but_curse_pile_empty() {
        setup2pGame("Embargo");
        Card embargo = getCardFromSupply("Embargo");
        Card gold = getCardFromSupply("Gold");

        List<Card> copy = new ArrayList<>(getSupply("Curse"));
        for (Card c : copy) {
            c.moveTo(trashedCards);
        }

        embargo.moveTo(p1.hand);
        playTurn("HAND:Embargo", "BUTTON:y", "SUPPLY:Silver", "");
        endTurn();

        gold.moveTo(p2.hand);
        playTurn("HAND:Gold", "SUPPLY:Silver");

        assertEquals("Silver", p2.discard.getLast().getName());

        boolean hasCurse = p2.discard.stream().anyMatch(card -> card.getName().equals("Curse"));
        assertFalse(hasCurse, "Le joueur ne doit pas recevoir de malédiction si la pile est vide");

        endTurn();
    }

    @Test
    void test_pirate_ship_attack() {
        setup2pGame("Pirate Ship");
        Card pirateShip = getCardFromSupply("Pirate Ship");
        Card p2Copper = getCardFromSupply("Copper");
        Card p2Estate = getCardFromSupply("Estate");

        pirateShip.moveTo(p1.hand);

        p2.draw.clear();
        p2Estate.moveTo(p2.draw);
        p2Copper.moveTo(p2.draw);

        playTurn("HAND:Pirate Ship", "BUTTON:attack", "BUTTON:Copper", "");

        assertCardInLocation(p2Copper, trashedCards);
        assertCardInLocation(p2Estate, p2.discard);

        endTurn();
    }

    @Test
    void test_pirate_ship_attack_but_no_treasure_found() {
        setup2pGame("Pirate Ship");
        Card pirateShip = getCardFromSupply("Pirate Ship");
        pirateShip.moveTo(p1.hand);

        p2.draw.clear();
        Card e1 = getCardFromSupply("Estate");
        Card e2 = getCardFromSupply("Estate");
        e1.moveTo(p2.hand);
        e2.moveTo(p2.draw);

        playTurn("HAND:Pirate Ship", "BUTTON:attack", "");

        assertCardInLocation(e1, p2.discard);
        assertCardInLocation(e2, p2.discard);

        assertPlayerState(p1, 0, 0, 1);

        endTurn();
    }

    @Test
    void test_pirate_ship_coin_mode() {
        setup2pGame("Pirate Ship");
        Card pirateShip1 = getCardFromSupply("Pirate Ship");
        Card pirateShip2 = getCardFromSupply("Pirate Ship");
        Card p2Copper = getCardFromSupply("Copper");

        pirateShip1.moveTo(p1.hand);
        p2.draw.clear();
        p2Copper.moveTo(p2.draw);

        playTurn("HAND:Pirate Ship", "BUTTON:attack", "BUTTON:Copper", "");
        assertCardInLocation(p2Copper, trashedCards);
        endTurn();

        playTurn("");
        endTurn();

        pirateShip2.moveTo(p1.hand);
        playTurn("HAND:Pirate Ship", "BUTTON:coins", "");

        assertPlayerState(p1, 1, 0, 1);
        endTurn();
    }

    @Test
    void test_pirate_ship_attack_with_lighthouse() {
        setup3pGame("Pirate Ship", "Lighthouse");
        Card pirateShip = getCardFromSupply("Pirate Ship");
        Card lighthouse = getCardFromSupply("Lighthouse");

        lighthouse.moveTo(p1.hand);
        playTurn("HAND:Lighthouse", "");
        endTurn();

        Card p1Copper = p1.hand.stream()
                .filter(c -> c.getName().equals("Copper"))
                .findFirst().get();
        Card p3Copper = p3.hand.stream()
                .filter(c -> c.getName().equals("Copper"))
                .findFirst().get();

        p1.draw.clear();
        p3.draw.clear();
        p1Copper.moveTo(p1.draw);
        p3Copper.moveTo(p3.draw);

        pirateShip.moveTo(p2.hand);

        playTurn("HAND:Pirate Ship", "BUTTON:attack", "BUTTON:Copper", "");

        assertCardInLocation(p1Copper, p1.draw);
        assertCardInLocation(p3Copper, trashedCards);

        endTurn();
    }

    @Test
    void test_navigator_discard_all() {
        setup2pGame("Navigator");
        Card navigator = getCardFromSupply("Navigator");
        navigator.moveTo(p1.hand);

        int discardSizeBefore = p1.discard.size();

        playTurn("HAND:Navigator", "BUTTON:y", "");

        assertPlayerState(p1, 2, 0, 1);
        assertEquals(discardSizeBefore + 5, p1.discard.size());
        assertTrue(p1.draw.isEmpty());
        endTurn();
    }

    @Test
    void test_pearl_diver_move_to_top() {
        setup2pGame("Pearl Diver");
        Card pearlDiver = getCardFromSupply("Pearl Diver");
        Card bottomCard = getCardFromSupply("Estate");
        Card topCard = getCardFromSupply("Copper");

        p1.draw.clear();
        bottomCard.moveTo(p1.draw);
        topCard.moveTo(p1.draw);
        pearlDiver.moveTo(p1.hand);

        playTurn("HAND:Pearl Diver", "BUTTON:y", "");

        assertPlayerState(p1, 0, 1, 1);
        assertCardInLocation(topCard, p1.hand);

        assertSame(bottomCard, p1.draw.getLast());
        endTurn();
        assertFalse(p1.discard.contains(pearlDiver));
    }

    @Test
    void test_pearl_diver_leave_at_bottom() {
        setup2pGame("Pearl Diver");
        Card pearlDiver = getCardFromSupply("Pearl Diver");
        Card bottomCard = getCardFromSupply("Estate");
        Card topCard = getCardFromSupply("Copper");

        p1.draw.clear();
        bottomCard.moveTo(p1.draw);
        topCard.moveTo(p1.draw);
        pearlDiver.moveTo(p1.hand);

        playTurn("HAND:Pearl Diver", "BUTTON:n", "");

        assertPlayerState(p1, 0, 1, 1);
        assertCardInLocation(topCard, p1.hand);

        assertSame(bottomCard, p1.draw.getFirst());
        endTurn();
    }

    @Test
    void test_ambassador_attack_and_return_copies() {
        setup3pGame("Ambassador");
        Card ambassador = getCardFromSupply("Ambassador");

        p2.discard.clear();
        p3.discard.clear();

        ambassador.moveTo(p1.hand);

        playTurn("HAND:Ambassador", "HAND:Copper", "", "");

        assertPlayerState(p1, 0, 0, 1);

        assertEquals(1, p2.discard.size(), "Le joueur 2 a dû recevoir 1 carte");
        assertEquals(1, p3.discard.size(), "Le joueur 3 a dû recevoir 1 carte");

        assertEquals("Copper", p2.discard.getLast().getName());
        assertEquals("Copper", p3.discard.getLast().getName());

        endTurn();
        assertCardInLocation(ambassador, p1.discard);
    }

    @Test
    void test_ghost_ship_victim_has_few_cards() {
        setup2pGame("Ghost Ship");
        Card ghostShip = getCardFromSupply("Ghost Ship");

        discardHand(p2);
        Card c1 = getCardFromSupply("Estate");
        Card c2 = getCardFromSupply("Copper");
        Card c3 = getCardFromSupply("Silver");
        c1.moveTo(p2.hand);
        c2.moveTo(p2.hand);
        c3.moveTo(p2.hand);

        ghostShip.moveTo(p1.hand);

        playTurn("HAND:Ghost Ship", "");

        assertPlayerState(p1, 0, 0, 1);

        assertEquals(3, p2.hand.size());
        assertCardInLocation(c1, p2.hand);

        endTurn();
    }

    @Test
    void test_ambassador_with_lighthouse() {
        setup3pGame("Ambassador", "Lighthouse");
        Card ambassador = getCardFromSupply("Ambassador");
        Card curse1 = getCardFromSupply("Curse");
        Card lighthouse = getCardFromSupply("Lighthouse");

        lighthouse.moveTo(p1.hand);
        playTurn("HAND:Lighthouse", "");
        endTurn();

        playTurn("");
        endTurn();

        ambassador.moveTo(p3.hand);
        curse1.moveTo(p3.hand);
        playTurn("HAND:Ambassador", "HAND:Curse", "", "");

        assertTrue(p1.discard.isEmpty() || !p1.discard.getLast().getName().equals("Curse"));

        assertEquals("Curse", p2.discard.getLast().getName());

        endTurn();
    }

    @Test
    void test_sea_witch_with_lighthouse() {
        setup3pGame("Sea Witch", "Lighthouse");
        Card seaWitch = getCardFromSupply("Sea Witch");
        Card lighthouse = getCardFromSupply("Lighthouse");

        lighthouse.moveTo(p1.hand);
        playTurn("HAND:Lighthouse", "");
        endTurn();

        seaWitch.moveTo(p2.hand);
        playTurn("HAND:Sea Witch", "");

        assertTrue(p1.discard.isEmpty() || !p1.discard.getLast().getName().equals("Curse"));
        assertEquals("Curse", p3.discard.getLast().getName());

        endTurn();
        assertCardInLocation(seaWitch, p2.inPlay);
    }

    @Test
    void test_ghost_ship_with_lighthouse() {
        setup3pGame("Ghost Ship", "Lighthouse");
        Card ghostShip = getCardFromSupply("Ghost Ship");
        Card lighthouse = getCardFromSupply("Lighthouse");

        lighthouse.moveTo(p1.hand);
        playTurn("HAND:Lighthouse", "");
        endTurn();

        playTurn("");
        endTurn();

        ghostShip.moveTo(p3.hand);

        Card cardToDeck1 = p2.hand.get(0);
        Card cardToDeck2 = p2.hand.get(1);

        playTurn("HAND:Ghost Ship", "HAND:" + cardToDeck1.getName(), "HAND:" + cardToDeck2.getName(), "");

        assertEquals(5, p1.hand.size());

        assertEquals(3, p2.hand.size());
        assertCardInLocation(cardToDeck1, p2.draw);
        assertCardInLocation(cardToDeck2, p2.draw);

        endTurn();
    }

    @Test
    void test_sea_hag_with_lighthouse() {
        setup3pGame("Sea Hag", "Lighthouse");
        Card seaHag = getCardFromSupply("Sea Hag");
        Card lighthouse = getCardFromSupply("Lighthouse");

        lighthouse.moveTo(p1.hand);
        playTurn("HAND:Lighthouse", "");
        endTurn();

        seaHag.moveTo(p2.hand);

        int p1DiscardSize = p1.discard.size();

        playTurn("HAND:Sea Hag", "");

        assertEquals(p1DiscardSize, p1.discard.size());
        boolean p1HasCurse = p1.draw.stream().anyMatch(c -> c.getName().equals("Curse"));
        assertFalse(p1HasCurse, "Le joueur 1 est immunisé et ne doit pas recevoir de malédiction");

        assertEquals("Curse", p3.draw.getLast().getName());

        endTurn();
    }

    @Test
    void test_outpost_triple_round() {
        setup2pGame("Outpost");
        Card[] c = getCardsFromSupply("Outpost", 2);

        c[0].moveTo(p1.hand);

        // p1
        playTurn("HAND:Outpost", "");
        assertPlayerState(p1, 0, 0, 1);
        endTurn();
        assertEquals(3, p1.hand.size());
        assertCardInLocation(c[0], p1.inPlay);

        // p1 (tour 2)
        c[1].moveTo(p1.hand);
        playTurn("HAND:Outpost", "");
        assertPlayerState(p1, 0, 0, 1);
        assertCardInLocation(c[0], p1.inPlay);
        assertCardInLocation(c[1],p1.inPlay);
        endTurn();
        assertFalse(p1.inPlay.contains(c[0]));


        // p2 (vérifie que p1 ne peut pas jouer 3 fois d'affilée)
        playTurn("HAND:Copper", "");
        assertCardInLocation(c[1],p1.inPlay);
        assertPlayerState(p1, 0, 0, 0);
        assertPlayerState(p2, 1, 1, 1);
        endTurn();

    }

    @Test
    void test_blockade_no_curse_with_3_players_out_of_turn() {
        // 1. Setup avec 3 joueurs
        setup3pGame("Blockade", "Ambassador", "Silver");
        Card blockade = getCardFromSupply("Blockade");
        Card ambassador = getCardFromSupply("Ambassador");
        Card silverSetAside = getCardFromSupply("Silver");
        Card curse = getCardFromSupply("Curse");

        // --- TOUR DU JOUEUR 1 ---
        blockade.moveTo(p1.hand);
        // P1 met un Silver de côté avec Blockade
        playTurn("HAND:Blockade", "SUPPLY:Silver", "");

        assertCardInLocation(blockade, p1.inPlay);
        assertCardInLocation(silverSetAside, p1.cardsSetAside);
        endTurn();

        // --- TOUR DU JOUEUR 2 ---
        // P2 prépare son Ambassador et un Silver
        Card p2Silver = getCardFromSupply("Silver");
        p2Silver.moveTo(p2.hand);
        ambassador.moveTo(p2.hand);

        // P2 joue Ambassador : il révèle son Silver, le remet en réserve.
        // Cela force P1 ET P3 à gagner un Silver.
        playTurn(
                "HAND:Ambassador",
                "HAND:Silver",   // Révèle le Silver
                "HAND:Silver",   // Retourne le Silver au Supply
                "",              // Ne retourne pas de 2ème exemplaire (optionnel)
                ""               // Fin de phase achat
        );

        // --- VÉRIFICATIONS ---

        // Vérification pour Joueur 1 (Propriétaire du Blockade)
        boolean p1GainedSilver = p1.discard.stream().anyMatch(c -> c.getName().equals("Silver"));
        assertTrue(p1GainedSilver, "Joueur 1 devrait avoir reçu un Silver via Ambassador.");
        assertFalse(p1.discard.contains(curse), "Joueur 1 ne doit pas avoir de Curse (pas son tour).");

        // Vérification pour Joueur 3 (Simple témoin)
        boolean p3GainedSilver = p3.discard.stream().anyMatch(c -> c.getName().equals("Silver"));
        assertTrue(p3GainedSilver, "Joueur 3 devrait aussi avoir reçu un Silver via Ambassador.");
        assertFalse(p3.discard.contains(curse), "Joueur 3 ne doit évidemment pas avoir de Curse.");

        // Vérification de la réserve de Malédictions
        // (Elle doit être intacte, personne n'en a pris)
        assertCardInLocation(curse, getSupply("Curse"));

        // Blockade est toujours en attente du prochain tour de P1
        assertCardInLocation(blockade, p1.inPlay);
    }

}
