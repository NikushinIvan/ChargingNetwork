package sber.school.ChargingNetwork.dto;

public class StopSessionRequestDto {
    private int chargeSessionId;
    private String stopReason;

    public StopSessionRequestDto() {
    }

    public StopSessionRequestDto(int chargeSessionId, String stopReason) {
        this.chargeSessionId = chargeSessionId;
        this.stopReason = stopReason;
    }

    public int getChargeSessionId() {
        return chargeSessionId;
    }

    public void setChargeSessionId(int chargeSessionId) {
        this.chargeSessionId = chargeSessionId;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }
}
