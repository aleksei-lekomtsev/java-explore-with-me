package ru.practicum.explorewithme.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class EventPageRequest extends PageRequest {
    public EventPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
