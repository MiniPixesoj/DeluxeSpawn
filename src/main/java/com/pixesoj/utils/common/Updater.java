package com.pixesoj.utils.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.UpdateCheckManager;
import com.pixesoj.model.internal.UpdateCheckResult;
import com.pixesoj.utils.MessagesUtils;
import com.pixesoj.utils.OtherUtils;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Updater {
    private final ScheduledExecutorService updateExecutor = Executors.newScheduledThreadPool(1);
    private final String currentVersion;
    private final String jarName;
    private boolean enabled;
    private final int resourceId;
    private final File pluginsFolder;
    private String latestVersion;
    boolean updateAvailable = false;
    boolean ready = false;
    boolean alreadyDownloaded = false;
    DeluxeSpawn plugin;

    public void colored(CommandSender sender, String prefix, String text){
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + text));
    }

    public Updater(String currentVersion, String jarName, boolean enabled, int resourceId, File pluginsFolder, CommandSender sender) {
        String prefix = "&eDeluxeSpawn &8» ";
        this.jarName = jarName;
        this.enabled = enabled;
        this.resourceId = resourceId;
        this.pluginsFolder = pluginsFolder;
        this.currentVersion = currentVersion.contains("-") ? currentVersion.split("-")[0] : currentVersion;
        this.updateExecutor.scheduleAtFixedRate(() -> {
            try {
                this.check(sender);
            } catch (Exception e) {
                colored(sender, prefix, "&cCan't check for update:" + e.getMessage());
            }
        }, 2L, 120L, TimeUnit.SECONDS);
    }

    public void check(CommandSender sender) throws IOException {
        String prefix = "&eDeluxeSpawn &8» ";
        if (!this.enabled) {
            return;
        }
        URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + this.resourceId);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.addRequestProperty("User-Agent", this.jarName + "/" + this.currentVersion);
        if (connection.getResponseCode() != 200) {
            throw new IllegalStateException("Response code was " + connection.getResponseCode());
        }
        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonObject json = new JsonParser().parse((Reader)reader).getAsJsonObject();
        this.latestVersion = json.get("current_version").getAsString();
        if (this.latestVersion.contains("-")) {
            this.latestVersion = this.latestVersion.split("-")[0];
        }
        if (this.latestVersion.isEmpty()) {
            throw new IllegalStateException("Latest version is empty!");
        }
        String[] parts = this.latestVersion.split("\\.");
        String[] curparts = this.currentVersion.split("\\.");
        int i = 0;
        for (String part : parts) {
            int curver;
            if (i >= curparts.length) break;
            int newver = Integer.parseInt(part);
            if (newver > (curver = Integer.parseInt(curparts[i]))) {
                if (i != 0) {
                    int currentverlast;
                    int newverlast = Integer.parseInt(parts[i - 1]);
                    if (newverlast >= (currentverlast = Integer.parseInt(curparts[i - 1]))) {
                        this.updateAvailable = true;
                        break;
                    }
                } else {
                    this.updateAvailable = true;
                    break;
                }
            }
            ++i;
        }
        if (this.updateAvailable && !this.ready) {
            colored(sender, prefix, "&aAn update is available! &8(&e" + this.latestVersion + "&8)");
            downloadUpdate(sender);
        } else if (!this.ready) {
            colored(sender, prefix, "&aYou are up to date! (" + this.latestVersion + ")");
        }
        this.ready = true;
    }

    public boolean isUpdateAvailable() {
        if (!this.enabled) {
            return false;
        }
        return this.updateAvailable;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAlreadyDownloaded() {
        return this.alreadyDownloaded;
    }

    public boolean downloadUpdate(CommandSender sender) {
        String prefix = "&eDeluxeSpawn &8» ";
        List<String> possibleNames = Arrays.asList(this.jarName + "-" + this.currentVersion, this.jarName.toLowerCase(Locale.ROOT) + "-" + this.currentVersion, this.jarName.toLowerCase(Locale.ROOT), this.jarName, this.jarName + "-" + this.currentVersion + " (1)", this.jarName + "-" + this.currentVersion + " (2)", this.jarName + "-" + this.currentVersion + " (3)");
        File oldJar = null;
        for (String name : possibleNames) {
            File file = new File(this.pluginsFolder, name + ".jar");
            if (!file.exists()) continue;
            oldJar = file;
            break;
        }
        if (oldJar == null) {
            colored(sender, prefix, "Could not find the old plugin jar! Make sure it is named like this: " + this.jarName + "-" + this.currentVersion + ".jar");
            return false;
        }
        try {
            URL url = new URL("https://github.com/MiniPixesoj/DeluxeSpawn/releases/download/" + this.latestVersion + "/DeluxeSpawn.jar");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("User-Agent", this.jarName + "/" + this.currentVersion);
            connection.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("Response code was " + connection.getResponseCode());
            }
            ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
            File out = new File(this.pluginsFolder, this.jarName + "-" + this.latestVersion + ".jar");
            String message = "&aDownloading the latest update...";
            colored(sender, prefix, message);
            colored(sender, "", "&6" + (out.getAbsolutePath()));
            FileOutputStream fos = new FileOutputStream(out);
            fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            fos.close();
            if (!oldJar.delete()) {
                colored(sender, prefix, "Unable to delete the old jar! You should delete it yourself before restarting.");
            }
            this.updateAvailable = false;
            this.alreadyDownloaded = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void shutdown() {
        this.updateExecutor.shutdown();
    }
}