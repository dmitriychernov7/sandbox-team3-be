package com.exadel.discountwebapp.category.controller;

import com.exadel.discountwebapp.category.service.CategoryService;
import com.exadel.discountwebapp.category.vo.CategoryRequestVO;
import com.exadel.discountwebapp.category.vo.CategoryResponseVO;
import com.exadel.discountwebapp.tag.vo.TagRequestVO;
import com.exadel.discountwebapp.tag.vo.TagResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseVO> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseVO findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CategoryResponseVO create(@Valid @RequestBody CategoryRequestVO request) {
        return categoryService.create(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public CategoryResponseVO update(@PathVariable Long id, @Valid @RequestBody CategoryRequestVO request) {
        return categoryService.update(id, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{categoryId}/tags")
    public CategoryResponseVO addTags(@PathVariable Long categoryId, @Valid @RequestBody List<TagRequestVO> tagRequest) {
        return categoryService.addTags(categoryId, tagRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{categoryId}/tags")
    public CategoryResponseVO deleteTags(@PathVariable Long categoryId, @Valid @RequestBody List<Long> tagIds) {
        return categoryService.deleteTags(categoryId, tagIds);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
