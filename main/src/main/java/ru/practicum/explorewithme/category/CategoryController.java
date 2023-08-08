package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CategoryDto> findCategories(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return service.findCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable Long catId) {
        return service.findById(catId);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Validated(CategoryCreateBasicInfo.class) NewCategoryDto dto) {
        return service.create(dto);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId,
                              @RequestBody @Validated(CategoryCreateBasicInfo.class) NewCategoryDto dto) {
        return service.update(catId, dto);
    }

    // > Почему NO_CONTENT?
    // Семен, привет! Если правильно понимаю, то по
    // спецификации API следует возвращать 204(Категория удалена)
    // Еще почитал здесь -
    // https://www.rfc-editor.org/rfc/rfc9110.html#section-9.3.5
    // > If a DELETE method is successfully applied, the origin server SHOULD send
    // 204 (No Content) status code if the action has been enacted and no further information is to be supplied
    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        service.delete(catId);
    }
}
