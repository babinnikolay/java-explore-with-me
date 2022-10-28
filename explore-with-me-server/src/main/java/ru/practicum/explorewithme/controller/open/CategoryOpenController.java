package ru.practicum.explorewithme.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.service.CategoryService;

@Controller
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryOpenController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size)
            throws NotFoundException {
        log.info("Get categories request from={}, size={}", from, size);
        return ResponseEntity.ok(categoryService.getCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable Long catId) throws NotFoundException {
        log.info("Get category request catId={}", catId);
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }
}
