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

        // > Данной проверки достаточно?
        // Если я все правильно понимаю, то пока не вижу какая тут может быть еще проверка)
        // Я получаю CommentDto, в котором есть text поле с NotBlank, Size аннотациями. Если выполнение программы
        // дошло сюда, значит информация в text прошла проверки на размер, пробелы, not null..
        // В if-блоке ниже я проверяю, что если текст в обновлении идентичен тексту сохраненного комментария, то
        // обновлять ничего не нужно...
        if (comment.getText().equals(dto.getText())) {
            return mapper.toCommentDto(comment);
        }

        comment.setText(dto.getText());
        return mapper.toCommentDto(comment);
    }

    // Семен, привет!
    // > А здесь не нужна проверка, что id существует?
    // Если комментарий с переданным id не существует, то deleteById метод
    // пробросит исключение EmptyResultDataAccessException и приложение "упадёт"
    // Поэтому думаю, что нужно сделать проверку что id существует предварительно
    @Transactional
    @Override
    public void delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        }
    }
}
