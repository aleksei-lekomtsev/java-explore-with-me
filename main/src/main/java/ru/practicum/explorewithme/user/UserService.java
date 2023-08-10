package ru.practicum.explorewithme.user;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findUsers(Long[] ids, int from, int size);

    UserDto create(UserDto dto);

    void delete(Long id);
}
