package ru.practicum.explorewithme.comment;


import java.util.Collection;

public interface CommentService {
    Collection<CommentDto> findComments(Long eventId);

    CommentDto create(Long userId, Long eventId, CommentDto dto);

    CommentDto update(Long commentId, CommentDto dto);

    void delete(Long id);
}
