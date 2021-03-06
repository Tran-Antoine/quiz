package net.starype.quiz.api.answer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface CorrectAnswerFactory {

    AnswerEvaluator createCorrectAnswer(Set<Answer> answers, AnswerProcessor answerProcessor);

    default Set<Answer> processList(Set<Answer> answers, AnswerProcessor answerProcessor) {
        return answers.stream()
                .map(answerProcessor::process)
                .collect(Collectors.toSet());
    }

    default AnswerEvaluator createCorrectAnswer(Answer answer, AnswerProcessor answerProcessor) {
        Set<Answer> answersSet = new HashSet<>();
        answersSet.add(answer);
        return createCorrectAnswer(answersSet, answerProcessor);
    }
}
