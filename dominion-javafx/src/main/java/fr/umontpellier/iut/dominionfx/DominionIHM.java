package fr.umontpellier.iut.dominionfx;

import fr.umontpellier.iut.dominionfx.mechanics.Game;
import fr.umontpellier.iut.dominionfx.views.ChoosePlayersView;
import fr.umontpellier.iut.dominionfx.views.GameView;
import fr.umontpellier.iut.dominionfx.views.ScoresView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DominionIHM extends Application {
    public static final double screenRatio = .55;/*.95*/
    private ChoosePlayersView choosePlayersView;
    private Stage primaryStage;
    private static Game game;

    private final boolean withChoosePlayersView = true;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        if (withChoosePlayersView) {
            choosePlayersView = new ChoosePlayersView();
            choosePlayersView.setPlayersNamesDefinedListener(whenPlayersNamesAreDefined);
            choosePlayersView.show();
        } else {
            setPlayersAndGame();
            startGame();
        }
    }

    public void startGame() {
        GameView gameView = new GameView(game);
        Scene scene = new Scene(gameView, Screen.getPrimary().getBounds().getWidth() * screenRatio,  Screen.getPrimary().getBounds().getHeight() * screenRatio);

        game.gameOverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ScoresView scoresView = new ScoresView(this);
                scene.setRoot(scoresView);
            }
        });

        game.run();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dominion-Seaside");
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            stopGame();
            event.consume();
        });
        primaryStage.show();
    }

    public void setPlayersAndGame() {
        String[] playerNames;
        if (withChoosePlayersView)
            playerNames = choosePlayersView.getPlayersNames();
        else {
            playerNames = new String[2];
            playerNames[0] = "Apollon";
            playerNames[1] = "Aphrodite";
        }
        String[] kingdomCards = selectKingdomCards();
        game = new Game(playerNames, kingdomCards);
    }

    public DominionIHM() {}

    private static String[] selectKingdomCards() {
        // Liste de toutes les cartes disponibles
        // Cartes royaume à utiliser
        // Option 1. Aucune carte royaume (uniquement les cartes communes)
        // String[] kingdomCards = new String[]{};

        // Option 2. Liste explicite de cartes royaume à utiliser (le nombre de cartes peut être quelconque)
        String[] kingdomCards = new String[]{"Ambassador", "Island", "Native Village", "Navigator", "Lookout", "Lighthouse", "Pirate", "Pearl Diver", "Pirate Ship", "Ghost Ship"};

        // Option 3. Choix aléatoire de 10 cartes parmi la liste complète allKingdomCards définie précédemment
        // String[] kingdomCards = getRandomKingdomCards();
        return kingdomCards;
    }

    public static String[] getRandomKingdomCards() {
        ArrayList<String> allKingdomCards = getAllKingdomCards();
        Collections.shuffle(allKingdomCards);
        return allKingdomCards.subList(0, 10).toArray(new String[10]);
    }

    public static ArrayList<String> getAllKingdomCards() {
        return new ArrayList<>(Arrays.asList(
                "Ambassador",
                "Astrolabe",
                "Bazaar",
                "Blockade",
                "Caravan",
                "Corsair",
                "Cutpurse",
                "Embargo",
                "Explorer",
                "Fishing Village",
                "Ghost Ship",
                "Haven",
                "Island",
                "Lighthouse",
                "Lookout",
                "Merchant Ship",
                "Monkey",
                "Native Village",
                "Navigator",
                "Outpost",
                "Pearl Diver",
                "Pirate",
                "Pirate Ship",
                "Sailor",
                "Salvager",
                "Sea Chart",
                "Sea Hag",
                "Sea Witch",
                "Smugglers",
                "Tactician",
                "Tide Pools",
                "Treasure Map",
                "Treasury",
                "Warehouse",
                "Wharf"
        ));
    }

    private final ListChangeListener<String> whenPlayersNamesAreDefined = change -> {
        if (!(choosePlayersView.getPlayersNames().length == 0)) {
            setPlayersAndGame();
            startGame();
        }
    };

    public void stopGame() {
//  On pourrait demander à l'utilisateur de confirmer qu'il souhaite bien quitter le jeu
        Platform.exit();
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");
        launch(args);
    }

    public static IGame getGame() {
        return game;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}