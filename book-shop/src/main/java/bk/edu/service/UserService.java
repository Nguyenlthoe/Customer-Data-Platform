package bk.edu.service;

import bk.edu.config.Config;
import bk.edu.data.entity.UserEntity;
import bk.edu.data.mapper.UserMapper;
import bk.edu.data.req.AuthRequest;
import bk.edu.data.req.LoginRequest;
import bk.edu.data.req.UserRequest;
import bk.edu.exception.RequestInvalid;
import bk.edu.repository.AuthDao;
import bk.edu.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthDao authDao;

    public Page<UserEntity> getListUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity createUser(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findByEmail(userRequest.getEmail());
        if(userEntity != null){
            throw new RequestInvalid("Email đã được đăng kí");
        }

        userEntity = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if(userEntity != null){
            throw new RequestInvalid("Số điện thoại đã được đăng kí");
        }
        UserEntity user = userMapper.userRequestToEntity(userRequest);
        userRepository.saveAndFlush(user);

        return user;
    }

    public UserEntity updateUser(UserRequest userRequest, Integer userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null){
            throw new RequestInvalid("user not found");
        }

        if(userRequest.getEmail() != null){
            userEntity.setEmail(userRequest.getEmail());
        }

        if(userRequest.getBirthday() != null){
            try {
                userEntity.setBirthday(Config.FORMAT_DATE.parse(userRequest.getBirthday()));
            } catch (ParseException e) {
                throw new RequestInvalid("format birthday must is: yyyy/MM/dd");
            }
        }

        if(userRequest.getName() != null){
            userEntity.setName(userRequest.getName());
        }

        if(userRequest.getGender() != null){
            userEntity.setGender(userRequest.getGender());
        }

        if(userRequest.getUrlAvatar() != null){
            userEntity.setUrlAvatar(userRequest.getUrlAvatar());
        }

        userEntity.setUpdatedAt(new Date());

        userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public String getToken(LoginRequest loginRequest, boolean isAdmin) {
        UserEntity userEntity = authDao.getUser(loginRequest, isAdmin);
        String jwtToken = JWT.create()
                .withIssuer("auth")
                .withSubject("sign in")
                .withClaim("user_id", userEntity.getUserId())
                .withClaim("email", userEntity.getEmail())
                .withClaim("phone_number", userEntity.getPhoneNumber())
                .withClaim("is_admin", isAdmin)
                .withClaim("username", userEntity.getName())
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID()
                        .toString())
                .sign(Config.JWT_ALGORITHM);
        return jwtToken;
    }

    public void checkToken(AuthRequest authRequest) {
        int userId = authRequest.getUserId();
        DecodedJWT jwt = Config.JWT_VERIFIER.verify(authRequest.getToken());
        Claim userClaim = jwt.getClaim("user_id");
        if(userId != userClaim.asInt()){
            throw new RequestInvalid("Token invalid");
        }
    }
}
