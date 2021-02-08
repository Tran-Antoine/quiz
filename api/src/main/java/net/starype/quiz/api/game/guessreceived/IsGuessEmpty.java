package net.starype.quiz.api.game.guessreceived;

import net.starype.quiz.api.game.GuessReceivedParameters;

/**
 * GuessReceivedHead that is linked to a predicate. This predicate is set to true if the guess is empty, it is set
 * to false if not.
 */
public class IsGuessEmpty extends GuessReceivedHead {
    @Override
    public void accept(GuessReceivedParameters parameters) {
        setControlledBoolean(parameters.getCorrectness() == null);
    }
}