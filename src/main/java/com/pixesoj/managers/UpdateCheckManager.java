package com.pixesoj.managers;

import com.pixesoj.model.internal.UpdateCheckResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheckManager {
    private String version;
    private String latestVersion;

    public UpdateCheckManager(String version) {
        this.version = version;
    }

    public UpdateCheckResult check() {
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=111403")).openConnection();
            int timed_out = 1500;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            this.latestVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();

            if (compareVersions(version, latestVersion) < 0) {
                return UpdateCheckResult.noErrors(this.latestVersion);
            }

            return UpdateCheckResult.noErrors((String) null);
        } catch (Exception var3) {
            return UpdateCheckResult.error();
        }
    }

    private int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            int part1 = Integer.parseInt(parts1[i]);
            int part2 = Integer.parseInt(parts2[i]);

            if (part1 < part2) {
                return -1;
            } else if (part1 > part2) {
                return 1;
            }
        }

        return Integer.compare(parts1.length, parts2.length);
    }

    public  String getLatestVersion() {
        return this.latestVersion;
    }
}
