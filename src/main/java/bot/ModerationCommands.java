package bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModerationCommands implements MessageCreateListener {
    private final DiscordApi api;

    public ModerationCommands(DiscordApi api) {
        this.api = api;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageAuthor().isServerAdmin()) {
            if (messageCreateEvent.getMessageContent().matches(">purge .*")) {
                try {
                    int num = Integer.parseInt(messageCreateEvent.getMessageContent().substring(7));
                    CompletableFuture<MessageSet> messagesBefore =
                            messageCreateEvent.getChannel().getMessagesBefore(num, messageCreateEvent.getMessageId());
                    messagesBefore.get().deleteAll();
                    messageCreateEvent.getChannel().sendMessage("Purged " + num + " messages successfully!");
                } catch (Exception e) {
                    messageCreateEvent.getChannel().sendMessage("Purge failed!\nCorrect format is " +
                            "!purge X where X = number of messages you want to purge");
                }
            } else if (messageCreateEvent.getMessageContent().equalsIgnoreCase(">nuke")) {

                messageCreateEvent.getChannel().sendMessage("Please enter CONFIRM in the next 30 seconds to " +
                        "nuke this channel \n:warning: This is irreversible! :warning:");
                AtomicBoolean nuked = new AtomicBoolean(false);
                messageCreateEvent.getChannel().addMessageCreateListener(e -> {
                    if (e.getMessageContent().equals("CONFIRM") && messageCreateEvent
                            .getMessageAuthor().equals(e.getMessageAuthor())) {
                        ServerTextChannel original = messageCreateEvent.getServerTextChannel().get();
                        ServerTextChannel channel = new ServerTextChannelBuilder(messageCreateEvent.getServer().get())
                                .setName(original.getName())
                                .setCategory(original.getCategory().get())
                                .setTopic(original.getTopic())
                                .create().join();
                        CompletableFuture<Void> move = channel.updateRawPosition(original.getRawPosition());
                        CompletableFuture<Void> deleteChannel = messageCreateEvent.getServerTextChannel().get().delete();
                        channel.sendMessage("Channel was nuked by " + messageCreateEvent.getMessageAuthor());
                        nuked.set(true);
                    }
                }).removeAfter(30, TimeUnit.SECONDS);
                api.getThreadPool().getScheduler().schedule(() -> {
                    if (!nuked.get()) {
                        messageCreateEvent.getChannel().sendMessage("Cancelled");
                    }
                }, 30, TimeUnit.SECONDS);

            }
        } else {
            messageCreateEvent.getChannel().sendMessage("Only server admins may do this!");
        }
    }
}

