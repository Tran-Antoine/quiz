package net.starype.quiz.api.game.answer;

public class IntegerCorrectAnswer implements CorrectAnswer {
    private ValidityEvaluator validityEvaluator;
    private CorrectnessEvaluator correctnessEvaluator;

    public IntegerCorrectAnswer(IntegerCorrectnessEvaluator integerCorrectnessEvaluator) {
        validityEvaluator = IntegerValidityEvaluator.getInstance();
        correctnessEvaluator = integerCorrectnessEvaluator;
    }

    @Override
    public ValidityEvaluator getCandidateValidityEvaluator() {
        return validityEvaluator;
    }

    @Override
    public CorrectnessEvaluator getCorrectnessEvaluator() {
        return correctnessEvaluator;
    }
}