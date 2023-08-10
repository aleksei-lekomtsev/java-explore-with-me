package ru.practicum.explorewithme.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public class EntityPageRequest extends PageRequest {
    public EntityPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
