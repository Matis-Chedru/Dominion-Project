package fr.umontpellier.iut.dominion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.umontpellier.iut.dominion.cards.Card;

/**
 * Carte action qui ne fait rien, pour les tests
 */
class Wasteland extends Card {
    public Wasteland() {
        super("Wasteland", 2);
        addType(CardType.ACTION);
    }

    @Override
    public void play(Player p) {
        // ne fait rien
    }
}

class PlayerProfTest extends BaseTestClass {
    @Test
    void test_getVictoryPoints_start_of_game() {
        setup3pGame();
        assertEquals(3, p1.player.getVictoryPoints());
        assertEquals(3, p2.player.getVictoryPoints());
        assertEquals(3, p3.player.getVictoryPoints());
    }

    @Test
    void test_getVictoryPoints_with_victory_cards() {
        setup3pGame();
        // p1 a 3 Estate + 3 Duchy + 1 Curse = 11 VP
        getCardFromSupply("Duchy").moveTo(p1.hand);
        getCardFromSupply("Duchy").moveTo(p1.draw);
        getCardFromSupply("Duchy").moveTo(p1.discard);
        getCardFromSupply("Curse").moveTo(p1.discard);

        // p2 a 3 Estate + 3 Province + 2 Curse = 19 VP
        getCardFromSupply("Province").moveTo(p2.hand);
        getCardFromSupply("Province").moveTo(p2.draw);
        getCardFromSupply("Province").moveTo(p2.discard);
        getCardFromSupply("Curse").moveTo(p2.draw);
        getCardFromSupply("Curse").moveTo(p2.draw);

        getCardFromSupply("Province").moveTo(trashedCards);

        assertEquals(11, p1.player.getVictoryPoints());
        assertEquals(19, p2.player.getVictoryPoints());
        assertEquals(3, p3.player.getVictoryPoints());
    }

    @Test
    void test_getCardFromDeck_draw_not_empty() {
        setup2pGame();

        Card curse = getCardFromSupply("Curse");
        curse.moveTo(p1.draw);
        assertSame(curse, p1.player.getCardFromDeck());
    }

    @Test
    void test_getCardFromDeck_draw_empty() {
        setup2pGame();

        // vider le deck dans la défausse
        while (!p1.draw.isEmpty()) {
            p1.draw.getLast().moveTo(p1.discard);
        }
        List<Card> cardsInDiscard = new ArrayList<>(p1.discard);
        Card c = p1.player.getCardFromDeck();
        assertCardInLocation(c, p1.draw);
        assertSameElementsInAnyOrder(cardsInDiscard, p1.draw);
        assertTrue(p1.discard.isEmpty());
    }

    @Test
    void test_getCardFromDeck_no_card_to_draw() {
        setup2pGame();

        // vider le deck dans la défausse
        while (!p1.draw.isEmpty()) {
            p1.draw.getLast().moveTo(p1.hand);
        }
        // vider la défausse
        while (!p1.discard.isEmpty()) {
            p1.discard.getLast().moveTo(p1.hand);
        }
        assertNull(p1.player.getCardFromDeck());
    }

    @Test
    void test_playCard() {
        setup2pGame();
        Card silver = getCardFromSupply("Silver");
        silver.moveTo(p1.hand);

        p1.player.playCard(silver);
        assertCardInLocation(silver, p1.inPlay);
        assertEquals(2, p1.player.getMoney());
    }

    @Test
    void test_gainTo() {
        setup2pGame();
        Card curse = getCardFromSupply("Curse");
        Card silver = getCardFromSupply("Silver");

        p1.player.gainTo(curse, p1.discard);
        p1.player.gainTo(silver, p1.hand);
        assertCardInLocation(curse, p1.discard);
        assertCardInLocation(silver, p1.hand);
    }

    @Test
    void test_playTurn_pass() {
        setup2pGame();
        game.setInput("");
        p1.player.playTurn();

        assertPlayerState(p1, 0, 1, 1);
        assertEquals(5, p1.hand.size());
    }

    @Test
    void test_playTurn_play_treasure() {
        setup2pGame();

        Card silver = getCardFromSupply("Silver");
        silver.moveTo(p1.hand);

        game.setInput("HAND:Silver", "");
        p1.player.playTurn();

        assertPlayerState(p1, 2, 1, 1);
        assertCardInLocation(silver, p1.inPlay);
        assertEquals(5, p1.hand.size());
    }

    @Test
    void test_playTurn_play_action_and_treasure() {
        setup2pGame();

        Card silver = getCardFromSupply("Silver");
        Wasteland wasteland = new Wasteland();
        silver.moveTo(p1.hand);
        wasteland.moveTo(p1.hand);

        game.setInput("HAND:Wasteland", "HAND:Silver", "");
        p1.player.playTurn();

        assertPlayerState(p1, 2, 0, 1);
        assertCardInLocation(silver, p1.inPlay);
        assertCardInLocation(wasteland, p1.inPlay);
    }

    @Test
    void test_playTurn_play_treasure_and_action() {
        setup2pGame();

        Card silver = getCardFromSupply("Silver");
        Wasteland wasteland = new Wasteland();
        silver.moveTo(p1.hand);
        wasteland.moveTo(p1.hand);

        game.setInput(
                "HAND:Silver",
                "HAND:Wasteland", // non valide (Action après Treasure)
                "");
        p1.player.playTurn();

        assertPlayerState(p1, 2, 1, 1);
        assertCardInLocation(silver, p1.inPlay);
        assertCardInLocation(wasteland, p1.hand);
    }

    @Test
    void test_playTurn_play_two_treasures() {
        setup2pGame();

        Card silver = getCardFromSupply("Silver");
        Card gold = getCardFromSupply("Gold");
        silver.moveTo(p1.hand);
        gold.moveTo(p1.hand);

        game.setInput(
                "HAND:Silver",
                "HAND:Gold",
                "");
        p1.player.playTurn();

        assertPlayerState(p1, 5, 1, 1);
        assertCardInLocation(silver, p1.inPlay);
        assertCardInLocation(gold, p1.inPlay);
    }

    @Test
    void test_playTurn_play_two_actions() {
        setup2pGame();

        Card wasteland1 = new Wasteland();
        Card wasteland2 = new Wasteland();
        wasteland1.moveTo(p1.hand);
        wasteland2.moveTo(p1.hand);

        game.setInput(
                "HAND:Wasteland",
                "HAND:Wasteland", // non valide
                "");
        p1.player.playTurn();

        assertPlayerState(p1, 0, 0, 1);
        assertCardInLocation(wasteland1, p1.inPlay);
        assertCardInLocation(wasteland2, p1.hand);
    }

    @Test
    void test_playTurn_play_treasures_and_buy() {
        setup2pGame();

        Card gold = getCardFromSupply("Gold");
        gold.moveTo(p1.hand);
        Card silver = getCardFromSupply("Silver");

        game.setInput(
                "HAND:Gold",
                "SUPPLY:Silver",
                "");
        p1.player.playTurn();

        assertPlayerState(p1, 0, 1, 0);
        assertCardInLocation(gold, p1.inPlay);
        assertCardInLocation(silver, p1.discard);
    }

    @Test
    void test_playTurn_play_treasures_and_action_and_buy() {
        setup2pGame();

        Card gold = getCardFromSupply("Gold");
        gold.moveTo(p1.hand);
        Card wasteland = new Wasteland();
        wasteland.moveTo(p1.hand);
        Card silver = getCardFromSupply("Silver");

        game.setInput(
                "HAND:Wasteland",
                "HAND:Gold",
                "SUPPLY:Silver",
                "");
        p1.player.playTurn();

        assertPlayerState(p1, 0, 0, 0);
        assertCardInLocation(gold, p1.inPlay);
        assertCardInLocation(wasteland, p1.inPlay);
        assertCardInLocation(silver, p1.discard);
    }

    @Test
    void test_cleanup() {
        setup2pGame();

        List<Card> cardsInHand = new ArrayList<>(p1.hand);
        List<Card> cardsInDraw = new ArrayList<>(p1.draw);

        game.setInput("HAND:Copper", "");

        p1.player.playTurn();
        assertPlayerState(p1, 1, 1, 1);

        p1.player.cleanup();

        // cartes de la pioche ont été piochées en main
        assertSameElementsInAnyOrder(cardsInDraw, p1.hand);
        // cartes qui étaient en main ont été défaussées
        assertSameElementsInAnyOrder(cardsInHand, p1.discard);
        assertPlayerState(p1, 0, 0, 0);
    }
}
