package sber.school.ChargingNetwork.dto;

public class StartSessionRequestDto {
    private String uid;

    public StartSessionRequestDto() {
    }

    public StartSessionRequestDto(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
