package fr.umontpellier.iut.dominionfx;

import javafx.beans.property.StringProperty;

public interface ICard {

    String getName();

    /** Propriete de la carte selectionnee a afficher (Ambassador, Blockade, Haven, SeaChart), null pour les autres cartes. */
    default StringProperty selectedCardNameProperty() {
        return null;
    }
}
