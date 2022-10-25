package ru.practicum.explorewithme.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.dto.UserDto;
import ru.practicum.explorewithme.model.dto.UserShortDto;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public static User toUser(UserDto userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

    public UserShortDto toUserShortDto(User initiator) {
        return mapper.map(initiator, UserShortDto.class);
    }
}
