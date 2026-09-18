package fr.umontpellier.iut.dominionfx.mechanics.playerstate;

import fr.umontpellier.iut.dominionfx.mechanics.Player;
import fr.umontpellier.iut.dominionfx.mechanics.cards.Card;

import java.util.List;

public class PirateReactionPhase extends PlayerState {

    private final Player reactingCardOwner;
    private final Card gainedCard;

    public PirateReactionPhase(Player currentPlayer, Player reactingCardOwner, Card gainedCard) {
        super(currentPlayer);
        getGame().instructionProperty().setValue("%s, do you want to react to %s playing your Pirate?".formatted(reactingCardOwner.getName(),gainedCard.getName()));
        this.gainedCard = gainedCard;
        this.reactingCardOwner = reactingCardOwner;
        processReactingCard();
    }

    @Override
    public void answer(String choice) {
        currentPlayer.setWaitForYesOrNo(false);
        if (choice.equals("Yes")) {
            Card cardToPlay = reactingCardOwner.getCardFromHand("Pirate");
            cardToPlay.reactToPlayerGainCard(currentPlayer, gainedCard, reactingCardOwner)
                    .thenRun(this::complete);
        } else
            complete();
    }

    public void processReactingCard() {
        List<Card> reactingCards = reactingCardOwner.getHand().stream()
                .filter(c -> c.canReactToPlayerGainCard(currentPlayer, gainedCard, reactingCardOwner))
                .toList();
        if (reactingCards.isEmpty())
            complete();
        else {
            currentPlayer.setWaitForYesOrNo(true);
        }
    }
}
