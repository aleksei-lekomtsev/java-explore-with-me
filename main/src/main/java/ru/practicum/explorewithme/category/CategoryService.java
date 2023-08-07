package ru.practicum.explorewithme.category;


import java.util.Collection;

public interface CategoryService {
    Collection<CategoryDto> findCategories(int from, int size);

    CategoryDto findById(Long id);

    CategoryDto create(NewCategoryDto dto);

    void delete(Long id);

    CategoryDto update(Long catId, NewCategoryDto dto);
}
