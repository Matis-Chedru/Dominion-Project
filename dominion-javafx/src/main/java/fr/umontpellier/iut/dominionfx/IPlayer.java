package fr.umontpellier.iut.dominionfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

public interface IPlayer {
    IntegerProperty numberOfActionsProperty();
    IntegerProperty numberOfBuysProperty();
    IntegerProperty moneyProperty();
    BooleanProperty waitForYesOrNoProperty();
    IntegerProperty pirateShipCounterProperty();

    String getName();
    int getVictoryPoints();
    ObservableList<? extends ICard> getHand();
    ObservableList<? extends ICard> getInPlay();
    ObservableList<? extends ICard> getIslandMat();
    ObservableList<? extends ICard> getNativeVillageMat();
    ObservableList<? extends ICard> getDraw();
    ObservableList<? extends ICard> getDiscard();

    void playTreasuresWasChosen();
    void cardInHandWasChosen(String supplyName);
    void answer(String yes);
}