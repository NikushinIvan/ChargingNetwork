package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.model.station.Vendor;
import sber.school.ChargingNetwork.repository.VendorRepository;
import sber.school.ChargingNetwork.service.VendorService;

import java.util.List;

@Service
public class VendorServiceImpl  implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}
