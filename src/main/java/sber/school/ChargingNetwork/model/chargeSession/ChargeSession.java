package sber.school.ChargingNetwork.model.chargeSession;

import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "Charge_sessions")
public class ChargeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sessionId;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Station station;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime stopTime;

    private String stopReason;

    public ChargeSession() {
    }

    public ChargeSession(int sessionId, User user, Station station, LocalDateTime startTime, LocalDateTime stopTime, String stopReason) {
        this.sessionId = sessionId;
        this.user = user;
        this.station = station;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.stopReason = stopReason;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }
}
