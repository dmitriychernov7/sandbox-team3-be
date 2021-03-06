package com.exadel.discountwebapp.userdiscount.service;

import com.exadel.discountwebapp.exception.exception.client.EntityAlreadyExistsException;
import com.exadel.discountwebapp.exception.exception.client.EntityNotFoundException;
import com.exadel.discountwebapp.userdiscount.entity.UserDiscount;
import com.exadel.discountwebapp.userdiscount.mapper.UserDiscountMapper;
import com.exadel.discountwebapp.userdiscount.qrcodegenerator.QRCodeGenerator;
import com.exadel.discountwebapp.userdiscount.repository.UserDiscountRepository;
import com.exadel.discountwebapp.userdiscount.vo.UserDiscountRequestVO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDiscountService {

    private final String APP_URL;

    private final Integer QRCODE_WIDTH = 300;
    private final Integer QRCODE_HEIGHT = 300;

    private final UserDiscountRepository userDiscountRepository;
    private final UserDiscountMapper userDiscountMapper;
    private final QRCodeGenerator qrCodeGenerator;

    @Autowired
    public UserDiscountService(@Value("${qrcode.url}") String APP_URL,
                               UserDiscountRepository userDiscountRepository,
                               UserDiscountMapper userDiscountMapper,
                               QRCodeGenerator qrCodeGenerator) {
        this.APP_URL = APP_URL;
        this.userDiscountRepository = userDiscountRepository;
        this.userDiscountMapper = userDiscountMapper;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDiscount(UserDiscountRequestVO request) {
        UserDiscount.UserDiscountId userDiscountId = new UserDiscount.UserDiscountId(request.getUserId(), request.getDiscountId());
        if (userDiscountRepository.existsById(userDiscountId)) {
            throw new EntityAlreadyExistsException(UserDiscount.class, "id", userDiscountId);
        }
        UserDiscount userDiscount = userDiscountMapper.toEntity(request);
        userDiscountRepository.save(userDiscount);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public byte[] getQRCodeByUserDiscountId(Long userId, Long discountId) {
        UserDiscount.UserDiscountId userDiscountId = new UserDiscount.UserDiscountId(userId, discountId);
        UserDiscount userDiscount = userDiscountRepository.findById(userDiscountId).orElseThrow(()->new EntityNotFoundException(UserDiscount.class, "id", userDiscountId));
        return qrCodeGenerator.generateQRCodeImage(dataForQRCode(userDiscount), QRCODE_WIDTH, QRCODE_HEIGHT);
    }

    @SneakyThrows
    private String dataForQRCode(UserDiscount request) {
        return APP_URL + "/" + request.getUser().getId() + "/" + request.getDiscount().getId();
    }
}