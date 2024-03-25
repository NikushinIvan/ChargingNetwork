package sber.school.ChargingNetwork.dto;

/**
 *
 * Объект данных запроса станции на начало зарядной сессии
 *
 */
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
