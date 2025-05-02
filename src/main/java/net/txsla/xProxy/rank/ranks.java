package net.txsla.xProxy.rank;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.txsla.xProxy.config;

import java.util.ArrayList;
import java.util.List;

public class ranks {
    // This most likely will not get used except to load the rank config file
    // I will prob just send the ranksConfig var directly to the proxies for processing
    public static YamlDocument ranksConfig;
    public static Rank defaultRank;
    public static List<Rank> ranks;
    public static boolean loading;
    public static int rankSystem; // 0 = Disabled, 1 = ProxyChat, 2 = xProxy, 3 = hopefully coming soon (either vault or placeholderAPI)
    public static void loadRanks() {
        System.out.println("[xProxy] Loading Ranks...");
        loadProxyChatRanks();
    }
    public static Rank getRank(String username) {
        for (Rank rank : ranks) if ( rank.getPlayers().contains(username)) return rank;
        // return default rank if no rank was found
        return defaultRank;
    }
    public static void loadProxyChatRanks() {
        loading = true;

        // reset ranks to not mem leak
        ranks = new ArrayList<>();

        // load default rank
        if (config.debug) System.out.println("Loading Default Rank");
        defaultRank = new Rank("default",
                ranksConfig.getString("xProxyRanks.ranks-config.default.prefix"),
                ranksConfig.getStringList("xProxyRanks.ranks-config.default.players"), // should be null
                ranksConfig.getStringList("xProxyRanks.ranks-config.default.permissions")
        );

        // load ranks into array
        for (String rank : ranksConfig.getStringList("xProxyRanks.ranks-list")) {
            if (config.debug) System.out.println("Loading Rank " + rank);
            ranks.add( new Rank(
                    rank,
                    ranksConfig.getString("xProxyRanks.ranks-config." + rank + ".prefix"),
                    ranksConfig.getStringList("xProxyRanks.ranks-config." + rank + ".players"),
                    ranksConfig.getStringList("xProxyRanks.ranks-config." + rank + ".permissions")
            ) );
        }
        loading = false;
    }
}

