package net.starype.quiz.api.game.guessreceived;

import net.starype.quiz.api.game.MutableGuessContext;

/**
 * Update the eligibility of the player guess context according to the eligibility of the round state
 */
public class UpdatePlayerEligibility extends SimpleReceivedAction {
    @Override
    public void execute(RoundState roundState, MutableGuessContext playerGuessContext) {
        playerGuessContext.setEligibility(roundState.isPlayerEligible(playerGuessContext.getPlayer()));
    }
}
