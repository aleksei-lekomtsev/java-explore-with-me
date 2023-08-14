package ru.practicum.explorewithme.comment;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping(path = "/users/comments/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> findComments(@PathVariable Long eventId) {
        return service.findComments(eventId);
    }

    @PostMapping(path = "/users/{userId}/comments/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId, @PathVariable Long eventId,
                             @RequestBody @Validated CommentDto dto) {
        return service.create(userId, eventId, dto);
    }

    @PatchMapping("/users/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@PathVariable Long commentId, @RequestBody @Validated CommentDto dto) {
        return service.update(commentId, dto);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        service.delete(commentId);
    }
}
