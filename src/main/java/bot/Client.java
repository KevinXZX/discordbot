package bot;

import org.javacord.api.*;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import java.util.Scanner;
public class Client {
    final private static long startTime = System.currentTimeMillis();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String token = sc.nextLine();
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        api.addMessageCreateListener(new HelpCommands());
        api.addMessageCreateListener(new ModerationCommands());
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }
    public static double getRunTime(){
        return (System.currentTimeMillis()-startTime)/1000;
    }

}

