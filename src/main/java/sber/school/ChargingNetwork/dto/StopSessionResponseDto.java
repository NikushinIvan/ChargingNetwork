package sber.school.ChargingNetwork.dto;

public class StopSessionResponseDto {

    private String status;

    public StopSessionResponseDto() {
    }


    public StopSessionResponseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
