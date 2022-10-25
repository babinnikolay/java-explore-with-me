package ru.practicum.explorewithme.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.explorewithme.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    Boolean existsByNameIgnoreCase(String name);
}
