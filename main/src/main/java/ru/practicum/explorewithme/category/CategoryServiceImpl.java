package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.util.EntityPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    private boolean doesNameExist(NewCategoryDto dto) {
        return !categoryRepository
                .findByName(dto.getName())
                .isEmpty();
    }

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        if (doesNameExist(dto)) {
            throw new ConflictException(String.format("Category with name: %s already exists.", dto.getName()));
        }
        return mapper.toCategoryDto(categoryRepository.save(mapper.toCategory(dto)));
    }

    @Transactional
    @Override
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        final Category category = categoryRepository.findById(catId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Category.class,
                            String.format("Entity with id=%d doesn't exist.", catId));
                });

        if (category.getName().equals(dto.getName())) {
            return mapper.toCategoryDto(category);
        }

        if (doesNameExist(dto)) {
            throw new ConflictException(String.format("Category with name: %s already exists.", dto.getName()));
        }

        category.setName(dto.getName());
        return mapper.toCategoryDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> findCategories(int from, int size) {
        final PageRequest pageRequest = new EntityPageRequest(from, size);

        return categoryRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto findById(Long id) {
        return mapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Category.class,
                            String.format("Entity with id=%d doesn't exist.", id));
                }));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!eventRepository.findByCategoryId(id).isEmpty()) {
            throw new ConflictException(String.format("Event with category id: %s exists.", id));
        }
        categoryRepository.deleteById(id);
    }
}