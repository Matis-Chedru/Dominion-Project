package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

import java.util.List;

public class LookoutState extends PlayerState {

    private final List<Card> topCards;
    private boolean trashChoice;

    public LookoutState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Choose a card to trash");
        topCards = currentPlayer.drawCards(3);
        getGame().getTemporaryCards().addAll(topCards);
        trashChoice = true;
    }

    @Override
    public void temporaryCardWasChosen(String cardName) {
        List<String> availableCards = topCards.stream().map(Card::getName).toList();
        if (!availableCards.isEmpty() && availableCards.contains(cardName)) {
            Card cardToPlay = topCards.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
            if (trashChoice) {
                trashChoice = false;
                currentPlayer.moveToTrash(cardToPlay);
                getGame().getTemporaryCards().remove(cardToPlay);
                getGame().instructionProperty().setValue("Choose a card to discard");
            } else {
                currentPlayer.moveToDiscard(cardToPlay);
                getGame().getTemporaryCards().remove(cardToPlay);
                currentPlayer.moveToDraw(getGame().getTemporaryCards());
                getGame().getTemporaryCards().clear();
                complete();
            }
        }
    }
 }