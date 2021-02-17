package net.starype.quiz.api.game;

import net.starype.quiz.api.game.guessreceived.*;
import net.starype.quiz.api.game.question.Question;

public class ClassicalRoundFactory {

    public StandardRound create(Question question, double maxAwarded, int maxGuesses) {

        IsGuessEmpty isGuessEmpty = new IsGuessEmpty();

        MaxGuessCounter counter = new MaxGuessCounter(maxGuesses);
        RoundState roundState = new RoundState(counter, counter);
        LeaderboardDistribution distribution = new LeaderboardDistribution(maxAwarded, roundState.getLeaderboard());

        GuessReceivedAction consumer =
                new InvalidateCurrentPlayerCorrectness().linkTo(isGuessEmpty)
                        .followedBy(new MakePlayerEligible().linkTo(isGuessEmpty))
                        .followedBy(new IncrementPlayerGuess().linkTo(isGuessEmpty.negate()))
                        .followedBy(new UpdateLeaderboard().linkTo(isGuessEmpty.negate()
                                .and(new IsCorrectnessZero().negate())))
                        .followedBy(new ConsumePlayerGuess().linkTo(isGuessEmpty.negate().and(new IsCorrectnessZero())))
                        .followedBy(new UpdatePlayerEligibility().linkTo(isGuessEmpty.negate()));

        return new StandardRound.Builder()
                .withGuessReceivedAction(consumer)
                .withGiveUpReceivedConsumer(new ConsumePlayerGuess())
                .withQuestion(question)
                .addScoreDistribution(distribution)
                .addPlayerEligibility(counter)
                .withRoundState(roundState)
                .withEndingCondition(new NoGuessLeft().or(new FixedLeaderboardEnding()))
                .build();
    }
}
