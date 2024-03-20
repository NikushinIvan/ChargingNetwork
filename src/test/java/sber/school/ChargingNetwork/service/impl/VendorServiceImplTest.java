package sber.school.ChargingNetwork.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.repository.VendorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    @Test
    public void getAllVendors() {
        var vendor1 = new Vendor("Промэлектроника", "+79998887766");
        var vendor2 = new Vendor("Парус электро", "+79998887744");
        var vendors = List.of(vendor1, vendor2);

        doReturn(vendors).when(vendorRepository).findAll();

        var result = vendorService.getAllVendors();

        verify(vendorRepository, times(1)).findAll();
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(vendor1)),
                () -> assertTrue(result.contains(vendor2))
        );
    }

}