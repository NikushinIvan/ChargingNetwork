package sber.school.ChargingNetwork.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sber.school.ChargingNetwork.config.SecurityConfig;
import sber.school.ChargingNetwork.dto.*;
import sber.school.ChargingNetwork.service.ApplicationStationDetailsService;
import sber.school.ChargingNetwork.service.ApplicationUserDetailsService;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static sber.school.ChargingNetwork.dto.StationState.ERROR;

@WebMvcTest(ChargerController.class)
@Import(SecurityConfig.class)
class ChargerControllerIntegrationTest {

    @MockBean
    private StationService stationService;
    @MockBean
    private SessionService sessionService;
    @MockBean
    private ApplicationUserDetailsService userDetailsService;
    @MockBean
    private ApplicationStationDetailsService stationDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void setStationState_returnOk() throws Exception {
        doReturn(User.builder()
                .username("station1")
                .password("$2a$12$bxLr6BGwFhVU1buTEWrXreORRzrTdl.WMx76EWdOEY8Ok7yHolrhi")
                .roles("STATION")
                .build()
        ).when(stationDetailsService).loadUserByUsername("station1");

        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1")
                        .with(httpBasic("station1", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationState\": \"ERROR\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(stationService, times(1)).setStationState(1, ERROR);
    }

    @Test
    public void setStationState_return401() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationState\": \"ERROR\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void startSession_returnOk() throws Exception {
        doReturn(User.builder()
                .username("station1")
                .password("$2a$12$bxLr6BGwFhVU1buTEWrXreORRzrTdl.WMx76EWdOEY8Ok7yHolrhi")
                .roles("STATION")
                .build()
        ).when(stationDetailsService).loadUserByUsername("station1");
        doReturn(new StartSessionResponseDto(ResponseStatus.ACCEPTED, 1))
                .when(sessionService).startSession(any(StartSessionRequestDto.class), eq(1));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1/start")
                        .with(httpBasic("station1", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uid\": \"00112233\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"status\": \"ACCEPTED\",\"sessionId\": 1}"));
    }

    @Test
    public void startSession_return401() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationState\": \"ERROR\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void stopSession_returnOk() throws Exception {
        doReturn(User.builder()
                .username("station1")
                .password("$2a$12$bxLr6BGwFhVU1buTEWrXreORRzrTdl.WMx76EWdOEY8Ok7yHolrhi")
                .roles("STATION")
                .build()
        ).when(stationDetailsService).loadUserByUsername("station1");
        doReturn(new StopSessionResponseDto(ResponseStatus.ACCEPTED))
                .when(sessionService).stopSession(any(StopSessionRequestDto.class));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1/stop")
                        .with(httpBasic("station1", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uid\": \"00112233\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"status\": \"ACCEPTED\"}"));
    }

    @Test
    public void stopSession_return401() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/charger/api/1/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationState\": \"ERROR\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}