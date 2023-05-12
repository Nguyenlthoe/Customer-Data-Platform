package bk.edu.service;

import bk.edu.data.entity.UserEntity;
import bk.edu.data.mapper.UserMapper;
import bk.edu.data.req.UserRequest;
import bk.edu.exception.UserRequestInvalid;
import bk.edu.repository.AuthorRepository;
import bk.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public Page<UserEntity> getListUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity createUser(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findByEmail(userRequest.getEmail());
        if(userEntity != null){
            throw new UserRequestInvalid("Email đã được đăng kí");
        }

        userEntity = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if(userEntity != null){
            throw new UserRequestInvalid("Số điện thoại đã được đăng kí");
        }
        UserEntity user = userMapper.userRequestToEntity(userRequest);
        userRepository.saveAndFlush(user);

        return user;
    }
}
