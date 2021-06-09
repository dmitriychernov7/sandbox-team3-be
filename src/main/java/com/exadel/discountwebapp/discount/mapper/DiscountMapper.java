package com.exadel.discountwebapp.discount.mapper;

import com.exadel.discountwebapp.category.entity.Category;
import com.exadel.discountwebapp.category.repository.CategoryRepository;
import com.exadel.discountwebapp.discount.entity.Discount;
import com.exadel.discountwebapp.discount.vo.DiscountRequestVO;
import com.exadel.discountwebapp.discount.vo.DiscountResponseVO;
import com.exadel.discountwebapp.vendor.entity.Vendor;
import com.exadel.discountwebapp.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountMapper {

    private final ModelMapper modelMapper;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public DiscountResponseVO toVO(Discount discount) {
        DiscountResponseVO response = modelMapper.map(discount, DiscountResponseVO.class);
        response.setCategoryId(discount.getCategory().getId());
        response.setVendorId(discount.getVendor().getId());
        return response;
    }

    public Discount toEntity(DiscountRequestVO request) {
        Discount discount = modelMapper.map(request, Discount.class);
        provideDiscountDependencies(request, discount);
        return discount;
    }

    public void updateEntity(DiscountRequestVO request, Discount discount) {
        provideDiscountDependencies(request, discount);
        modelMapper.map(request, discount);
    }

    private void provideDiscountDependencies(DiscountRequestVO request, Discount discount) {
        Vendor vendor = vendorRepository.findById(request.getVendorId()).orElse(null);
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        discount.setVendor(vendor);
        discount.setCategory(category);
    }
}
