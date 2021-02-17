package net.starype.quiz.api.game.guessreceived;

import net.starype.quiz.api.game.MutableGuessContext;

/**
 * GuessReceivedHead which adds the player's answer to answers if there is no answer linked to the
 * current player in answers. Else replace te old answer.
 */
public class UpdateAnswers extends SimpleReceivedAction {

    @Override
    public void execute(RoundState roundState, MutableGuessContext guessContext) {
        roundState.updateRoundAnswers(guessContext.getPlayer(), guessContext.getAnswer());
    }
}
