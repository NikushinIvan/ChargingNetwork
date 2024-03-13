package sber.school.ChargingNetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.service.AddressService;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;
import sber.school.ChargingNetwork.service.VendorService;

@Controller
@RequestMapping("/station")
public class StationController {

    private final StationService stationService;
    private final UserService userService;
    private final AddressService addressService;
    private final VendorService vendorService;

    public StationController(StationService stationService, UserService userService, AddressService addressService, VendorService vendorService) {
        this.stationService = stationService;
        this.userService = userService;
        this.addressService = addressService;
        this.vendorService = vendorService;
    }

    @GetMapping
    public String getAllStations(Model model) {
        model.addAttribute("stations", stationService.getAllStations());
        return "station/station";
    }

    @GetMapping("/{id}")
    public String getStation(Model model, @PathVariable int id) {
        var station = stationService.getStationById(id);
        model.addAttribute("station", station);
        return "/station/profile";
    }

    @PostMapping
    public String createStation(@ModelAttribute Station station) {
        stationService.saveStation(station);
        return "redirect:/station";
    }

    @DeleteMapping("/{id}")
    public String deleteStation(@PathVariable int id) {
        stationService.deleteStation(id);
        return "redirect:/station";
    }

    @PutMapping("/{id}")
    public String updateStation(@PathVariable int id, @ModelAttribute Station station) {
        stationService.updateStation(id, station);
        return "redirect:/station";
    }

    @GetMapping("/managerPanel")
    public String showManagerPanel() {
        return "/station/managerPanel";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("station", new Station());
        model.addAttribute("addresses", addressService.getAllAddress());
        model.addAttribute("vendors", vendorService.getAllVendors());
        model.addAttribute("users", userService.getUsersGetWithRole("ROLE_MANAGER_STATION"));
        return "/station/create";
    }

    @GetMapping("/{id}/update")
    public String showUpdatePage(Model model, @PathVariable int id) {
        var station = stationService.getStationById(id);
        model.addAttribute("station", station);
        model.addAttribute("addresses", addressService.getAllAddress());
        model.addAttribute("users", userService.getUsersGetWithRole("ROLE_MANAGER_STATION"));
        return "/station/update";
    }

}
