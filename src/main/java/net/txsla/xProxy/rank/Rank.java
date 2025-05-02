package net.txsla.xProxy.rank;

import java.util.List;
// data type 0.2.0-dev
public class Rank {
    // data type version
    public static String version = "0.2.0-dev";
    // class for storing rank data (hopefully this does not get too messy)
    private String name;
    private String prefix;
    private List<String> players;
    private List<String> permissions;
    public Rank (String name) {
        this.name = name;
    }
    public Rank (String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }
    public Rank (String name, String prefix, List<String> players) {
        this.name = name;
        this.prefix = prefix;
        this.players = players;
    }
    public Rank (String name, String prefix, List<String> players, List<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.players = players;
        this.permissions = permissions;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    public void addPlayer(String username) {
        if (!this.players.contains(username)) this.players.add(username);
    }
    public void addPlayers(List<String> usernames) {
        for (String username : usernames) {
            if (!this.players.contains(username)) this.players.add(username);
        }
    }
    public String getPrefix() {
        return this.prefix;
    }
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
    public String getName() {
        return this.name;
    }
    public List<String> getPlayers() {
        return this.players;
    }
    public List<String> getPermissions() { return permissions; }
}

