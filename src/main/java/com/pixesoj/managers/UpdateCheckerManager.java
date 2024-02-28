package com.pixesoj.managers;

import com.pixesoj.model.internal.UpdateCheckerResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheckerManager {
    private String version;
    private String latestVersion;

    public UpdateCheckerManager(String version) {
        this.version = version;
    }

    public UpdateCheckerResult check() {
        try {
            HttpURLConnection con = (HttpURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=111403")).openConnection();
            int timed_out = 1500;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            this.latestVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();
            return this.latestVersion.length() <= 7 && !this.version.equals(this.latestVersion) ? UpdateCheckerResult.noErrors(this.latestVersion) : UpdateCheckerResult.noErrors((String)null);
        } catch (Exception var3) {
            return UpdateCheckerResult.error();
        }
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }
}
