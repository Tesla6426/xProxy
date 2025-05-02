package net.txsla.xProxy.rank;

import net.txsla.xProxy.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class RankProcessor {

    public static String remove_this_later;

    public static String encodeRankConfig() {
        // add rank version
        String out = Rank.version + "¦"; // +ranks go into the array in a b64 list

        // encode default rank (players list should be 'null')
        if (config.debug) System.out.println("Encoding Default Rank");
        out += encode( "default¦" + encode(ranks.defaultRank.getPrefix()) + "¦" + encode("null") + "¦" + encode(ranks.defaultRank.getPermissions().toString()) );

        // encode each rank and add it to the string
        for (Rank rank : ranks.ranks) {
            if (config.debug) System.out.println("Encoding Rank " + rank.getName());
            out += "," + encode( rank.getName() + "¦" + encode(rank.getPrefix()) + "¦" + encode( String.join( "¦", rank.getPlayers())) + "¦" + encode(String.join("¦",rank.getPermissions() )));
        }
        return out;
    }
    public static void decodeRankConfig(String encoded_config) {
        // decode the encoded config directly to live vars

        // get rank config version, for future compatibility
        String rank_config_version = encoded_config.split("¦")[0];

        // get a list of encoded ranks
        String[] encoded_ranks = encoded_config.split("¦")[1].split(",");

        ranks.loading = true;

        // temp var to prevent declaring in a loop
        String[] encoded_rank;

        // decode ranks and load to mem
        ranks.ranks = new ArrayList<>();
        if (config.debug) System.out.println("Encoded_Ranks: " + Arrays.toString(encoded_ranks));

        // default rank
        if (config.debug) System.out.println("Decoding Default Rank");
        encoded_rank = decode(encoded_ranks[0]).split("¦");
        ranks.defaultRank = new Rank(
                encoded_rank[0],
                decode(encoded_rank[1]),
                decodeList(encoded_rank[2]),
                decodeList(encoded_rank[3])
        );

        for (int i = 1; i < encoded_ranks.length; i++) {
            if (config.debug) System.out.println("Decoding Rank " + i);
            encoded_rank = decode(encoded_ranks[i]).split("¦");
            if (config.debug) System.out.println("encoded_rank: " + Arrays.toString(encoded_rank));
            ranks.ranks.add(new Rank(
                    encoded_rank[0],
                    decode(encoded_rank[1]),
                    decodeList(encoded_rank[2]),
                    decodeList(encoded_rank[3])
            ));
            if (config.debug) {
                System.out.println("\nLoading Rank " + encoded_rank[0] +
                        "\nPrefix: " + decode(encoded_rank[1]) +
                        "\nPlayers: " + decodeList(encoded_rank[2]) +
                        "\nPermissions: " + decodeList(encoded_rank[3]) + "\n\n"
                );
            }
        }

        // hopefully this works

        ranks.loading = false;
    }
    public static List<String> decodeList(String encodedList) {
        try {
            return Arrays.stream(decode(encodedList).split("¦")).toList();
        } catch (Exception e) {
            return null;
        }
    }
    public static String encode(String encode) { return new String(Base64.getEncoder().encode(encode.getBytes()));}
    public static String decode(String b64) { return new String(Base64.getDecoder().decode(b64));}
}
