package fr.umontpellier.iut.dominion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.umontpellier.iut.dominion.cards.Card;

class GameProfTest extends BaseTestClass {

    @Test
    void testAvailableSupplyCards() {
        setup3pGame(
                "Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Fishing Village",
                "Haven",
                "Island",
                "Lighthouse");
        List<Card> expected = new ArrayList<>();
        expected.add(getSupply("Copper").getLast());
        expected.add(getSupply("Silver").getLast());
        expected.add(getSupply("Gold").getLast());
        expected.add(getSupply("Estate").getLast());
        expected.add(getSupply("Duchy").getLast());
        expected.add(getSupply("Province").getLast());
        expected.add(getSupply("Curse").getLast());
        expected.add(getSupply("Astrolabe").getLast());
        expected.add(getSupply("Bazaar").getLast());
        expected.add(getSupply("Blockade").getLast());
        expected.add(getSupply("Caravan").getLast());
        expected.add(getSupply("Corsair").getLast());
        expected.add(getSupply("Cutpurse").getLast());
        expected.add(getSupply("Fishing Village").getLast());
        expected.add(getSupply("Haven").getLast());
        expected.add(getSupply("Island").getLast());
        expected.add(getSupply("Lighthouse").getLast());

        assertSameElementsInAnyOrder(expected, game.getAvailableSupplyCards());
    }

    @Test
    void testAvailableSupplyPilesWithEmptyPiles() {
        setup3pGame(
                "Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Fishing Village",
                "Haven",
                "Island",
                "Lighthouse");

        // vider les piles de Astrolabe et Estate
        getSupply("Astrolabe").clear();
        getSupply("Estate").clear();

        List<Card> expected = new ArrayList<>();
        expected.add(getSupply("Copper").getLast());
        expected.add(getSupply("Silver").getLast());
        expected.add(getSupply("Gold").getLast());
        expected.add(getSupply("Duchy").getLast());
        expected.add(getSupply("Province").getLast());
        expected.add(getSupply("Curse").getLast());
        expected.add(getSupply("Bazaar").getLast());
        expected.add(getSupply("Blockade").getLast());
        expected.add(getSupply("Caravan").getLast());
        expected.add(getSupply("Corsair").getLast());
        expected.add(getSupply("Cutpurse").getLast());
        expected.add(getSupply("Fishing Village").getLast());
        expected.add(getSupply("Haven").getLast());
        expected.add(getSupply("Island").getLast());
        expected.add(getSupply("Lighthouse").getLast());

        assertSameElementsInAnyOrder(expected, game.getAvailableSupplyCards());
    }

    @Test
    void test_isFinished_false() {
        setup2pGame("Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Fishing Village",
                "Haven",
                "Island",
                "Lighthouse");
        assertFalse(game.isFinished());
    }

    @Test
    void test_isFinished_3_empty_supply_piles() {
        setup2pGame(
                "Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Fishing Village",
                "Haven",
                "Island",
                "Lighthouse");

        assertFalse(game.isFinished());
        
        getSupply("Astrolabe").clear();
        assertFalse(game.isFinished());
        
        getSupply("Estate").clear();
        assertFalse(game.isFinished());
        
        getSupply("Silver").clear();
        assertTrue(game.isFinished());
    }

    @Test
    void test_isFinished_empty_province_pile() {
        setup2pGame("Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Fishing Village",
                "Haven",
                "Island",
                "Lighthouse");
        
        assertFalse(game.isFinished());

        getSupply("Province").clear();
        assertTrue(game.isFinished());
    }

    @Test
    void test_moveToNextPlayer_2_players_game() {
        setup2pGame();
        assertSame(p1.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
        assertSame(p2.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
        assertSame(p1.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
    }

    @Test
    void test_moveToNextPlayer_3_players_game() {
        setup3pGame();
        assertSame(p1.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
        assertSame(p2.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
        assertSame(p3.player, game.getCurrentTurnPlayer());
        game.moveToNextPlayer();
        assertSame(p1.player, game.getCurrentTurnPlayer());
    }
}