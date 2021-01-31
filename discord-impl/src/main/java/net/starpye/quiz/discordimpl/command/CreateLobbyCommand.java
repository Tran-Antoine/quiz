package net.starpye.quiz.discordimpl.command;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import net.starpye.quiz.discordimpl.game.GameList;
import net.starpye.quiz.discordimpl.game.GameLobby;
import net.starpye.quiz.discordimpl.game.LobbyList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CreateLobbyCommand implements QuizCommand {

    @Override
    public void execute(CommandContext context) {

        Member author = context.getAuthor();
        Snowflake playerId = author.getId();
        TextChannel channel = context.getChannel();

        Map<Supplier<Boolean>, String> stopConditions = createStopConditions(
                context.getGameList(),
                context.getLobbyList(),
                playerId,
                author.getDisplayName());

        Message message = context.getMessage();

        if(StopConditions.shouldStop(stopConditions, channel, message)) {
            return;
        }

        LobbyList lobbies = context.getLobbyList();
        GameLobby lobby = lobbies.registerLobby(channel, author);
        lobby.trackMessage(message.getId());
    }

    private Map<Supplier<Boolean>, String> createStopConditions(
            GameList gameList, LobbyList lobbyList, Snowflake authorId, String nickName) {

        Map<Supplier<Boolean>, String> conditions = new LinkedHashMap<>();
        conditions.put(
                () -> lobbyList.findByPlayer(authorId).isPresent(),
                nickName + ", you are already in a lobby");

        conditions.put(
                () -> gameList.isPlaying(authorId),
                nickName + ", you are already playing a game");
        return conditions;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Sets up a game lobby that can be started at the author's request";
    }
}
