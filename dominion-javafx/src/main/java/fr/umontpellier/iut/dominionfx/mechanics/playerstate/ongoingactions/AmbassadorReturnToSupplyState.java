package fr.umontpellier.iut.dominionfx.mechanics.playerstate.ongoingactions;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.AttackCard;

public class AmbassadorReturnToSupplyState extends OnGoingActionPhase {

    private int nbCardsToReveal;
    private final String revealedCardName; // pour le message d'instruction uniquement
    private final AttackCard ambassadorCard;

    public AmbassadorReturnToSupplyState(Player currentPlayer, String revealedCardName, AttackCard ambassadorCard) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("Return up to 2 copies of " + revealedCardName);
        this.nbCardsToReveal = 2;
        this.revealedCardName = revealedCardName;
        this.ambassadorCard = ambassadorCard;
    }

    @Override
    public void skip() {
        if (nbCardsToReveal < 2) {
            ambassadorCard.complete();
        }
    }

    @Override
    public void cardInHandWasChosen(String cardName) {
        if (cardName.equals(revealedCardName)) {
            currentPlayer.moveFromHandToSupply(cardName);
            nbCardsToReveal--;
            if (nbCardsToReveal == 0)
                skip();
            else
                getGame().instructionProperty().setValue("Return 1 copy of "+ revealedCardName);
        }
    }
}