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
            HttpURLConnection con = (HttpURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=111403")).openConnection();
            int timed_out = 1500;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            this.latestVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();
            return this.latestVersion.length() <= 7 && !this.version.equals(this.latestVersion) ? UpdateCheckResult.noErrors(this.latestVersion) : UpdateCheckResult.noErrors((String)null);
        } catch (Exception var3) {
            return UpdateCheckResult.error();
        }
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }
}
