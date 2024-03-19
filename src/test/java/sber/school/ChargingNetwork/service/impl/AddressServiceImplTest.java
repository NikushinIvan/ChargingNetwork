package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sber.school.ChargingNetwork.model.station.Address;
import sber.school.ChargingNetwork.repository.AddressRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    public void getAllAddress_returnListUsers() {
        var addresses = List.of(
                new Address(1, "Рязань", "Каширина", 1),
                new Address(2, "Москва", "Ленина", 5)
        );

        doReturn(addresses).when(addressRepository).findAll();

        var result = addressService.getAllAddress();

        assertEquals(addresses, result);
    }

    @Test
    public void getAllAddress_returnEmptyList() {
        var addresses = List.of();

        doReturn(addresses).when(addressRepository).findAll();

        var result = addressService.getAllAddress();

        assertEquals(addresses, result);
    }
}