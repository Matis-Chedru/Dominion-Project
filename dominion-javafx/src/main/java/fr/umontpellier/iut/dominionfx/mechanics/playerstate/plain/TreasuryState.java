package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.Treasury;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

public class TreasuryState extends PlayerState {

    private final Treasury treasuryCard;

    public TreasuryState(Player currentPlayer, Treasury treasuryCard) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Do you want to put Treasury onto your deck?");
        this.treasuryCard = treasuryCard;
        currentPlayer.setWaitForYesOrNo(true);
    }

    @Override
    public void answer(String choice) {
        currentPlayer.setWaitForYesOrNo(false);
        if (choice.equals("Yes")) {
            currentPlayer.moveToDraw(treasuryCard);
            complete();
        } else {
            treasuryCard.superCleanup(currentPlayer)
                    .thenRun(this::complete);
        }
    }
 }