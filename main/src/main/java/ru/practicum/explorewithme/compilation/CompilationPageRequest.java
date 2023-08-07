package ru.practicum.explorewithme.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CompilationPageRequest extends PageRequest {
    public CompilationPageRequest(int from, int size) {
        super(from > 0 ? from / size : 0, size, Sort.unsorted());
    }
}
