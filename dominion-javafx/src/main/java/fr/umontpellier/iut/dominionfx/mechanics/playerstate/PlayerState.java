package fr.umontpellier.iut.dominionfx.mechanics.playerstate;

import fr.umontpellier.iut.dominionfx.mechanics.Game;
import fr.umontpellier.iut.dominionfx.mechanics.Player;

import java.util.concurrent.CompletableFuture;

public abstract class PlayerState {
    protected CompletableFuture<Void> future;
    protected final Player currentPlayer;

    public PlayerState(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        start();
    }

    public CompletableFuture<Void> start() {
        this.future = new CompletableFuture<>();
        onEnter();
        return future;
    }

    protected void onEnter(){}

    public void complete() {
        future.complete(null);
    }

    public CompletableFuture<Void> getCompletionFuture() {
        return future;
    }

    public void moveToNextPhase() {
        if (currentPlayer.areActionsCompleted()) {
            if (currentPlayer.areBuysCompleted()) {
                endOfCurrentPlayersTurn();
            }
            else
                currentPlayer.setCurrentState(new TreasurePhase(currentPlayer));
        } else
            currentPlayer.setCurrentState(new StartTurnState(currentPlayer));
    }

    public void skip() {
    }

    public void playTreasuresWasChosen() {
        currentPlayer.endActionPhase();
        currentPlayer.setCurrentState(new TreasurePhase(currentPlayer));
        currentPlayer.playTreasures();
    }

    public void answer(String choice) {
    }

    public void cardInHandWasChosen(String cardName) {
    }

    public void supplyCardWasChosen(String cardName) {
    }

    public void temporaryCardWasChosen(String cardName) {
    }

    public void endOfCurrentPlayersTurn() {
        // on vérifie si fin de partie ??
        if (!getGame().isFinished())
            getGame().moveToNextPlayerState(); // fait le cleanUp et startTurn
        else {
            currentPlayer.setCurrentState(new GameOverState(currentPlayer));
        }
    }

    protected Game getGame() {
        return currentPlayer.getGame();
    }
}