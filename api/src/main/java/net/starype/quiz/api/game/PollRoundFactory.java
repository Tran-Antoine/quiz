package net.starype.quiz.api.game;

import net.starype.quiz.api.game.guessreceived.*;
import net.starype.quiz.api.game.question.Question;

public class PollRoundFactory {
    public StandardRound create(Question question, int maxGuesses) {
        MaxGuessCounter counter = new MaxGuessCounter(maxGuesses);
        RoundState roundState = new RoundState(counter, counter);
        NoGuessLeft noGuessLeft = new NoGuessLeft(counter);

        GuessReceivedAction consumer =
                new IncrementPlayerGuess()
                .followedBy(new UpdatePlayerEligibility());


        return new StandardRound.Builder()
                .withGuessReceivedHead(new UpdateAnswers())
                .withGuessReceivedAction(consumer)
                .withGiveUpReceivedConsumer(new ConsumePlayerGuess())
                .withQuestion(question)
                .addScoreDistribution(new ZeroScoreDistribution())
                .addPlayerEligibility(counter)
                .withRoundState(roundState)
                .withEndingCondition(noGuessLeft)
                .addPlayerSettable(noGuessLeft)
                .addPlayerSettable(roundState)
                .build();
    }
}