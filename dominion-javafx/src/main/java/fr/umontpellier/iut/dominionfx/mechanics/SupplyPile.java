package fr.umontpellier.iut.dominionfx.mechanics;

import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.function.Supplier;

public class SupplyPile extends SimpleListProperty<Card> {
    private final String name;
    private final int cost;
    private final IntegerProperty nbEmbargoTokens;

    public SupplyPile(Supplier<Card> cardSupplier, int numberOfCopies) {
        super(FXCollections.observableArrayList());
        nbEmbargoTokens = new SimpleIntegerProperty(0);
        Card card = cardSupplier.get();
        name = card.getName();
        cost = card.getCost();
        for (int i = 0; i < numberOfCopies; i++) {
            cardSupplier.get().moveTo(this.get());
        }
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void addEmbargoToken() {
        nbEmbargoTokens.setValue(nbEmbargoTokens.getValue() + 1);
    }

    public int getNbEmbargoTokens() {
        return nbEmbargoTokens.getValue();
    }

    public IntegerProperty nbEmbargoTokensProperty() {
        return nbEmbargoTokens;
    }
}
