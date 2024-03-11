package com.pixesoj.utils;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.model.internal.UpdateCheckResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherUtils {

    public static boolean isNew() {
        ServerVersion serverVersion = DeluxeSpawn.serverVersion;
        return serverVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_16_R1);
    }

    public static boolean isLegacy() {
        ServerVersion serverVersion = DeluxeSpawn.serverVersion;
        return !serverVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_13_R1);
    }

    public static boolean isTrimNew() {
        ServerVersion serverVersion = DeluxeSpawn.serverVersion;
        return serverVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_20_R1);
    }

    public static String getLastVersion() {
        try {
            HttpURLConnection con = (HttpURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=111403")).openConnection();
            int timed_out = 1500;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);

            String latestVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();

            if (latestVersion != null && latestVersion.length() <= 7) {
                return latestVersion;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
