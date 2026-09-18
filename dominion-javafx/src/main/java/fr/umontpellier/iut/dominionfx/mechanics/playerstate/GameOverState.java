package fr.umontpellier.iut.dominionfx.mechanics.playerstate;

import fr.umontpellier.iut.dominionfx.mechanics.Player;

public class GameOverState extends PlayerState {
    public GameOverState(Player currentPlayer) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Fin de la partie");
    }
}
