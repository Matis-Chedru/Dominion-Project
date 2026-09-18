package fr.umontpellier.iut.dominionfx.mechanics;

import fr.umontpellier.iut.dominionfx.IGame;
import fr.umontpellier.iut.dominionfx.IPlayer;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;
import fr.umontpellier.iut.dominionfx.mechanics.cards.FactorySupplyPile;
import fr.umontpellier.iut.dominionfx.mechanics.playerstate.StartTurnState;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Class représentant une partie de Dominion
 */
public class Game extends Task<Void> implements IGame {
    /**
     * Tableau contenant les joueurs de la partie
     */
    private final ArrayList<Player> players;

    public ArrayList<IPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Le joueur dont c'est actuellement le tour
     */
    private final ObjectProperty<Player> currentTurnPlayer;

    private final ObjectProperty<Player> previousTurnPlayer;

    /**
     * Instruction à afficher au joueur actif
     */
    private final ObjectProperty<String> instruction;

    /**
     * Permet de savoir si la partie est terminée
     */
    private final BooleanProperty gameOver;

    private boolean samePlayerShouldPlayExtraTurn = false;

    /**
     * Liste des piles dans la réserve du jeu.
     * <p>
     * On suppose ici que toutes les listes contiennent des copies de la même
     * carte. Ces piles peuvent être vides en cours de partie si toutes les
     * cartes de la pile ont été achetées ou gagnées par les joueurs.
     */
    private final List<SupplyPile> supplyPiles;

    /**
     * Liste des cartes qui ont été écartées (trash)
     */
    private final List<Card> trashedCards;

    /**
     * Constructeur
     *
     * @param playerNames  liste des noms des joueurs qui participent à la
     *                     partie. Le constructeur doit créer les objets
     *                     correspondant aux joueurs
     * @param kingdomPiles nom des cartes "royaume" à utiliser pour la partie
     */
    public Game(String[] playerNames, String[] kingdomPiles) {
        instruction = new SimpleObjectProperty<>("");
        gameOver = new SimpleBooleanProperty(false);
        int nbPlayers = playerNames.length;
        trashedCards = new ArrayList<>();

        // Création des piles de réserve
        supplyPiles = new ArrayList<>();
        for (String cardName : kingdomPiles) {
            supplyPiles.add(FactorySupplyPile.createSupplyPile(cardName, nbPlayers));
        }
        supplyPiles.sort(new PileComparator());
        // Ajout des piles communes à la réserve
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Copper", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Silver", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Gold", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Estate", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Duchy", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Province", nbPlayers));
        supplyPiles.add(FactorySupplyPile.createSupplyPile("Curse", nbPlayers));

        BooleanBinding gameOverBinding = Bindings.createBooleanBinding(
                this::isFinished,
                supplyPiles.stream()
                        .map(SupplyPile::sizeProperty)
                        .toArray(Observable[]::new)
        );
        gameOver.bind(gameOverBinding);
        // Création des joueurs
        players = new ArrayList<>(nbPlayers);
        for (String playerName : playerNames)
            players.add(new Player(playerName, this));
        currentTurnPlayer = new SimpleObjectProperty<>();
        previousTurnPlayer = new SimpleObjectProperty<>();
    }

    /**
     * Renvoie l'indice du joueur passé en argument dans le tableau des
     * joueurs, ou -1 si le joueur n'est pas dans le tableau.
     */
    public int getPlayerIndex(Player p) {
        return players.indexOf(p);
    }

    public Player currentPlayer() {
        return currentTurnPlayer.getValue();
    }

    public Player getPreviousTurnPlayer() {
        return previousTurnPlayer.getValue();
    }

    public void setSamePlayerShouldPlayExtraTurn(boolean b) {
        samePlayerShouldPlayExtraTurn = b;
    }

    /**
     * Renvoie la liste des adversaires du joueur passé en argument, dans
     * l'ordre dans lequel ils apparaissent à partir du joueur {@code p}.
     *
     * @param p joueur dont on veut renvoyer la liste des adversaires. On
     *          suppose que {@code p} est bien dans le tableau des joueurs.
     * @return un {@code ArrayList} contenant les autres joueurs de la partie
     *         en commençant par celui qui se trouve juste après {@code p} et
     *         en terminant par celui qui se trouve juste avant (le tableau est
     *         considéré comme cyclique, c'est-à-dire qu'après le premier
     *         élément on revient au premier).
     */
    public List<Player> otherPlayers(Player p) {
        int index = getPlayerIndex(p);
        List<Player> result = new ArrayList<>();
        for (int i = index + 1; i < players.size(); i++)
            result.add(players.get(i));

        for (int i = 0; i < index; i++)
            result.add(players.get(i));

        return result;
    }

    /**
     * @return une liste de cartes contenant la carte du dessus (la dernière de la
     *         liste) de chaque pile non-vide de la réserve (cartes royaume et
     *         cartes communes)
     */
    public List<Card> getAvailableSupplyCards() {
        List<Card> availableCards = new ArrayList<>();

        for (SupplyPile supply : supplyPiles)
            if (!supply.isEmpty())
                availableCards.add(supply.getLast());
        return availableCards;
    }

    public List<String> getAllSupplyPileNames() {
        return supplyPiles.stream().map(SupplyPile::getName).toList();
    }

    public void addEmbargoToken(String cardName) {
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals(cardName)) {
                pile.addEmbargoToken();
                return;
            }
        }
    }

    public int getNumberOfEmbargoTokens(String cardName) {
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals(cardName)) {
                return pile.getNbEmbargoTokens();
            }
        }
        return 0;
    }

    /**
     * Déplace une carte vers la pile de trash (écartée).
     * 
     * @param c la carte à écarter
     */
    void moveCardToTrash(Card c) {
        c.moveTo(trashedCards);
    }

    /**
     * Renvoie une carte de la réserve dont le nom est passé en argument.
     *
     * @param cardName nom de la carte à trouver dans la réserve
     * @return la carte du dessus de la pile de réserve dont le nom est passé en
     *         argument ou {@code null} si aucune carte ne correspond (ou si la pile
     *         de cette carte est vide)
     */
    public Card getCardFromSupply(String cardName) {
        for (SupplyPile pile : supplyPiles)
            if (pile.getName().equals(cardName) && !pile.isEmpty()) {
                return pile.getLast();
            }
        return null;
    }

    public void moveToSupply(Card c) {
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals(c.getName())) {
                c.moveTo(pile);
                return;
            }
        }
        throw new RuntimeException("Card not found in supply: " + c.getName());
    }

    /**
     * Teste si la partie est terminée
     *
     * @return un booléen indiquant si la partie est terminée, c'est-à-dire si
     *         au moins l'une des deux conditions de fin suivantes est vraie
     *         - 3 piles ou plus de la réserve sont vides
     *         - la pile de Provinces de la réserve est vide
     */
    public boolean isFinished() {
        int nbEmptyPiles = 0; // nombre de piles vides
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals("Province") && pile.isEmpty()) {
                // si la pile de Provinces est vide, la partie est terminée
                return true;
            }
            if (pile.isEmpty()) {
                nbEmptyPiles += 1;
            }
        }
        return (nbEmptyPiles >= 3);
    }

    /**
     * Passe au joueur suivant et incrémente le numéro du tour si nécessaire.
     * <p>
     * Cette méthode doit mettre à jour l'attribut {@code currentTurnPlayer} pour
     * qu'il référence le joueur dont c'est le tour après l'appel de la
     * méthode.
     */
    public CompletableFuture<Void> moveToNextPlayer() {
        return currentPlayer().cleanup()
                .thenRun(() -> {
            previousTurnPlayer.setValue(currentPlayer());
            if (!samePlayerShouldPlayExtraTurn) {
                // passe au joueur suivant
                currentTurnPlayer.setValue(getNextPlayer());
            }
            samePlayerShouldPlayExtraTurn = false;
        });
    }

    /**
     * Boucle d'exécution d'une partie.
     * <p>
     * Cette méthode exécute les tours des joueurs jusqu'à ce que la partie soit
     * terminée. Lorsque la partie se termine, la méthode affiche le score
     * final et les cartes possédées par chacun des joueurs.
     */
    public void run() {
        currentTurnPlayer.setValue(players.getFirst());
        currentPlayer().setCurrentState(new StartTurnState(currentPlayer())) ;
        currentPlayer().startTurn();
    }

    @Override
    public Void call() {
        run();
        return null;
    }

    @Override
    public void skipWasChosen() {
        currentPlayer().getCurrentState().skip();
    }

    @Override
    public void temporaryCardWasChosen(String cardName) {
        currentPlayer().getCurrentState().temporaryCardWasChosen(cardName);
    }

    @Override
    public void supplyCardWasChosen(String supplyName) {
        currentPlayer().getCurrentState().supplyCardWasChosen(supplyName);
    }

    @Override
    public ObjectProperty<String> instructionProperty() {
        return instruction;
    }

    @Override
    public ObjectProperty<? extends IPlayer> currentPlayerProperty() {
        return currentTurnPlayer;
    }

    @Override
    public List<SupplyPile> getSupplyPiles() {
        return supplyPiles;
    }

    @Override
    public BooleanProperty gameOverProperty() {
        return gameOver;
    }

    public void moveToNextPlayerState() {
        moveToNextPlayer().thenRun(() -> {
            currentPlayer().setCurrentState(new StartTurnState(currentPlayer())) ;
            currentPlayer().startTurn();
        });
    }

    public Player getNextPlayer() {
        int nextPlayerIndex = (players.indexOf(currentTurnPlayer.getValue()) + 1) % players.size();
        return players.get(nextPlayerIndex);
    }

    public List<String> getCardsFromSupplyMatchingCondition(Predicate<Card> filter) {
        return getAvailableSupplyCards().stream()
                .filter(filter)
                .map(Card::getName)
                .toList();
    }

    public int getNumberOfTrashedCards() { // pour les tests
        return trashedCards.size();
    }

    private final ObservableList<Card> temporaryCards = FXCollections.observableArrayList();

    @Override
    public ObservableList<Card> temporaryCardsProperty() {
        return temporaryCards;
    }

    public ObservableList<Card> getTemporaryCards() {
        return temporaryCards;
    }

    public List<String> getTemporaryCardsNames() {
        return temporaryCards.stream().map(Card::getName).toList();
    }

    public Player getFirstPlayer() {
        return players.getFirst();
    }

    public Player getLastPlayer() {
        return players.getLast();
    }
}