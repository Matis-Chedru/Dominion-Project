package fr.umontpellier.iut.dominionfx.mechanics.cards;

import fr.umontpellier.iut.dominionfx.ICard;
import fr.umontpellier.iut.dominionfx.mechanics.CardType;
import fr.umontpellier.iut.dominionfx.mechanics.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Représentation des cartes du jeu Dominion
 */
public abstract class Card implements ICard {
    /**
     * Le nom de la carte
     */
    private final String name;

    /**
     * Le coût de la carte à l'achat
     */
    private final int cost;

    private final Set<CardType> types;

    private List<Card> location;

    private final BooleanProperty hasDurationEffect = new SimpleBooleanProperty(false);

    /**
     * Pour la carte selectionnee a afficher (Ambassador, Blockade, Haven, SeaChart).
     * Null par défaut ; initialisée via {@link #initselectedCardNameProperty()}.
     */
    private StringProperty selectedCardName = null;

    /**
     * Constructeur simple
     *
     * @param name le nom de la carte
     * @param cost le coût de la carte
     */
    public Card(String name, int cost) {
        this.name = name;
        this.cost = cost;
        this.types = new HashSet<>();
    }

    /**
     * Getters et setters
     */
    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
    }

    public boolean hasSameNameAs(Card c) {
        return this.name.equals(c.getName());
    }

    public void addType(CardType type) {
        this.types.add(type);
    }

    /**
     * @return {@code true} si la carte est de type {@code type}, {@code false}
     *         sinon
     */
    public boolean hasType(CardType type) {
        return this.types.contains(type);
    }

    public void moveTo(List<Card> newLocation) {
        // Ajout dans la nouvelle liste avant suppression de l'ancienne
        // pour gérer le cas de la liste qui devient vide et fin de partie
        List<Card> oldLocation = location;
        if (oldLocation != null) {
            oldLocation.remove(this);
        }
        location = newLocation;
        newLocation.add(this);
    }

    /** Comme moveTo, mais insère en position 0 (bas de pioche, fond de liste). */
    public void moveToFirst(List<Card> newLocation) {
        List<Card> oldLocation = location;
        if (oldLocation != null) {
            oldLocation.remove(this);
        }
        location = newLocation;
        newLocation.addFirst(this);
    }

    public boolean getHasDurationEffect() {
        return hasDurationEffect.getValue();
    }

    public void setHasDurationEffect(boolean hasDurationEffect) {
        this.hasDurationEffect.setValue(hasDurationEffect);
    }

    /**
     * Initialise la propriété de carte sélectionnée à afficher pour les cartes qui en ont besoin
     * (Ambassador, Blockade, Haven, SeaChart).
     * À appeler dans le constructeur de la sous-classe concernée.
     */
    protected void initselectedCardNameProperty() {
        if (selectedCardName == null) {
            selectedCardName = new SimpleStringProperty();
        }
    }

    /**
     * {@inheritDoc}
     * Retourne {@code null} pour les cartes qui n'ont pas initialisé cette propriété.
     */
    @Override
    public StringProperty selectedCardNameProperty() {
        return selectedCardName;
    }

    /**
     * Exécute l'effet de la carte, jouée par le joueur {@code p}
     *
     * @param p joueur qui exécute l'effet de la carte
     *
     *          L'action de cette méthode dépend de la classe de la carte.
     */
    public abstract CompletableFuture<Void> play(Player p);

    public CompletableFuture<Void> onCleanup(Player p) {
        // Remarque: le fait d'appeler getHasDurationEffect() au lieu de lire
        // directement hasDurationEffect permet de redéfinir la méthode dans
        // les sous-classes si nécessaire (cf. Blockade)
        if (!getHasDurationEffect()) {
            p.moveToDiscard(this);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> atStartOfTurn(Player p) {
        return CompletableFuture.completedFuture(null);
    }

    public boolean isProtectionFromAttack() {
        return false;
    }

    /**
     * Cette méthode est appelée sur toutes les cartes en jeu de tous les joueurs
     * quand un joueur joue une carte
     * 
     * @param p          le joueur qui joue la carte
     * @param playedCard la carte qui est jouée
     * @param owner      le joueur qui possède la carte en jeu sur laquelle la
     *                   méthode est appelée
     */
    public void onPlayerPlayCard(Player p, Card playedCard, Player owner) {
    }

    /**
     * Cette méthode est appelée sur toutes les cartes en jeu de tous les joueurs
     * quand un joueur reçoit une carte (gain).
     * 
     * @param p          le joueur qui reçoit la carte
     * @param gainedCard la carte qui est reçue
     * @param owner      le joueur qui possède la carte en jeu sur laquelle la
     *                   méthode est appelée
     */
    public CompletableFuture<Void> onPlayerGainCard(Player p, Card gainedCard, Player owner) {
        return CompletableFuture.completedFuture(null);
    }

    public boolean canReactToPlayerGainCard(Player p, Card gainedCard, Player owner) {
        return false;
    }

    public CompletableFuture<Void> reactToPlayerGainCard(Player p, Card gainedCard, Player owner) {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Renvoie la valeur de la carte en points de victoire (c'est cette méthode
     * qui est appelée sur toutes les cartes du deck d'un joueur pour
     * déterminer le score du joueur en fin de partie)
     * <p>
     * Toutes les cartes qui ne sont pas de type Victoire ont une valeur de
     * 0 (la méthode devra donc être redéfinie pour les cartes Victoire)
     */
    public int getVictoryValue() {
        return 0;
    }

    public boolean canBeInPlay() {
        return true;
    }
}
