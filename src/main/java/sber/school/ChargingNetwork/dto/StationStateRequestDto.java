package sber.school.ChargingNetwork.dto;

/**
 *
 * Объект данных состояния станции, передаваемый зарядной станцией серверу
 *
 */
public class StationStateRequestDto {
    private StationState stationState;

    public StationStateRequestDto() {
    }

    public StationStateRequestDto(StationState stationState) {
        this.stationState = stationState;
    }

    public StationState getStationState() {
        return stationState;
    }

    public void setStationState(StationState stationState) {
        this.stationState = stationState;
    }
}
