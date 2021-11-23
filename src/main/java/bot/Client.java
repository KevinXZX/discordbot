package bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Client {
    final private static long startTime = System.currentTimeMillis();
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String token = sc.nextLine();
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        ArrayList<ServerInstance> cachedServers = new ArrayList<>();
        Collection<Server> serverCollection = api.getServers();
        checkCache(cachedServers,serverCollection);
        api.addMessageCreateListener(new HelpCommands());
        api.addMessageCreateListener(new ModerationCommands(api));
        api.addMessageCreateListener(new GameCommands());
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }
    public static void checkCache(ArrayList<ServerInstance> cachedServers,Collection<Server> serverCollection) throws IOException {
        ArrayList<String> serverIDs = new ArrayList<>();
        URL cache = Client.class.getClassLoader().getResource("Cache");
        File file = new File(cache.getFile());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        FileInputStream fileInputStream = new FileInputStream(cache.getFile());
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try {
            cachedServers = (ArrayList<ServerInstance>) objectInputStream.readObject();
        } catch (Exception e) {
            oos.writeObject(cachedServers);
        }
        for(int i = 0; i< cachedServers.size();i++){
            serverIDs.add(cachedServers.get(i).getServerID());
        }
        for(Server s:serverCollection){
            if(!(serverIDs.contains(s.getIdAsString()))){
                cachedServers.add(new ServerInstance(s.getIdAsString()));
            }
        }

    }
    public static double getRunTime(){
        return (System.currentTimeMillis()-startTime)/1000;
    }
}

