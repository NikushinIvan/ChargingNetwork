package sber.school.ChargingNetwork.dto;

public class StartSessionResponseDto {

    private String status;
    private int sessionId;

    public StartSessionResponseDto() {
    }

    public StartSessionResponseDto(String status) {
        this.status = status;
    }

    public StartSessionResponseDto(String status, int sessionId) {
        this.status = status;
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
