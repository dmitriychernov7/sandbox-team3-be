package com.exadel.discountwebapp.userdiscount.controller;

import com.exadel.discountwebapp.userdiscount.service.UserDiscountService;
import com.exadel.discountwebapp.userdiscount.vo.UserDiscountRequestVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-discount")
public class UserDiscountController {

    private final UserDiscountService userDiscountService;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Void> addDiscount(@Valid @RequestBody UserDiscountRequestVO request) {
        userDiscountService.addDiscount(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/{userId}/{discountId}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQRCodeByUserDiscountId(@PathVariable Long userId, @PathVariable Long discountId) {
        return userDiscountService.getQRCodeByUserDiscountId(userId, discountId);
    }
}