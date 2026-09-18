package fr.umontpellier.iut.dominion;

import java.util.ArrayList;
import java.util.function.Supplier;

import fr.umontpellier.iut.dominion.cards.Card;

public class SupplyPile extends ArrayList<Card> {
    private final String name;
    private final int cost;
    private int jetonEmbargo; // Ajout du compteur de jetons

    public SupplyPile(Supplier<Card> cardSupplier, int numberOfCopies) {
        Card card = cardSupplier.get();
        name = card.getName();
        cost = card.getCost();
        jetonEmbargo = 0;
        for (int i = 0; i < numberOfCopies; i++) {
            cardSupplier.get().moveTo(this);
        }
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void addEmbargoToken() {
        jetonEmbargo++;
    }

    public int getJetonEmbargo() {
        return jetonEmbargo;
    }
}