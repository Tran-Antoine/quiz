package net.starype.quiz.api.reader;

import net.starype.quiz.api.game.question.Question;
import net.starype.quiz.api.parser.SimpleQuestionDatabase;
import net.starype.quiz.api.parser.QuestionQueries;

import java.util.List;

public class QuestionDBTest {
    public static void main(String[] args) {
        SimpleQuestionDatabase db = new SimpleQuestionDatabase("api/src/main/resources/questions", "db.bin",
                false, false);
        db.sync();
        List<Question> lQ = db.listQuery(QuestionQueries.allWithTag("Math"));
    }
}
