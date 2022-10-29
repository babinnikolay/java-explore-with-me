package ru.practicum.explorewithme.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.dto.CategoryDto;
import ru.practicum.explorewithme.model.dto.NewCategoryDto;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper mapper;

    public CategoryDto toCategoryDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    public Category toCategory(NewCategoryDto newCategoryDto) {
        return mapper.map(newCategoryDto, Category.class);
    }

    public Category toCategory(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }
}
