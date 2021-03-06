package com.exadel.discountwebapp.vendor.service;

import com.exadel.discountwebapp.exception.exception.client.EntityNotFoundException;
import com.exadel.discountwebapp.exception.exception.client.IncorrectFilterInputException;
import com.exadel.discountwebapp.fileupload.image.ImageUploadService;
import com.exadel.discountwebapp.user.repository.UserRepository;
import com.exadel.discountwebapp.vendor.entity.Vendor;
import com.exadel.discountwebapp.vendor.repository.VendorRepository;
import com.exadel.discountwebapp.vendor.vo.VendorRequestVO;
import com.exadel.discountwebapp.vendor.vo.VendorResponseVO;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/vendor-init.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/clean-up.sql")
class VendorServiceIntegrationTest {

    @Autowired
    private VendorService vendorService;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ImageUploadService imageUploadService;

    @Test
    void shouldFindVendorById() {
        var id = 1L;
        var expected = vendorRepository.findById(id).get();
        var actual = vendorService.findById(id);

        matchOne(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfNoVendorFoundById() {
        var id = 345345L;
        assertThrows(EntityNotFoundException.class, () -> {
            vendorService.findById(id);
        });
    }

    @Test
    void shouldFindVendorByTitle() {
        var title = "Sport Life";
        var expected = vendorRepository.findByTitle(title).get();
        var actual = vendorService.findByTitle(title);

        matchOne(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfNoVendorFoundByTitle() {
        var title = "Test wrong title";
        assertThrows(EntityNotFoundException.class, () -> {
            vendorService.findByTitle(title);
        });
    }

    @Test
    void shouldFindAllVendors() {
        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter);

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(null, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsAndSortByTitleWithDirAsc() {
        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter);
        expected.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));

        var sortField = "title";
        var sortDir = Sort.Direction.ASC;
        var sort = Sort.by(sortDir, sortField);

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount, sort);
        var actual = vendorService.findAll(null, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsAndSortByTitleWithDirDesc() {
        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter);
        expected.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));

        var sortField = "title";
        var sortDir = Sort.Direction.DESC;
        var sort = Sort.by(sortDir, sortField);

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount, sort);
        var actual = vendorService.findAll(null, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereIdLessThanTwo() {
        var id = 2L;
        var query = "id<" + id;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getId() < id).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereIdGreaterThanTwo() {
        var id = 2L;
        var query = "id>" + id;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getId() > id).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereCreatedDateBetweenTwoDates() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var firstDate = LocalDateTime.parse("2021-06-06 17:22:21", formatter);
        var secondDate = LocalDateTime.parse("2023-06-06 17:22:21", formatter);
        var query = String.format("created>%s;created<%s", firstDate, secondDate);

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getCreated().isAfter(firstDate) && e.getCreated().isBefore(secondDate)).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereTitleEqualsSportLife() {
        var title = "sport life";
        var query = "title:" + title;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereTitleStartsWithSport() {
        var title = "sport";
        var query = "title:*" + title;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getTitle().toLowerCase().startsWith(title)).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereTitleEndsWithLife() {
        var title = "life";
        var query = "title*:" + title;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getTitle().toLowerCase().endsWith(title)).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereDescriptionContainsPizza() {
        var description = "pizza";
        var query = "description*:*" + description;

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream().filter(e -> e.getDescription().toLowerCase().contains(description)).collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    void shouldFindAllVendorsWhereCityEqualsLviv() {
        var city = "lviv";
        var query = "locations.city:" + city;

        var expectedIter = vendorRepository.findAll();

        var expected = Lists.newArrayList(expectedIter).stream()
                .filter(e -> e.getLocations().stream()
                        .anyMatch(l -> l.getCity().equalsIgnoreCase(city)))
                .collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    void shouldFindAllVendorsWhereTitleStartsWithSportAndDescriptionContainsCasualAndCityEqualsKyiv() {
        var title = "sport";
        var description = "casual";
        var city = "kyiv";
        var query = String.format("title:*%s;description*:*%s;locations.city:%s", title, description, city);

        var expectedIter = vendorRepository.findAll();
        var expected = Lists.newArrayList(expectedIter)
                .stream()
                .filter(e -> e.getTitle().toLowerCase().startsWith(title) &&
                        e.getDescription().toLowerCase().contains(description)
                        &&
                        e.getLocations().stream().anyMatch(l -> l.getCity().equalsIgnoreCase(city))
                )
                .collect(Collectors.toList());

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAll(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfFieldDoesNotExistInVendor() {
        var field = "I don't exist";
        var query = field + ":some value";

        var pageable = PageRequest.of(0, 20);

        assertThrows(IncorrectFilterInputException.class, () -> {
            vendorService.findAll(query, pageable);
        });
    }

    @Test
    void shouldCreateVendor() {
        var expected = createVendorRequest();
        var actual = vendorService.create(expected);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        matchOne(expected, actual);
    }

    @Test
    void shouldUpdateVendorById() {
        var id = 1L;
        var expected = updateVendorRequest();
        var actual = vendorService.update(id, expected);

        assertNotNull(actual);
        assertEquals(actual.getId(), id);

        matchOne(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereTitleContainsSportOrPizza() {
        var firstTitle = "sport";
        var secondTitle = "pizza";

        var query = String.format("title?*:*%s;title?*:*%s;", firstTitle, secondTitle);

        var firstVendor = Vendor.builder()
                .id(1L)
                .title("Sport Life")
                .description("Sport Life - a chain of casual fitness centers")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("sprort_life@com.ua")
                .created(LocalDateTime.parse("2021-12-06T17:22:21"))
                .modified(LocalDateTime.parse("2021-12-06T17:22:21"))
                .build();

        var secondVendor = Vendor.builder()
                .id(2L)
                .title("Domino`s Pizza")
                .description("Domino`s Pizza - an American multinational pizza restaurant chain founded in 1960")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("dominos@gmail.com")
                .created(LocalDateTime.parse("2022-06-06T17:22:21"))
                .modified(LocalDateTime.parse("2022-06-06T17:22:21"))
                .build();

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);

        var expected = List.of(firstVendor, secondVendor);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAllPure(expected, actual);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    void shouldFindAllVendorsWhereDescriptionContainsTravelOrPizzaAndEmailEndsWithCom() {
        var firstDesc = "travel";
        var secondDesc = "pizza";
        var emailEnds = "com";

        var query = String.format("description?*:*%s;description?*:*%s;email*:%s",
                firstDesc, secondDesc, emailEnds);

        var firstVendor = Vendor.builder()
                .id(2L)
                .title("Domino`s Pizza")
                .description("Domino`s Pizza - an American multinational pizza restaurant chain founded in 1960")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("dominos@gmail.com")
                .created(LocalDateTime.parse("2022-06-06T17:22:21"))
                .modified(LocalDateTime.parse("2022-06-06T17:22:21"))
                .build();

        var secondVendor = Vendor.builder()
                .id(3L)
                .title("TUI")
                .description("TUI AG - travel and tourism company")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("tuigroup@gmail.com")
                .created(LocalDateTime.parse("2023-06-06T17:22:21"))
                .modified(LocalDateTime.parse("2023-06-06T17:22:21"))
                .build();

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);

        var expected = List.of(firstVendor, secondVendor);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAllPure(expected, actual);
    }

    @Test
    void shouldFindAllVendorsWhereTitleNotEqualsDominoPizza() {
        var title = "Domino`s Pizza";

        var query = String.format("title!:%s", title);

        var firstVendor = Vendor.builder()
                .id(1L)
                .title("Sport Life")
                .description("Sport Life - a chain of casual fitness centers")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("sprort_life@com.ua")
                .created(LocalDateTime.parse("2021-12-06T17:22:21"))
                .modified(LocalDateTime.parse("2021-12-06T17:22:21"))
                .build();

        var secondVendor = Vendor.builder()
                .id(3L)
                .title("TUI")
                .description("TUI AG - travel and tourism company")
                .imageUrl("https://res.cloudinary.com/hudrds7km/image/upload/v1626823788/ltcgv0hmuszxheoa6i1p.png")
                .email("tuigroup@gmail.com")
                .created(LocalDateTime.parse("2023-06-06T17:22:21"))
                .modified(LocalDateTime.parse("2023-06-06T17:22:21"))
                .build();

        var vendorCount = (int) vendorRepository.count();
        var pageable = PageRequest.of(0, vendorCount);

        var expected = List.of(firstVendor, secondVendor);
        var actual = vendorService.findAll(query, pageable).getContent();

        matchAllPure(expected, actual);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void shouldSubscribe() {
        var user = userRepository.findById(1L).orElse(null);
        var vendor = vendorRepository.findById(1L).orElse(null);

        assertNotNull(user);
        assertNotNull(vendor);

        assertFalse(vendor.getSubscribers().contains(user));

        assertNotNull(vendor.getId());
        assertNotNull(user.getEmail());

        vendorService.subscribe(vendor.getId(), user.getEmail());

        assertTrue(vendor.getSubscribers().contains(user));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void shouldUnsubscribe() {
        var user = userRepository.findById(1L).orElse(null);
        var vendor = vendorRepository.findById(1L).orElse(null);

        assertNotNull(user);
        assertNotNull(vendor);

        assertFalse(vendor.getSubscribers().contains(user));

        assertNotNull(vendor.getId());
        assertNotNull(user.getEmail());

        vendorService.subscribe(vendor.getId(), user.getEmail());

        assertTrue(vendor.getSubscribers().contains(user));

        vendorService.unsubscribe(vendor.getId(), user.getEmail());

        assertFalse(vendor.getSubscribers().contains(user));
    }

    private VendorRequestVO createVendorRequest() {
        var title = "title";
        var description = "description";
        var imageUrl = "http://localhost/images/img.png";
        var email = "testemail@gmail.com";

        return VendorRequestVO.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .email(email)
                .locationIds(List.of(1L, 2L))
                .build();
    }

    private VendorRequestVO updateVendorRequest() {
        var title = "titleSome";
        var description = "descriptionSome";
        var imageUrl = "http://localhost/images/Someimg.png";
        var email = "sprort_life@com.ua";

        return VendorRequestVO.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .email(email)
                .locationIds(List.of(2L))
                .build();
    }

    private void matchOne(Vendor expected, VendorResponseVO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    private void matchOne(VendorRequestVO expected, VendorResponseVO actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getLocationIds().get(0), actual.getLocations().get(0).getId());
    }

    private void matchOnePure(Vendor expected, VendorResponseVO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    private void matchAll(List<Vendor> expected, List<VendorResponseVO> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            matchOne(expected.get(i), actual.get(i));
        }
    }

    private void matchAllPure(List<Vendor> expected, List<VendorResponseVO> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            matchOnePure(expected.get(i), actual.get(i));
        }
    }
}
