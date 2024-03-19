package com.pixesoj.utils.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Comments {
    public static final List<String> SpawnCommandsAlias = new ArrayList<>();
    static {
        String headerString =
                "\nHere you can add alias commands in addition to /spawn\n" +
                     "Restart your server after adding or removing an alias\n";

        String[] lines = headerString.split("\n");
        SpawnCommandsAlias.addAll(Arrays.asList(lines));
    }

    public static final List<String> LobbyCommandsAlias = new ArrayList<>();
    static {
        String headerString =
                        "\nHere you can add alias commands in addition to /lobby\n" +
                        "Restart your server after adding or removing an alias\n";

        String[] lines = headerString.split("\n");
        LobbyCommandsAlias.addAll(Arrays.asList(lines));
    }
}
