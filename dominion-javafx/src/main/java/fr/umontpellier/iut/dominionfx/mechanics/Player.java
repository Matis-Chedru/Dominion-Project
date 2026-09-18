package fr.umontpellier.iut.dominionfx.mechanics;

import fr.umontpellier.iut.dominionfx.IPlayer;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PirateReactionPhase;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.PlayerState;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.StartTurnState;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.TreasurePhase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static fr.umontpellier.iut.dominionfx.mechanics.CardType.TREASURE;

/**
 * Un joueur de Dominion
 */
public class Player implements IPlayer {

    // ========================
    // Champs
    // ========================

    /** Nom du joueur */
    private final String name;

    /** Nombre d'actions disponibles */
    private final IntegerProperty numberOfActions;

    /** Nombre d'achats disponibles */
    private final IntegerProperty numberOfBuys;

    /** Nombre de pièces disponibles pour acheter des cartes */
    private final IntegerProperty money;

    /**
     * Nombre de fois où un Argent ou un Or a été joué ce tour
     * (pour la carte Corsair)
     */
    private int nbSilverOrGoldPlayed;

    /** La partie en cours */
    private final Game game;

    /** Liste des cartes dans la main du joueur */
    private final ObservableList<Card> hand;

    /** Liste des cartes dans la défausse du joueur */
    private final ObservableList<Card> discard;

    /**
     * Liste des cartes dans la pioche du joueur
     * (le dessus de la pioche est en fin de liste)
     */
    private final ObservableList<Card> draw;

    /** Cartes jouées pendant le tour courant */
    private final ObservableList<Card> inPlay;

    /** Cartes mises de côté temporairement */
    private final ObservableList<Card> cardsSetAside;

    /** Cartes mises de côté sur le plateau île (Island Mat) */
    private final ObservableList<Card> islandMat;

    /** Cartes sur le Native Village Mat */
    private final ObservableList<Card> nativeVillageMat;

    private final BooleanProperty waitForYesOrNo;

    /** Cartes gagnées ce tour */
    private final ObservableList<Card> cardsGainedThisTurn;

    /** Cartes achetées ce tour */
    private final ObservableList<Card> cardsBoughtThisTurn;

    /** Nombre de cartes à piocher en fin de tour (normalement 5) */
    private int nbCardsToDrawAtCleanup = 5;

    /** Nombre de jetons Pirate Ship */
    private final IntegerProperty pirateShipCounter;

    /** État courant du joueur */
    private PlayerState currentState;

    // ========================
    // Constructeur
    // ========================

    /**
     * Initialise les piles de cartes du joueur, place 3 Estate et 7 Copper
     * dans la défausse, mélange et pioche 5 cartes en main.
     *
     * @param name le nom du joueur
     * @param game le jeu en cours
     */
    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
        money = new SimpleIntegerProperty(0);
        numberOfActions = new SimpleIntegerProperty(0);
        numberOfBuys = new SimpleIntegerProperty(0);
        hand = FXCollections.observableArrayList();
        discard = FXCollections.observableArrayList();
        draw = FXCollections.observableArrayList();
        inPlay = FXCollections.observableArrayList();
        cardsSetAside = FXCollections.observableArrayList();
        islandMat = FXCollections.observableArrayList();
        nativeVillageMat = FXCollections.observableArrayList();
        waitForYesOrNo = new SimpleBooleanProperty(false);
        pirateShipCounter = new SimpleIntegerProperty(0);
        cardsGainedThisTurn = FXCollections.observableArrayList();
        cardsBoughtThisTurn = FXCollections.observableArrayList();

        for (int i = 0; i < 3; i++)
            moveToDiscard(getCardFromSupply("Estate"));
        for (int i = 0; i < 7; i++)
            moveToDiscard(getCardFromSupply("Copper"));

        Collections.shuffle(discard);
        while (!discard.isEmpty()) {
            discard.getLast().moveTo(draw);
        }
        for (int i = 0; i < 5; i++) {
            draw.getLast().moveTo(hand);
        }
    }

    // ========================
    // Getters / Properties
    // ========================

    public String getName() {
        return name;
    }


    public Game getGame() {
        return game;
    }

    public int getMoney() {
        return money.getValue();
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public int getNumberOfActions() {
        return numberOfActions.getValue();
    }

    @Override
    public IntegerProperty numberOfActionsProperty() {
        return numberOfActions;
    }

    public int getNumberOfBuys() {
        return numberOfBuys.getValue();
    }

    @Override
    public IntegerProperty numberOfBuysProperty() {
        return numberOfBuys;
    }

    public int getPirateShipCounter() {
        return pirateShipCounter.getValue();
    }

    public IntegerProperty pirateShipCounterProperty() {
        return pirateShipCounter;
    }

    /** Uniquement pour la carte Corsair */
    public int getNbSilverOrGoldPlayed() {
        return nbSilverOrGoldPlayed;
    }


    public BooleanProperty waitForYesOrNoProperty() {
        return waitForYesOrNo;
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    // ========================
    // Setters / Incrementers
    // ========================

    /**
     * Incrémente le nombre d'actions du joueur.
     *
     * @param n nombre d'actions à ajouter (peut être négatif)
     */
    public void incrementActions(int n) {
        numberOfActions.setValue(numberOfActions.getValue() + n);
    }

    /**
     * Incrémente le nombre de pièces du joueur.
     *
     * @param n nombre de pièces à ajouter (peut être négatif)
     */
    public void incrementMoney(int n) {
        money.setValue(money.getValue() + n);
    }

    /**
     * Incrémente le nombre d'achats du joueur.
     *
     * @param n nombre d'achats à ajouter (peut être négatif)
     */
    public void incrementBuys(int n) {
        numberOfBuys.setValue(numberOfBuys.getValue() + n);
    }

    public void incrementPirateShipCounter() {
        pirateShipCounter.setValue(pirateShipCounter.getValue() + 1);
    }

    public void setNbCardsToDrawAtCleanup(int n) {
        nbCardsToDrawAtCleanup = n;
    }

    /** Uniquement pour la carte Corsair */
    public void incrementNbSilverOrGoldPlayed() {
        this.nbSilverOrGoldPlayed += 1;
    }

    public void setWaitForYesOrNo(boolean waitForYesOrNo) {
        this.waitForYesOrNo.set(waitForYesOrNo);
    }

    public void endActionPhase() {
        numberOfActions.setValue(0);
    }

    public void endTreasurePhase() {
        numberOfBuys.setValue(0);
    }

    public void setCurrentState(PlayerState currentState) {
        this.currentState = currentState;
    }

    // ========================
    // Collections de cartes
    // ========================

    /**
     * Renvoie une copie de la main du joueur.
     */
    public List<Card> getCardsInHand() {
        return new ArrayList<>(hand);
    }


    /**
     * Renvoie une copie des cartes en jeu du joueur.
     */
    public List<Card> getCardsInPlay() {
        return new ArrayList<>(inPlay);
    }

    /**
     * Renvoie une copie des cartes mises de côté du joueur.
     */
    public List<Card> getCardsSetAside() {
        return new ArrayList<>(cardsSetAside);
    }

    /**
     * Renvoie une copie des cartes sur le Native Village Mat du joueur.
     */
    public List<Card> getCardsOnNativeVillageMat() {
        return new ArrayList<>(nativeVillageMat);
    }

    public List<Card> getCardsGainedThisTurn() {
        return new ArrayList<>(cardsGainedThisTurn);
    }

    public List<Card> getCardsBoughtThisTurn() {
        return new ArrayList<>(cardsBoughtThisTurn);
    }

    /**
     * Renvoie une liste de toutes les cartes possédées par le joueur
     * (main, défausse, pioche, en jeu, mises de côté, Island Mat, Native Village Mat).
     */
    public List<Card> getAllOwnedCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(hand);
        allCards.addAll(discard);
        allCards.addAll(draw);
        allCards.addAll(inPlay);
        allCards.addAll(cardsSetAside);
        allCards.addAll(islandMat);
        allCards.addAll(nativeVillageMat);
        return allCards;
    }

    // Vues observables (IPlayer)

    @Override
    public ObservableList<Card> getHand() {
        return hand;
    }

    @Override
    public ObservableList<Card> getDraw() {
        return draw;
    }

    @Override
    public ObservableList<Card> getDiscard() {
        return discard;
    }

    @Override
    public ObservableList<Card> getInPlay() {
        return inPlay;
    }

    @Override
    public ObservableList<Card> getNativeVillageMat() {
        return nativeVillageMat;
    }

    @Override
    public ObservableList<Card> getIslandMat() {
        return islandMat;
    }

    // Recherche de cartes

    public Card getCardFromHand(String cardName) {
        return hand.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
    }

    public Card getCardFromInPlay(String cardName) {
        return inPlay.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
    }

    public List<String> getNamesOfCardsInHand() {
        return hand.stream().map(Card::getName).toList();
    }

    public List<String> getNamesOfPlayableCardsInHand() {
        return hand.stream().filter(Card::canBeInPlay).map(Card::getName).toList();
    }

    public List<String> getNamesOfTreasuresInHand() {
        return hand.stream()
                .filter(c -> c.hasType(TREASURE))
                .map(Card::getName)
                .toList();
    }

    public List<String> getProvincesInHand() {
        return hand.stream().filter(c -> c.hasName("Province")).map(Card::getName).toList();
    }

    // ========================
    // Joueurs & victoire
    // ========================

    /**
     * Renvoie les autres joueurs dans l'ordre de jeu, en commençant par
     * celui qui joue immédiatement après le joueur courant.
     */
    public List<Player> getOtherPlayers() {
        return game.otherPlayers(this);
    }

    /**
     * Renvoie tous les joueurs dans l'ordre de jeu, en commençant par le joueur lui-même.
     */
    public List<Player> getPlayers() {
        List<Player> players = game.otherPlayers(this);
        players.addFirst(this);
        return players;
    }

    public boolean isProtectedFromAttack() {
        for (Card c : inPlay) {
            if (c.isProtectionFromAttack()) {
                return true;
            }
        }
        // TODO: éventuellement ajouter la possibilité de révéler Moat
        return false;
    }

    /**
     * Renvoie le nombre total de points de victoire du joueur
     * (somme des {@code getVictoryValue()} de toutes les cartes possédées).
     */
    public int getVictoryPoints() {
        int points = 0;
        for (Card c : getAllOwnedCards())
            points += c.getVictoryValue();
        return points;
    }

    // ========================
    // Pioche & deck
    // ========================

    private void shuffleDiscardIntoDrawIfEmpty() {
        if (draw.isEmpty()) {
            Collections.shuffle(discard);
            moveToDraw(discard);
        }
    }

    /**
     * Renvoie la carte au sommet de la pioche sans la retirer.
     * Mélange la défausse dans la pioche si nécessaire.
     *
     * @return la carte du dessus de la pioche, ou {@code null} si aucune disponible
     */
    public Card getCardFromDeck() {
        shuffleDiscardIntoDrawIfEmpty();
        if (!draw.isEmpty()) {
            return draw.getLast();
        }
        return null;
    }

    /** Pour Pearl Diver : renvoie la carte du bas de la pioche sans la retirer. */
    public Card getBottomCardOfDeck() {
        shuffleDiscardIntoDrawIfEmpty();
        if (!draw.isEmpty()) {
            return draw.getFirst();
        }
        return null;
    }

    /**
     * @param cardName nom de la carte dans la réserve
     * @return la carte du sommet de la pile correspondante, ou {@code null}
     */
    public Card getCardFromSupply(String cardName) {
        return game.getCardFromSupply(cardName);
    }

    /**
     * Pioche {@code n} cartes de la pioche et les retourne dans une nouvelle liste.
     *
     * @param n le nombre de cartes à piocher
     * @return les cartes piochées
     */
    public List<Card> drawCards(int n) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Card c = getCardFromDeck();
            if (c == null) {
                break;
            }
            c.moveTo(drawnCards);
        }
        return drawnCards;
    }

    /**
     * Pioche une carte et la place directement dans la main du joueur.
     *
     * @return la carte piochée, ou {@code null} si la pioche est vide
     */
    public Card drawToHand() {
        Card c = getCardFromDeck();
        if (c != null)
            moveToHand(c);
        return c;
    }

    /**
     * Pioche {@code n} cartes et les place directement dans la main du joueur.
     *
     * @param n le nombre de cartes à piocher
     * @return les cartes ajoutées en main
     */
    public List<Card> drawToHand(int n) {
        List<Card> drawnCards = drawCards(n);
        List<Card> result = new ArrayList<>(drawnCards);
        moveToHand(drawnCards);
        return result;
    }

    // ========================
    // Déplacement de cartes
    // ========================

    public void moveToDiscard(Card c) {
        c.moveTo(discard);
    }

    public void moveToDiscard(List<Card> cards) {
        for (Card c : new ArrayList<>(cards)) {
            moveToDiscard(c);
        }
    }

    public void moveToDraw(Card c) {
        c.moveTo(draw);
    }

    public void moveToDraw(List<Card> cards) {
        for (Card c : new ArrayList<>(cards)) {
            moveToDraw(c);
        }
    }

    public void moveToBottomOfDraw(Card c) {
        c.moveToFirst(draw);
    }

    public void moveToHand(Card c) {
        c.moveTo(hand);
    }

    public void moveToHand(List<Card> cards) {
        for (Card c : new ArrayList<>(cards)) {
            moveToHand(c);
        }
    }

    public void moveToInPlay(Card c) {
        c.moveTo(inPlay);
    }

    public void moveToSetAside(Card c) {
        c.moveTo(cardsSetAside);
    }

    public void moveToIslandMat(Card c) {
        c.moveTo(islandMat);
    }

    public void moveToNativeVillageMat(Card c) {
        c.moveTo(nativeVillageMat);
    }

    public void moveToTrash(Card c) {
        game.moveCardToTrash(c);
    }

    public void moveToTrash(List<Card> cards) {
        for (Card c : new ArrayList<>(cards)) {
            moveToTrash(c);
        }
    }

    public void moveToSupply(Card c) {
        game.moveToSupply(c);
    }

    public void moveFromHandToSupply(String cardName) {
        Card cardToReturnToSupply = hand.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
        moveToSupply(cardToReturnToSupply);
    }

    // ========================
    // Gain & Achat
    // ========================

    /**
     * Le joueur gagne une carte et la place dans {@code location}.
     * Déclenche ensuite les effets "on gain" pour tous les joueurs.
     *
     * @param gainedCard carte à gagner (peut être {@code null})
     * @param location   liste cible
     */
    public CompletableFuture<Void> gainTo(Card gainedCard, List<Card> location) {
        if (gainedCard == null) {
            return CompletableFuture.completedFuture(null);
        }
        gainedCard.moveTo(location);
        cardsGainedThisTurn.add(gainedCard);
        return onGainedCardAllPlayers(gainedCard);
    }

    private CompletableFuture<Void> onGainedCardAllPlayers(Card gainedCard) {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (Player cardOwner : getPlayers()) {
            for (Card cardInPlay : new ArrayList<>(cardOwner.inPlay)) {
                future = future.thenCompose(v -> cardInPlay.onPlayerGainCard(this, gainedCard, cardOwner));
            }
            future = future.thenCompose(v -> reactOnGainCard(cardOwner, gainedCard));
        }
        return future;
    }

    public CompletableFuture<Void> reactOnGainCard(Player owner, Card gainedCard) {
        PirateReactionPhase phase = new PirateReactionPhase(this, owner, gainedCard);
        setCurrentState(phase);
        return phase.getCompletionFuture();
    }

    public CompletableFuture<Void> gainToDiscard(Card c) {
        return gainTo(c, discard);
    }

    public void gainToHand(Card c) {
        gainTo(c, hand);
    }

    public void gainToDraw(Card c) {
        gainTo(c, draw);
    }

    public void gainToSetAside(Card c) {
        gainTo(c, cardsSetAside);
    }

    public void gainTreasure(String cardName) {
        Card gainedCard = getCardFromSupply(cardName);
        if (gainedCard != null) {
            gainToHand(gainedCard);
        }
    }

    /**
     * Le joueur achète la carte {@code cardName} depuis la réserve.
     * Déduit le coût, le nombre d'achats, puis gagne la carte en défausse.
     * Applique également les jetons Embargo si présents.
     */
    public CompletableFuture<Void> buy(String cardName) {
        Card c = getCardFromSupply(cardName);
        incrementBuys(-1);
        money.setValue(money.getValue() - c.getCost());
        return gainToDiscard(c).thenRun(() -> {
            cardsBoughtThisTurn.add(c);
            for (int i = 0; i < game.getNumberOfEmbargoTokens(cardName); i++) {
                Card curse = getCardFromSupply("Curse");
                if (curse != null) {
                    gainToDiscard(curse);
                }
            }
        });
    }

    public boolean areBuysCompleted() {
        return numberOfBuys.getValue() == 0;
    }

    public boolean areActionsCompleted() {
        return numberOfActions.getValue() == 0;
    }

    public List<String> getAvailableSupplyCards() {
        if (numberOfBuys.getValue() > 0) {
            return game.getAvailableSupplyCards().stream()
                    .filter(c -> c.getCost() <= money.getValue())
                    .map(Card::getName)
                    .toList();
        }
        return List.of();
    }

    // ========================
    // Jeu de cartes
    // ========================

    /**
     * Joue une carte depuis la main du joueur.
     * La carte est déplacée vers {@code inPlay}, puis {@code play()} est exécuté,
     * suivi des effets {@code onPlayerPlayCard} pour toutes les cartes en jeu.
     *
     * @param c carte à jouer
     */
    public CompletableFuture<Void> playCard(Card c) {
        moveToInPlay(c);
        return c.play(this)
                .thenRun(() -> {
                    for (Player p : getPlayers()) {
                        for (Card cardInPlay : p.inPlay) {
                            cardInPlay.onPlayerPlayCard(this, c, p);
                        }
                    }
                });
    }

    /**
     * Joue automatiquement tous les trésors de la main du joueur.
     */
    public void playTreasures() {
        List<Card> treasures = hand.stream().filter(c -> c.hasType(TREASURE)).toList();
        for (Card c : treasures)
            playCard(c);
    }

    /**
     * Joue la carte {@code cardName} depuis la main en choisissant automatiquement
     * la phase selon son type (Action ou Trésor).
     */
    public void switchToStateByCardType(String cardName) {
        Card cardToPlay = hand.stream()
                .filter(card -> card.getName().equals(cardName))
                .findFirst()
                .orElseThrow();
        if (cardToPlay.hasType(CardType.ACTION)) {
            incrementActions(-1);
        } else if (cardToPlay.hasType(TREASURE)) {
            setCurrentState(new TreasurePhase(this));
            numberOfActions.setValue(0);
        }
        playCard(cardToPlay).thenRun(currentState::moveToNextPhase);
    }

    /**
     * Joue un trésor spécifique depuis la main (utilisé pendant la TreasurePhase).
     */
    public void playTreasureCard(String cardName) {
        Card cardToPlay = hand.stream()
                .filter(card -> card.getName().equals(cardName))
                .findFirst()
                .orElseThrow();
        numberOfActions.setValue(0);
        playCard(cardToPlay).thenRun(currentState::moveToNextPhase);
    }

    public void answer(String answer) {
        waitForYesOrNoProperty().setValue(false);
        currentState.answer(answer);
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        currentState.cardInHandWasChosen(cardName);
    }

    @Override
    public void playTreasuresWasChosen() {
        currentState.playTreasuresWasChosen();
    }

    // ========================
    // Gestion du tour
    // ========================

    /**
     * Démarre le tour du joueur : réinitialise les compteurs, exécute les
     * effets Duration restants, puis passe en {@link StartTurnState}.
     */
    public void startTurn() {
        numberOfActions.setValue(1);
        numberOfBuys.setValue(1);
        money.setValue(0);
        nbSilverOrGoldPlayed = 0;
        cardsGainedThisTurn.clear();
        cardsBoughtThisTurn.clear();
        execDurationsSequentially().thenRun(() -> setCurrentState(new StartTurnState(this)));
    }

    private CompletableFuture<Void> execDurationsSequentially() {
        List<Card> cards = new ArrayList<>(getInPlay());
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (Card card : cards) {
            future = future.thenCompose(v -> card.atStartOfTurn(this));
        }
        return future;
    }

    /**
     * Phase de Clean-up : remet les compteurs à 0, défausse la main,
     * exécute {@code onCleanup} pour toutes les cartes en jeu, puis pioche
     * la prochaine main (normalement 5 cartes).
     */
    public CompletableFuture<Void> cleanup() {
        numberOfActions.setValue(0);
        money.setValue(0);
        numberOfBuys.setValue(0);
        moveToDiscard(hand);

        return cleanupAllPlayers().thenRun(() -> {
            drawToHand(nbCardsToDrawAtCleanup);
            nbCardsToDrawAtCleanup = 5;
        });
    }

    private CompletableFuture<Void> cleanupAllPlayers() {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (Player p : getPlayers()) {
            future = future.thenCompose(v -> cleanupPlayerCards(p));
        }
        return future;
    }

    private CompletableFuture<Void> cleanupPlayerCards(Player p) {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        for (Card c : new ArrayList<>(p.inPlay)) {
            future = future.thenCompose(v -> c.onCleanup(p));
        }
        return future;
    }
}

