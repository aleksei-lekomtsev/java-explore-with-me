package ru.practicum.explorewithme.category;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CategoryPageRequest extends PageRequest {
    public CategoryPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
