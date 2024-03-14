package sber.school.ChargingNetwork.dto;

public class StationStateRequestDto {
    private String stationState;

    public StationStateRequestDto() {
    }

    public StationStateRequestDto(String stationState) {
        this.stationState = stationState;
    }

    public String getStationState() {
        return stationState;
    }

    public void setStationState(String stationState) {
        this.stationState = stationState;
    }
}
