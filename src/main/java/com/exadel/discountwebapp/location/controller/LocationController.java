package com.exadel.discountwebapp.location.controller;

import com.exadel.discountwebapp.location.service.LocationService;
import com.exadel.discountwebapp.location.vo.LocationRequestVO;
import com.exadel.discountwebapp.location.vo.LocationResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<LocationResponseVO> getAllLocations() {
        return locationService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public LocationResponseVO getLocationById(@PathVariable Long id) {
        return locationService.findById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/country")
    public List<LocationResponseVO> getAllLocationsByCountry(
            @RequestParam(name = "country") String country) {
        return locationService.findAllByCountry(country);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/city")
    public List<LocationResponseVO> getAllLocationsByCity(@Valid @RequestParam(name = "city") String city) {
        return locationService.findAllByCity(city);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public LocationResponseVO create(@Valid @RequestBody LocationRequestVO request) {
        return locationService.create(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public LocationResponseVO update(@PathVariable Long id, @Valid @RequestBody LocationRequestVO request) {
        return locationService.update(id, request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        locationService.deleteById(id);
    }
}
