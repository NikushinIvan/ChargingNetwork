package sber.school.ChargingNetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.model.station.Station;
import sber.school.ChargingNetwork.service.AddressService;
import sber.school.ChargingNetwork.service.StationService;
import sber.school.ChargingNetwork.service.UserService;
import sber.school.ChargingNetwork.service.VendorService;

/**
 *
 * Контроллер станций
 *
 */
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

    /**
     *
     * Метод отображения HTML-страницы со списком всех станций
     *
     * @param model - Передает в представление список объектов станций.
     *              Атрибут stations - список станций
     * @return Строка с названием представления
     */
    @GetMapping
    public String getAllStations(Model model) {
        model.addAttribute("stations", stationService.getAllStations());
        return "station/station";
    }

    /**
     *
     * Метод отображения HTML-страницы профиля стацнии
     *
     * @param model - Передает в представление объект станции.
     *              Атрибут station - объект станции
     * @param id - Содержится в URL, ID станции
     * @return Строка с названием представления
     */
    @GetMapping("/{id}")
    public String getStation(Model model, @PathVariable int id) {
        var station = stationService.getStationById(id);
        model.addAttribute("station", station);
        return "/station/profile";
    }

    /**
     *
     * Метод создания новой станции
     *
     * @param station - Объект станции, переданный представлением контроллеру
     * @return Перенаправление на страницу со списком станций
     */
    @PostMapping
    public String createStation(@ModelAttribute Station station) {
        stationService.saveStation(station);
        return "redirect:/station";
    }

    /**
     *
     * Метод удаления станции
     *
     * @param id - Содержится в URL, ID станции
     * @return Перенаправление на страницу со списком станций
     */
    @DeleteMapping("/{id}")
    public String deleteStation(@PathVariable int id) {
        stationService.deleteStation(id);
        return "redirect:/station";
    }

    /**
     *
     * Метод обновления данных станции
     *
     * @param id - Содержится в URL, ID станции
     * @param station - Объект станции, переданный представлением контроллеру, содержит обновленные данные
     * @return Перенаправление на страницу со списком станций
     */
    @PutMapping("/{id}")
    public String updateStation(@PathVariable int id, @ModelAttribute Station station) {
        stationService.updateStation(id, station);
        return "redirect:/station";
    }

    /**
     *
     * Метод отображения HTML-страницы панели управления станциями
     *
     * @return Строка с названием представления
     */
    @GetMapping("/managerPanel")
    public String showManagerPanel() {
        return "/station/managerPanel";
    }

    /**
     *
     * Метод отображения HTML-страницы создания станции
     *
     * @param model - Передает в представление данные, необходимые для создания новой станции.
     *              Атрибут station - пустой объект станции,
     *              Атрибут addresses - список доступных адресов,
     *              Атрибут vendors - список доступных поставщиков станций,
     *              Атрибут users - список доступных диспетчеров станций
     * @return Строка с названием представления
     */
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("station", new Station());
        model.addAttribute("addresses", addressService.getAllAddress());
        model.addAttribute("vendors", vendorService.getAllVendors());
        model.addAttribute("users", userService.getUsersGetWithRole("ROLE_MANAGER_STATION"));
        return "/station/create";
    }

    /**
     *
     * Метод отображения HTML-страницы обновления данных станции
     *
     * @param model - Передает в представление актуальные данные станции.
     *              Атрибут station - объект станции с актуальными данными,
     *              Атрибут addresses - список доступных адресов,
     *              Атрибут users - список доступных диспетчеров станций
     * @param id - Содержится в URL, ID станции
     * @return Строка с названием представления
     */
    @GetMapping("/{id}/update")
    public String showUpdatePage(Model model, @PathVariable int id) {
        var station = stationService.getStationById(id);
        model.addAttribute("station", station);
        model.addAttribute("addresses", addressService.getAllAddress());
        model.addAttribute("users", userService.getUsersGetWithRole("ROLE_MANAGER_STATION"));
        return "/station/update";
    }

}
