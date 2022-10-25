package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.CategoryDto;
import ru.practicum.explorewithme.model.dto.NewCategoryDto;
import ru.practicum.explorewithme.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryDto categoryDto)
            throws BadRequestException, NotFoundException {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto)
            throws BadRequestException {
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@NotNull @PathVariable Long catId)
            throws BadRequestException, NotFoundException {
        return categoryService.deleteCategory(catId);
    }
}
