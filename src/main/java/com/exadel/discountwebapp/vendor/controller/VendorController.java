package com.exadel.discountwebapp.vendor.controller;

import com.exadel.discountwebapp.vendor.service.VendorService;
import com.exadel.discountwebapp.vendor.vo.VendorRequestVO;
import com.exadel.discountwebapp.vendor.vo.VendorResponseVO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public List<VendorResponseVO> findAllVendors() {
        return vendorService.findAll();
    }

    @GetMapping("/{id}")
    public VendorResponseVO findById(@NotNull @Positive @PathVariable Long id) {
        return vendorService.findById(id);
    }

    @GetMapping("/title")
    public VendorResponseVO findByTitle(@NotBlank @Valid @RequestParam(name = "title") String title) {
        return vendorService.findByTitle(title);
    }

    @PostMapping
    public VendorResponseVO create(@RequestBody VendorRequestVO request) {
        return vendorService.create(request);
    }

    @PutMapping("/{id}")
    public VendorResponseVO update(@NotNull @Positive @PathVariable Long id, @NotBlank @Valid @RequestBody VendorRequestVO request) {
        return vendorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @Positive @PathVariable Long id) {
        vendorService.deleteById(id);
    }
}