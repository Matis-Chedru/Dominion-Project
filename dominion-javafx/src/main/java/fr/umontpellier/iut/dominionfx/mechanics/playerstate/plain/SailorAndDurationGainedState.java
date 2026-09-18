package fr.umontpellier.iut.dominionfx.mechanics.playerstate.plain;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.cards.seaside.Sailor;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;

public class SailorAndDurationGainedState extends PlayerState {

    private final Card gainedCard;
    private final Sailor sailorCard;

    public SailorAndDurationGainedState(Player currentPlayer, Card gainedCard, Sailor sailorCard) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Do you want to play " + gainedCard.getName());
        this.gainedCard = gainedCard;
        this.sailorCard = sailorCard;
        currentPlayer.setWaitForYesOrNo(true);
    }

    @Override
    public void answer(String choice) {
        if (choice.equals("Yes")) {
            sailorCard.cannotPlayDurationAnyMore();
            currentPlayer.playCard(gainedCard);
        }
        future.complete(null);
    }
}