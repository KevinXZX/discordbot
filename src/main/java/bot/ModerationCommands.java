package bot;

import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.CompletableFuture;

public class ModerationCommands implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageAuthor().isServerAdmin()){
            if(messageCreateEvent.getMessageContent().matches(">purge .*")){
                if(messageCreateEvent.getMessageAuthor().isServerAdmin()) {
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
                }
                else{
                    System.out.println("Only server admins may do this!");
                }
            }
        }
    }
}
