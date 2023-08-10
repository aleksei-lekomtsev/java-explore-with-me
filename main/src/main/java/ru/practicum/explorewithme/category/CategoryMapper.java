package ru.practicum.explorewithme.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(NewCategoryDto dto);

    NewCategoryDto toNewCategoryDto(Category category);

    Category toCategory(CategoryDto dto);

    CategoryDto toCategoryDto(Category category);
}
