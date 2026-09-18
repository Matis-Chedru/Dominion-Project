package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.PirateShip;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

public class PirateShipChoiceState extends PlayerState {

    private final PirateShip pirateShip;

    public PirateShipChoiceState(Player currentPlayer, PirateShip pirateShip) {
        super(currentPlayer);
        int pirateShipCounter = currentPlayer.getPirateShipCounter();
        getGame().instructionProperty().setValue("Choose Yes to add %d %s or No to attack\"".formatted(pirateShipCounter, pirateShipCounter < 2 ? "coin" : "coins"));
        currentPlayer.setWaitForYesOrNo(true);
        this.pirateShip = pirateShip;
    }

    @Override
    public void answer(String choice) {
        currentPlayer.setWaitForYesOrNo(false);
        if (choice.equals("Yes")) {
            currentPlayer.incrementMoney(currentPlayer.getPirateShipCounter());
            pirateShip.setAttacking(false);
        }
        else
            pirateShip.setAttacking(true);
        complete();
    }
}
