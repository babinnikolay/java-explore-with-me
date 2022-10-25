package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.dto.UserDto;
import ru.practicum.explorewithme.model.mapper.UserMapper;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<Object> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<User> users;
        if (ids == null) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = new ArrayList<>(userRepository.findAllByIdIn(ids, page));
        }
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return ResponseEntity.ok(UserMapper.toUserDto(user));
    }

    public ResponseEntity<Object> deleteUser(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User userId=%s not found", userId));
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok(null);
    }
}
