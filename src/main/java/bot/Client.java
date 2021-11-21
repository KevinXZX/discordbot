package bot;

import com.google.gson.Gson;
import org.javacord.api.*;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
public class Client {
    final private static long startTime = System.currentTimeMillis();
    public static void main(String[] args) {
        Gson gson = new Gson();
        Scanner sc = new Scanner(System.in);
        String token = sc.nextLine();
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        Collection<Server> servers = api.getServers();
        api.addMessageCreateListener(new HelpCommands());
        api.addMessageCreateListener(new ModerationCommands(api));
        api.addMessageCreateListener(new GameCommands());
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }
    public static double getRunTime(){
        return (System.currentTimeMillis()-startTime)/1000;
    }
}

