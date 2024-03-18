package sber.school.ChargingNetwork.dto;

public class StopSessionResponseDto {

    private ResponseStatus status;

    public StopSessionResponseDto() {
    }

    public StopSessionResponseDto(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
