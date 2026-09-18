package fr.umontpellier.iut.dominion;

import java.util.*;
import java.util.stream.Collectors;

import fr.umontpellier.iut.dominion.cards.Card;
import fr.umontpellier.iut.dominion.cards.FactorySupplyPile;
import fr.umontpellier.iut.dominion.gui.Utils;

/**
 * Class représentant une partie de Dominion
 */
public class Game {
    /**
     * Tableau contenant les joueurs de la partie
     */
    private final ArrayList<Player> players;

    /**
     * Le joueur dont c'est actuellement le tour
     */
    private Player currentTurnPlayer;

    /**
     * Numéro du tour courant (commence à 1 et est incrémenté à chaque fois que
     * le tour d'un nouveau joueur commence)
     */
    private int turnNumber = 1;

    /**
     * Messages envoyés dans le log du jeu (pour affichage dans l'interface
     * graphique)
     */
    private ArrayList<String> logLines = new ArrayList<>();

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
     * Scanner permettant de lire les entrées au clavier
     */
    private final Scanner scanner;

    /**
     * Constructeur
     *
     * @param playerNames  liste des noms des joueurs qui participent à la
     *                     partie. Le constructeur doit créer les objets
     *                     correspondant aux joueurs
     * @param kingdomPiles nom des cartes "royaume" à utiliser pour la partie
     */
    public Game(String[] playerNames, String[] kingdomPiles) {
        int nbPlayers = playerNames.length;
        trashedCards = new ArrayList<>();
        scanner = new Scanner(System.in);

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

        // Création des joueurs
        players = new ArrayList<>(nbPlayers);
        for (String playerName : playerNames)
            players.add(new Player(playerName, this));
        currentTurnPlayer = players.getFirst();
    }

    /**
     * Renvoie l'indice du joueur passé en argument dans le tableau des
     * joueurs, ou -1 si le joueur n'est pas dans le tableau.
     */
    public int getPlayerIndex(Player p) {
        return players.indexOf(p);
    }

    /**
     * Renvoie l'objet SupplyPile correspondant au nom passé en argument.
     * Utile pour gérer les jetons Embargo.
     */
    public SupplyPile getSupply(String cardName) {
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals(cardName)) {
                return pile;
            }
        }
        return null;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    /**
     * @return une liste de cartes contenant la carte du dessus (la dernière de la
     *         liste) de chaque pile non-vide de la réserve (cartes royaume et
     *         cartes communes)
     */
    public List<Card> getAvailableSupplyCards() {
        List<Card> cards = new ArrayList<>();
        for (SupplyPile pile : supplyPiles) {
            if (!pile.isEmpty()) {
                cards.add(pile.getLast());
            }
        }
        return cards;
    }
    //TODO a retirer et faire boucler les joueurs dans game pas dans les cartes
    public ArrayList<Player> getListPlayers(){
//        ArrayList<Player> pl = new ArrayList<>();
//        pl.addAll(players);
//        return pl;
        return players;
    }

    /**
     * TODO SUPPRIMER CETTE METHODE ELLE NE FAIT RIEN
     * @param p
     */
    // Distribut une carte Curse à tous les joueurs SAUF le joueur p
    public void putCurse(Player p){
        for (Player e : players){
            if (!e.equals(p)){
                e.moveToDiscard(e.getCardFromSupply("Curse"));
            }
        }
    }

    /**
     * Déplace une carte vers la pile de trash (écartée).
     * 
     * @param c la carte à écarter
     */
    void moveCardToTrash(Card c) {
        c.moveTo(trashedCards);
    }
    public void moveToTrash(Card c){
        moveCardToTrash(c);
    }

    /**
     * Renvoie une représentation de l'état de la partie sous forme d'une chaîne
     * de caractères.
     * <p>
     * Cette représentation comporte
     * — le nom du joueur dont c'est le tour
     * — la liste des piles de la réserve en indiquant pour chacune :
     * — le nom de la carte
     * — le nombre de copies disponibles
     * — le prix de la carte entre parenthèses
     * si la pile n'est pas vide, ou "Empty pile" si la pile est vide.
     * <p>
     * On pourrait par exemple avoir l'affichage suivant :
     * <p>
     * -- Toto's Turn --
     * Ambassador x4(3) [Empty pile] Smugglers x5(3) Blockade x10(4) Navigator
     * x10(4) Sailor x8(4) Treasure Map x10(4) Outpost x10(5) Treasury x10(5) Wharf
     * x10(5) Copper x60(0) Silver x32(3) Gold x20(6) Estate x8(2) Duchy x8(5)
     * Province x2(8) Curse x4(0)
     */
    @Override
    public String toString() {
        String title = String.format("     -- %s's Turn --\n", currentTurnPlayer.getName());
        StringJoiner joiner = new StringJoiner("   ");
        for (List<Card> pile : supplyPiles)
            if (pile.isEmpty())
                joiner.add("[Empty pile]");
            else {
                Card c = pile.getLast();
                joiner.add(String.format("%s x%d(%d)", c.getName(), pile.size(), c.getCost()));
            }
        return title + joiner + "\n";
    }

    /**
     * Méthode utilitaire pour l'interface graphique.
     * À NE PAS MODIFIER.
     */
    public String toJSON() {
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add("\"turn_player\": " + players.indexOf(currentTurnPlayer));
        StringJoiner kingdomJoiner = new StringJoiner(", ");
        for (SupplyPile pile : supplyPiles) {
            kingdomJoiner.add(
                    "{\"card\": \"%s\", \"number\": %d, \"cost\": %d}"
                            .formatted(pile.getName(), pile.size(), pile.getCost()));
        }
        joiner.add("\"supply\": [" + kingdomJoiner + "]");

        StringJoiner playersJoiner = new StringJoiner(", ");
        for (Player p : players) {
            playersJoiner.add(p.toJSON());
        }
        joiner.add("\"players\": [" + playersJoiner + "]");
        joiner.add("\"log\": ["
                + String.join(", ", logLines.stream().map(s -> "\"" + s.replace("\"", "\\\"") + "\"").toList())
                + "]");
        return "{" + joiner + "}";
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

    /**
     * Teste si la partie est terminée
     *
     * @return un booléen indiquant si la partie est terminée, c'est-à-dire si
     *         au moins l'une des deux conditions de fin suivantes est vraie
     *         - 3 piles ou plus de la réserve sont vides
     *         - la pile de Provinces de la réserve est vide
     */
    public boolean isFinished() {
        int emptyPilesCount = 0;
        for (SupplyPile pile : supplyPiles) {
            if (pile.isEmpty()) {
                if (pile.getName().equals("Province")) {
                    return true;
                }
                emptyPilesCount++;
            }
        }
        return emptyPilesCount >= 3;
    }

    /**
     * Passe au joueur suivant et incrémente le numéro du tour si nécessaire.
     * <p>
     * Cette méthode doit mettre à jour l'attribut {@code currentTurnPlayer} pour
     * qu'il référence le joueur dont c'est le tour après l'appel de la
     * méthode.
     */
    public void moveToNextPlayer() {
        turnNumber++;
        if (currentTurnPlayer.hasOutpostPlayed() && !currentTurnPlayer.isOutpostTurn()) {
            currentTurnPlayer.setOutpostTurn(true);
            currentTurnPlayer.setOutpostPlayed(false);
        } else {
            currentTurnPlayer.setOutpostTurn(false);
            currentTurnPlayer.setOutpostPlayed(false);

            int nextPlayerIndex = (currentTurnPlayer.getIndex() + 1) % players.size();
            currentTurnPlayer = players.get(nextPlayerIndex);
        }
    }

    /**
     * Boucle d'exécution d'une partie.
     * <p>
     * Cette méthode exécute les tours des joueurs jusqu'à ce que la partie soit
     * terminée. Lorsque la partie se termine, la méthode affiche le score
     * final et les cartes possédées par chacun des joueurs.
     */
    public void run() {
        while (!isFinished()) {
            // joue le tour du joueur courant
            log("<div class=\"turn-title\">%s (turn %d)</div>".formatted(currentTurnPlayer.toLog(), turnNumber));
            currentTurnPlayer.playTurn();
            currentTurnPlayer.cleanup();
            moveToNextPlayer();
        }

        // affiche le score de chaque joueur dans le log et regroupe toutes les
        // cartes des joueurs dans leur main
        log("<div class=\"turn-title\">Game over</div>");
        for (Player p : players) {
            for (Card c : p.getAllOwnedCards()) {
                p.moveToHand(c);
            }
            log("%s: %d Points".formatted(
                    p.toLog(),
                    p.getVictoryPoints()));
            log(Utils.toLog(p.getAllOwnedCards()));
        }
        // force un rafraîchissement de l'interface graphique
        prompt("Game over", new ArrayList<>(), new ArrayList<>(), 0);
    }

    /**
     * Envoie une chaîne de caractères à l'interface graphique
     * <p>
     * Cette méthode ne fait rien mais elle est utilisée par une sous-classe de
     * Game ({@code GameGUI}) qui communique avec l'interface graphique. Vous
     * ne devez pas l'utiliser ni la modifier.
     *
     * @param message chaîne de caractères à envoyer
     */
    public void sendToUI(String message) {
    }

    /**
     * Lit une ligne de l'entrée standard
     * <p>
     * C'est cette méthode qui doit être appelée à chaque fois qu'on veut lire
     * l'entrée clavier de l'utilisateur (par exemple dans Player.choose), ce
     * qui permet de n'avoir qu'un seul Scanner pour tout le programme.
     *
     * @return une chaîne de caractères correspondant à la ligne suivante de
     *         l'entrée standard (sans le retour à la ligne finale)
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Envoie l'état de la partie pour affichage aux joueurs et à l'UI avant de
     * faire un choix
     *
     * @param instruction l'instruction qui est donnée au joueur
     * @param choices     la liste des choix possibles à afficher à l'utilisateur
     * @param buttons     la liste des boutons à afficher à l'utilisateur
     */
    public void prompt(String instruction, List<String> choices, List<Button> buttons, int activePlayerIndex) {
        // Prépare la version affichée à l'utilisateur
        System.out.println("");
        System.out.println(toString());
        System.out.println(currentTurnPlayer.toString());
        String ligneInstruction = ">>> " + instruction + "<<<";
        System.out.println(ligneInstruction);

        // Prépare la représentation envoyée à l'UI
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        joiner.add("\"game\": " + toJSON());
        joiner.add("\"active_player\": " + activePlayerIndex);
        joiner.add("\"instruction\": \"" + instruction + "\"");
        joiner.add("\"choices\": "
                + choices.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(", ", "[", "]")));
        joiner.add("\"buttons\": " + buttons.stream()
                .map(b -> String.format("{\"label\": \"%s\", \"value\": \"%s\"}", b.label(), b.value()))
                .toList());
        // Envoie la version pour l'UI
        sendToUI(joiner.toString());
    }

    /**
     * Ajoute un message dans le log du jeu qui est affiché dans l'interface
     * graphique. Le message peut contenir du HTML pour le formatage.
     * 
     * @param message
     */
    public void log(String message) {
        logLines.add(message);
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }

    public boolean isImmune(Player p, Card attack) {
        return p.immune();

    }

    public void replaceCardInSupplyPile(Card c) {
        for (SupplyPile pile : supplyPiles) {
            if (pile.getName().equals(c.getName())) {
                c.moveTo(pile);
                return;
            }
        }
    }


    /**
     * TODO SUPPRIMER CETTE METHODE ELLE NE FAIT RIEN
     * @param p
     * @param attack
     */
    public void moveOrShowHand(Player p, Card attack ){
        for (Player e : players) {
            if (e == p) continue;
            if (isImmune(e, attack)) continue;
            Card carteMontre = e.getCardsInHand().stream().filter(card -> card.hasName("Copper")).findFirst().orElse(null);
            if (carteMontre != null) {
                e.moveToDiscard(carteMontre);
            } else {
                e.log("Montre ses cartes " + e.getCardsInHand());
            }
        }
    }

    /**
     * TODO SUPPRIMER CETTE METHODE ELLE NE FAIT RIEN
     * @param p
     */
    //fais défausser une carte à tous les joueurs sauf le joueur p
    public void putUpCardAway(Player p){
        for(Player e : players){
            if(!e.equals(p)) {
                Card upCard = e.getCardFromDeck();
                if (upCard != null) {
                    e.moveToDiscard(upCard);
                }
            }
        }
    }


}
