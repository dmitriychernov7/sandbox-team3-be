package com.exadel.discountwebapp.discount.service;

import com.exadel.discountwebapp.common.BaseEntityMapper;
import com.exadel.discountwebapp.common.BaseFilterService;
import com.exadel.discountwebapp.fileupload.image.ImageUploadService;
import com.exadel.discountwebapp.discount.entity.Discount;
import com.exadel.discountwebapp.discount.mapper.DiscountMapper;
import com.exadel.discountwebapp.discount.repository.DiscountRepository;
import com.exadel.discountwebapp.discount.vo.DiscountRequestVO;
import com.exadel.discountwebapp.discount.vo.DiscountResponseVO;
import com.exadel.discountwebapp.exception.exception.client.EntityNotFoundException;
import com.exadel.discountwebapp.notification.event.EntityCreateEvent;
import com.exadel.discountwebapp.user.entity.User;
import com.exadel.discountwebapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DiscountService
        extends BaseFilterService<Discount, DiscountResponseVO> {

    private final DiscountMapper discountMapper;
    private final DiscountRepository discountRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Transactional(propagation = Propagation.REQUIRED)
    public DiscountResponseVO findByIdAndUpdateStatistics(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Discount.class, "id", id));
        Long quantity = discount.getViewNumber() == null ? 1 : discount.getViewNumber() + 1;
        discount.setViewNumber(quantity);
        discountRepository.save(discount);
        return discountMapper.toVO(discount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DiscountResponseVO create(DiscountRequestVO request) {
        Discount discount = discountMapper.toEntity(request);
        discountRepository.save(discount);

        EntityCreateEvent<Discount> event = new EntityCreateEvent<>(discount);
        eventPublisher.publishEvent(event);

        return discountMapper.toVO(discount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DiscountResponseVO update(Long id, DiscountRequestVO request) {
        Discount discount = getDiscountById(id);
        String imageUrl = discount.getImageUrl();

        discountMapper.updateEntity(request, discount);
        discountRepository.save(discount);

        if (imageUrl != null && !imageUrl.equals(request.getImageUrl())) {
            imageUploadService.delete(imageUrl);
        }

        return discountMapper.toVO(discount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteById(Long id) {
        Discount discount = getDiscountById(id);
        String imageUrl = discount.getImageUrl();
        discountRepository.deleteById(discount.getId());

        if (imageUrl != null) {
            imageUploadService.delete(imageUrl);
        }
    }

    private Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Discount.class, "id", id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addDiscountToFavorites(Long userId, Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException(Discount.class, "id", discountId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId));
        if (!discount.getUserFavorites().contains(user)) {
            discount.getUserFavorites().add(user);
            discountRepository.save(discount);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDiscountFromFavorites(Long userId, Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException(Discount.class, "id", discountId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId));
        discount.getUserFavorites().remove(user);
        discountRepository.save(discount);
    }

    @Override
    protected JpaSpecificationExecutor<Discount> getEntityRepository() {
        return discountRepository;
    }

    @Override
    protected BaseEntityMapper<Discount, DiscountResponseVO> getEntityToVOMapper() {
        return discountMapper;
    }
}