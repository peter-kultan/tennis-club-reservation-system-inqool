package cz.payload;

public class CourtPostRequest {

    private final int surfaceId;

    private final double hourPrice;

    public CourtPostRequest(int surfaceId, double hourPrice) {
        this.surfaceId = surfaceId;
        this.hourPrice = hourPrice;
    }

    public int getSurfaceId() {
        return surfaceId;
    }

    public double getHourPrice() {
        return hourPrice;
    }
}
