package net.txsla.xProxy;

import java.util.Base64;

public class RequestHandler {
    public static void handle(String name, String rawData, String packet) {
        // Ignore packets that were sent from the internal client
        if (name.equals("xProxyInternal")) return;

        // split
        String whoFor = rawData.split("-")[0];
        String data = rawData.split("-")[1];


        // send the data to the proper class for farther processing
        switch (whoFor) {
            case "xproxyrank":
                net.txsla.xProxy.rank.RankSync.request(data);
                break;
            default:
                if (config.debug) System.out.println("\nFailed to process packet " + packet + "\n");
                break;
        }

    }
}
