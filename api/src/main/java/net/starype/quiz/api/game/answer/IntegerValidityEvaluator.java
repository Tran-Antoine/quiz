package net.starype.quiz.api.game.answer;

public class IntegerValidityEvaluator implements ValidityEvaluator {

    private IntegerValidityEvaluator() {}

    private static IntegerValidityEvaluator instance = null;

    public static IntegerValidityEvaluator getInstance()
    {
        if(instance == null) {
            instance = new IntegerValidityEvaluator();
        }
        return instance;
    }

    @Override
    public boolean isValid(Answer answer) {
        return answer
                .getAnswerText()
                .strip()
                .matches("^[+-]?[0-9]+");
    }
}
