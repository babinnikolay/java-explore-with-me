package ru.practicum.explorewithme.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.dto.CategoryDto;
import ru.practicum.explorewithme.model.dto.NewCategoryDto;

@Component
public class CategoryMapper {
    private static ModelMapper mapper;

    @Autowired
    public CategoryMapper(ModelMapper modelMapper) {
        CategoryMapper.mapper = modelMapper;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return mapper.map(newCategoryDto, Category.class);
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }
}
