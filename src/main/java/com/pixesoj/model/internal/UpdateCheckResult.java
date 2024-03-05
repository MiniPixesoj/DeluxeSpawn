package com.pixesoj.model.internal;

public class UpdateCheckResult {
    private String latestVersion;
    private boolean error;

    public UpdateCheckResult(String latestVersion, boolean error) {
        this.latestVersion = latestVersion;
        this.error = error;
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }

    public boolean isError() {
        return this.error;
    }

    public static UpdateCheckResult noErrors(String latestVersion) {
        return new UpdateCheckResult(latestVersion, false);
    }

    public static UpdateCheckResult error() {
        return new UpdateCheckResult((String)null, true);
    }
}
