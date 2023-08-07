package ru.practicum.explorewithme.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class UserPageRequest extends PageRequest {
    public UserPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
