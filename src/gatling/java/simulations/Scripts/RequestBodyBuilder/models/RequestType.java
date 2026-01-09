package simulations.Scripts.RequestBodyBuilder.models;

public enum RequestType {
    DOWNLOAD(70),
    PLAYBACK(30);

    private final int percentage;

    RequestType(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }
}