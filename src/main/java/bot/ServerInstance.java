package bot;

import org.javacord.api.entity.user.User;

import java.io.Serializable;
import java.util.TreeMap;

public class ServerInstance implements Serializable {
    private char prefix;
    private final String serverID;
    private TreeMap<User,Integer> activityRanking = new TreeMap();
    public ServerInstance(String serverID){
        this.serverID = serverID;
        prefix = '>';
    }

    public String getServerID() {
        return serverID;
    }

    public TreeMap<User, Integer> getActivityRanking() {
        return activityRanking;
    }

    public void increaseMessages(User x){
        int value = activityRanking.get(x);
        activityRanking.replace(x,value);
    }

    public void addPlayer(User x){
        activityRanking.putIfAbsent(x,0);
    }

    public char getPrefix() {
        return prefix;
    }

    public void setPrefix(char prefix) {
        this.prefix = prefix;
    }
}
