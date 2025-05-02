package net.txsla.xProxy.rank;

import net.txsla.xProxy.Main;
import net.txsla.xProxy.internal_client.xProxyClient;

import java.util.Base64;

public class RankSync {
    public static void request(String data) {
            switch (data) {
                case "send":
                    send();
                    break;
                case "reload":
                    reload();
                    break;
                    // add modify later
            }
    }
    public static void send() {
        xProxyClient.out = "bdc¦xproxyrank-" + encode(RankProcessor.encodeRankConfig());
    }
    public static void reload() {
        // reloads the config
        Main.loadConfigFiles();
        // reload ranks in mem
        ranks.loadRanks();
        // send reloaded config over xProxy
        send();
    }



    public static String encode(String encode) { return new String(Base64.getEncoder().encode(encode.getBytes()));}
    public static String decode(String b64) { return new String(Base64.getDecoder().decode(b64));}
}
