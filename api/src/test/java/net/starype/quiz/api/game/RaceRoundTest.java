package net.starype.quiz.api.game;


import net.starype.quiz.api.game.answer.*;
import net.starype.quiz.api.game.event.UpdatableHandler;
import net.starype.quiz.api.game.event.GameUpdatableHandler;
import net.starype.quiz.api.game.player.IDHolder;
import net.starype.quiz.api.game.player.Player;
import net.starype.quiz.api.game.question.Question;
import net.starype.quiz.api.game.question.QuestionDifficulty;
import net.starype.quiz.api.game.question.QuestionTag;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class RaceRoundTest {

    private static CorrectAnswerFactory factory = new WordAnswerFactory();

    @Test
    public void round_ends_when_out_of_guesses() {
        UpdatableHandler updatableHandler = new GameUpdatableHandler();
        Set<Player<UUID>> players = new HashSet<>();
        players.add(new MockPlayer());
        players.add(new MockPlayer());

        GameRound round = new RaceRound.Builder()
                .withMaxGuessesPerPlayer(1)
                .withQuestion(new MockQuestion(factory.createCorrectAnswer(Answer.fromString("CORRECT"), new IdentityProcessor())))
                .build();

        round.start(null, players, updatableHandler);
        GameRoundContext context = round.getContext();

        for(Player<?> player : players) {
            round.onGuessReceived(player, "INCORRECT-ANSWER");
        }

        Assert.assertTrue(context.getEndingCondition().ends());
    }

    @Test
    public void round_ends_when_one_winner() {
        UpdatableHandler updatableHandler = new GameUpdatableHandler();
        MockPlayer player = new MockPlayer();

        GameRound round = new RaceRound.Builder()
                .withMaxGuessesPerPlayer(3)
                .withQuestion(new MockQuestion(factory.createCorrectAnswer(Answer.fromString("CORRECT"), new IdentityProcessor())))
                .build();
      
        round.start(null, Collections.singletonList(player), updatableHandler);
        RoundEndingPredicate endingPredicate = round.getContext().getEndingCondition();

        Assert.assertFalse(endingPredicate.ends());
        round.onGuessReceived(player, "INCORRECT ANSWER");
        Assert.assertFalse(endingPredicate.ends());
        round.onGuessReceived(player, "CORRECT");
        Assert.assertTrue(endingPredicate.ends());
    }

    @Test
    public void game_ends_when_players_give_up() {
        UpdatableHandler updatableHandler = new GameUpdatableHandler();
        Player<UUID> player = new MockPlayer();
        GameRound round = new RaceRound.Builder()
                .withMaxGuessesPerPlayer(10)
                .build();
        round.start(null, Collections.singletonList(player), updatableHandler);
        RoundEndingPredicate endingCondition = round.getContext().getEndingCondition();
        Assert.assertFalse(endingCondition.ends());
        round.onGiveUpReceived(player);
        Assert.assertTrue(endingCondition.ends());
    }

    @Test
    public void score_was_awarded() {
        UpdatableHandler updatableHandler = new GameUpdatableHandler();
        double pointsToAward = 3.5;
        Player<UUID> player1 = new MockPlayer();
        Player<UUID> player2 = new MockPlayer();

        GameRound round = new RaceRound.Builder()
                .withMaxGuessesPerPlayer(1)
                .withQuestion(new MockQuestion(factory.createCorrectAnswer(Answer.fromString("CORRECT"), new IdentityProcessor())))
                .withPointsToAward(pointsToAward)
                .build();

        round.start(null, Arrays.asList(player1, player2), updatableHandler);
        round.onGuessReceived(player1, "CORRECT");
        ScoreDistribution scoreDistribution = round.getContext().getScoreDistribution();

        double score1 = scoreDistribution.apply(player1);
        double score2 = scoreDistribution.apply(player2);

        Assert.assertEquals(score1, pointsToAward, 0.01);
        Assert.assertEquals(score2, 0, 0.01);
    }

    private static class MockUUIDHolder implements IDHolder<UUID> {
        private UUID id = UUID.randomUUID();
        @Override
        public UUID getId() {
            return id;
        }
    }

    private static class MockPlayer extends Player<UUID> {

        public MockPlayer() {
            super(UUID.randomUUID(), "");
        }
    }

    private static class MockQuestion implements Question {

        private AnswerEvaluator answerEvaluator;

        public MockQuestion(AnswerEvaluator answerEvaluator) {
            this.answerEvaluator = answerEvaluator;
        }

        @Override
        public Set<QuestionTag> getTags() { return null; }

        @Override
        public void registerTag(QuestionTag tag) { }

        @Override
        public void unregisterTag(QuestionTag tag) { }

        @Override
        public QuestionDifficulty getDifficulty() { return null; }

        @Override
        public String getRawQuestion() { return null; }

        @Override
        public String getDisplayableCorrectAnswer() { return null; }

        @Override
        public UUID getId() { return null; }

        @Override
        public Optional<Double> evaluateAnswer(Answer answer) {
            if(!answerEvaluator.getValidityEvaluator()
                    .isValid(answer))
                return Optional.empty();
            return Optional.of(answerEvaluator.getCorrectnessEvaluator()
                    .getCorrectness(answer));
        }
    }
}
