package bot;

import bot.Client;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import java.util.regex.*;


public class HelpCommands implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().equalsIgnoreCase(">runtime")){
            messageCreateEvent.getChannel().sendMessage("The bot has been up for " + Client.getRunTime()
                    + " seconds");
        }
        else if((String.valueOf(messageCreateEvent.getMessageContent()).matches(">nick .+"))){
           if(messageCreateEvent.getMessageAuthor().isBotOwner()) {
               messageCreateEvent.getApi()
                       .updateUsername(String.valueOf(messageCreateEvent.getMessageContent()).substring(6));
           }
           else{
               messageCreateEvent.getChannel().sendMessage("Only the bot owner can do this!");
           }
        }
        else if(messageCreateEvent.getMessageContent().equalsIgnoreCase(">destroy")) {
            if (messageCreateEvent.getMessageAuthor().isBotOwner()) {
                messageCreateEvent.getChannel().sendMessage("Shutting down...");
                System.exit(1);
            } else {
                messageCreateEvent.getChannel().sendMessage("Only the bot owner can do this!");
            }
        }
    }
}
