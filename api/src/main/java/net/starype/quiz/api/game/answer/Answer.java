package net.starype.quiz.api.game.answer;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Answer {

    private final String answer;

    private Answer(String answer) {
        this.answer = answer;
    }

    public static Answer fromString(String answer) {
        return new Answer(answer);
    }

    public static Set<Answer> fromSetOfString(Set<String> stringSet) {
        return stringSet.stream()
                .map(Answer::fromString)
                .collect(Collectors.toSet());
    }

    public String getAnswerText() {
        return answer;
    }

    public int asInt() {
        return Integer.parseInt(answer);
    }

    public double asDouble() {
        return Double.parseDouble(answer.replace(',', '.'));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return answer.equals(answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }
}
