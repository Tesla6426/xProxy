package net.txsla.xProxy;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.txsla.xProxy.internal_client.xProxyClient;
import net.txsla.xProxy.rank.RankProcessor;
import net.txsla.xProxy.rank.ranks;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class Main {
    public static Path dir;
    public static YamlDocument configFile;
    public static void main(String[] args){

        // load config files
        loadConfigFiles();


        // add config for these two
        config.port = 25599;
        config.password = "P@ssword";
        ServerSocket serverSocket;


        // load ranks
        net.txsla.xProxy.rank.ranks.loadRanks();


        // start internal client
        initialiseXProxyInternalClient();

        // start server
        System.out.println("Starting xProxy...");
        try {
            serverSocket = new ServerSocket(config.port);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch  (Exception e) { e.printStackTrace();}

    }

    public static void initialiseXProxyInternalClient() {
        // internal client
        if (config.debug) System.out.println("[ProxyChat Internal] starting xProxy client");
        new Thread(() -> {
            Socket socket;

            try {
                // wait 1000ms for xproxy server to start before connecting
                // remind me to add disconnect redundancy later
                Thread.sleep(1000);
                socket = new Socket("127.0.0.1", config.port);
                xProxyClient client = new xProxyClient(socket, "xProxyInternal", config.password);
                client.listener();
                client.send();
                xProxyClient.out = ("con¦" + config.password);
            } catch (Exception e) {
                if (config.debug) System.out.println("(Ignorable) Error Connecting" + e);
            }

        }).start();

    }


    public static void loadConfigFiles() {
        dir = Path.of(System.getProperty("user.dir"));
        Path dataDir = dir;

        try {
            // config file magic
            configFile = YamlDocument.create(new File(dataDir.toFile(), "config.yml"),
                    Objects.requireNonNull( Main.class.getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
            );
            configFile.update();
            configFile.save();
        }
        catch (Exception e) {
            if (config.debug) System.out.println("Failed to load main config: " + e);
        }


        // RANKS CONFIG FILE
        System.out.println("[ProxyChat] Loading ProxyChat Ranks...");

        try {
            // load ranks config file
            ranks.ranksConfig = YamlDocument.create(new File(dataDir.toFile(), "ranks.yml"),
                    Objects.requireNonNull(Main.class.getResourceAsStream("/ranks.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
            );
            ranks.ranksConfig.update();
            ranks.ranksConfig.save();
            System.out.println("[ProxyChat] Ranks Config Loaded!");
        }
        catch (Exception e) {
            System.out.println("\n\n" + e);
            System.out.println("[ProxyChat] [ERROR] Failed to load ProxyChat Ranks Config!\n\n");
            // disable ranks
            ranks.rankSystem = 0;
        }
    }
}