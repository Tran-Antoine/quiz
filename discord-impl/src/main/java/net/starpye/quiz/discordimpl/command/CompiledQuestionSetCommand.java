package net.starpye.quiz.discordimpl.command;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import net.starpye.quiz.discordimpl.game.GameList;
import net.starpye.quiz.discordimpl.game.GameLobby;
import net.starpye.quiz.discordimpl.game.LobbyList;
import net.starpye.quiz.discordimpl.util.MessageUtils;
import net.starype.quiz.api.database.ByteSerializedIO;
import net.starype.quiz.api.database.QuestionDatabase;
import net.starype.quiz.api.database.QuizQueryable;
import net.starype.quiz.api.database.SerializedIO;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class CompiledQuestionSetCommand implements QuizCommand {

    @Override
    public void execute(CommandContext context) {
        LobbyList lobbyList = context.getLobbyList();
        Member author = context.getAuthor();
        TextChannel channel = context.getChannel();
        Message message = context.getMessage();
        Snowflake authorId = author.getId();
        String[] args = context.getArgs();

        Map<Supplier<Boolean>, String> conditions = createStopConditions(
                lobbyList,
                message,
                authorId,
                args);

        if(StopConditions.shouldStop(conditions, channel, message)) {
            return;
        }

        GameLobby lobby = lobbyList.findByAuthor(authorId).get();
        String url = findUrl(message, args);
        byte[] dbData;
        try {
            dbData = new URL(url)
                    .openStream()
                    .readAllBytes();
        } catch (IOException e) {
            channel.createMessage("Error: Couldn't load .sphinx file").subscribe();
            return;
        }

        SerializedIO serializedIO = new ByteSerializedIO(dbData, new AtomicReference<>());
        QuestionDatabase database = new QuestionDatabase(Collections.emptyList(), serializedIO, true);
        database.sync();
        lobby.setQueryObject(database);

        lobby.trackMessage(message.getId());
        MessageUtils.sendAndTrack(
                "Successfully registered the database",
                channel,
                lobby
        );
    }

    private String findUrl(Message message, String[] args) {
        Set<Attachment> attachments = message.getAttachments();
        if(attachments.size() == 1) {
            return attachments
                    .iterator()
                    .next()
                    .getUrl();
        }
        return args[1];
    }

    private static Map<Supplier<Boolean>, String> createStopConditions(
            LobbyList lobbyList, Message message, Snowflake authorId, String[] args) {
        Map<Supplier<Boolean>, String> conditions = new LinkedHashMap<>();
        conditions.put(
                () -> lobbyList.findByAuthor(authorId).isEmpty(),
                "You are not the author of any lobby");

        conditions.put(() -> message.getAttachments().size() != 1 && args.length != 2,
                "You need to attach a single .sphinx file or a link to the file as second argument");

        return conditions;
    }

    @Override
    public String getName() {
        return "compiled-question-set";
    }

    @Override
    public String getDescription() {
        return "Set the set of questions used for the game from a .sphinx file";
    }
}
