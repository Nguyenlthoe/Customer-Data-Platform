package bk.edu.data.mapper;

import bk.edu.config.Config;
import bk.edu.data.entity.UserEntity;
import bk.edu.data.req.UserRequest;
import bk.edu.data.response.dto.UserDto;
import bk.edu.exception.UserRequestInvalid;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    public UserDto userEntityToDto(UserEntity userEntity){
        String birthday = Config.FORMAT_DATE.format(userEntity.getBirthday());
        return new UserDto(userEntity.getUserId(), userEntity.getName(),
                userEntity.getEmail(), userEntity.getPhoneNumber(),
                userEntity.getGender(), birthday, userEntity.getUrlAvatar());
    }

    public UserEntity userRequestToEntity(UserRequest userRequest){
        UserEntity userEntity = new UserEntity();
        if(userRequest.getBirthday() != null){
            try {
                userEntity.setBirthday(Config.FORMAT_DATE.parse(userRequest.getBirthday()));
            } catch (ParseException e) {
                throw new UserRequestInvalid("format birthday must is: yyyy/MM/dd");
            }
        }

        userEntity.setName(userRequest.getName());
        userEntity.setAddress(userRequest.getAddress());
        userEntity.setGender(userRequest.getGender());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPhoneNumber(userRequest.getPhoneNumber());
        userEntity.setUrlAvatar(userRequest.getUrlAvatar());

        return userEntity;
    }

    public List<UserDto> listUserEntityToDto(List<UserEntity> list) {
        List<UserDto> users = new ArrayList<>();
        list.forEach(userEntity -> {
            users.add(userEntityToDto(userEntity));
        });
        return users;
    }
}
