package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.util.EntityPageRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    private boolean doesEmailExist(UserDto dto) {
        return !repository
                .findByEmail(dto.getEmail())
                .isEmpty();
    }

    @Transactional
    @Override
    public UserDto create(UserDto dto) {
        if (doesEmailExist(dto)) {
            throw new ConflictException(String.format("User with email: %s already exists.", dto.getEmail()));
        }
        return mapper.toDto(repository.save(mapper.toUser(dto)));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> findUsers(Long[] ids, int from, int size) {
        final PageRequest pageRequest = new EntityPageRequest(from, size);

        final Page<User> page = ids.length == 0
                ? repository.findAll(pageRequest)
                : repository.findByIdIn(Arrays.asList(ids), pageRequest);
        return page
                .getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}