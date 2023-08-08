package ru.practicum.explorewithme.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

// > А есть смысл создавать еще один класс, который дублирует CategoryPageRequest?
// Думаю можно создать EntityPageRequest класс, чтобы не было дублей...
public class EntityPageRequest extends PageRequest {
    public EntityPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
