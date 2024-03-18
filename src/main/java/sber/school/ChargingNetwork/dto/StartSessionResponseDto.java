package sber.school.ChargingNetwork.dto;

public class StartSessionResponseDto {

    private ResponseStatus status;
    private int sessionId;

    public StartSessionResponseDto() {
    }

    public StartSessionResponseDto(ResponseStatus status) {
        this.status = status;
    }

    public StartSessionResponseDto(ResponseStatus status, int sessionId) {
        this.status = status;
        this.sessionId = sessionId;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
