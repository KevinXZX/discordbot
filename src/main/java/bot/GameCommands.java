package bot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Arrays;
import java.util.Random;

public class GameCommands implements MessageCreateListener {
    Random random = new Random();
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().matches(">\\d+d\\d+")){
            try {
                int numOfRolls = Integer.parseInt(messageCreateEvent.getMessageContent().split("d")[0].substring(1));
                int numOfSides = Integer.parseInt(messageCreateEvent.getMessageContent().split("d")[1]);
                int[] score = new int[numOfRolls];
                for (int i = 0; i < numOfRolls; i++) {
                    score[i] = random.nextInt(numOfSides) + 1;
                }
                messageCreateEvent.getChannel().sendMessage("You rolled " + Arrays.toString(score));
            }
            catch (Exception e){
                messageCreateEvent.getChannel().sendMessage("Numbers too large");
            }
        }
        if(messageCreateEvent.getMessageContent().equalsIgnoreCase(">russian-roulette")){
            if(random.nextInt(6)+1 == 6){
                messageCreateEvent.getChannel().sendMessage(":gun: You died!");
            }
            else{
                messageCreateEvent.getChannel().sendMessage("You lived!");
            }
        }
    }
}
