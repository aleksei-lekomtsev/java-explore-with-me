package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.user.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<CommentDto> findComments(Long eventId) {
        return commentRepository
                .findByEventId(eventId)
                .stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, CommentDto dto) {
        final Comment comment = mapper.toComment(dto);

        comment.setUser(userRepository.getReferenceById(userId));
        comment.setEvent(eventRepository.getReferenceById(eventId));

        return mapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(Long commentId, CommentDto dto) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Comment.class,
                            String.format("Entity with id=%d doesn't exist.", commentId));
                });

        if (comment.getText().equals(dto.getText())) {
            return mapper.toCommentDto(comment);
        }

        comment.setText(dto.getText());
        return mapper.toCommentDto(comment);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        }
    }
}
