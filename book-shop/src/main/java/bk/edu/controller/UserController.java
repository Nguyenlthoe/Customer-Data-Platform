package bk.edu.controller;

import bk.edu.data.entity.UserEntity;
import bk.edu.data.mapper.UserMapper;
import bk.edu.data.req.AuthRequest;
import bk.edu.data.req.LoginRequest;
import bk.edu.data.req.UserRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.UserDto;
import bk.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        UserEntity userEntity = userService.createUser(userRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(userMapper.userEntityToDto(userEntity))
                .get();
        return ResponseEntity
                .ok(response);
    }

    @RequestMapping(value = "/all/user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() {
        List<UserEntity> userEntities = userService.getAllUser();

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(userMapper.listUserEntityToDto(userEntities))
                .get();
        return ResponseEntity
                .ok(response);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest,
                                        @RequestParam Integer userId) {
        UserEntity userEntity = userService.updateUser(userRequest, userId);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(userMapper.userEntityToDto(userEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest){
        String jwt = userService.getToken(loginRequest,false);
        Map<String, String> mapReturn = new HashMap<>();
        mapReturn.put("token", jwt);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    };

    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest){
        String jwt = userService.getToken(loginRequest,true);
        Map<String, String> mapReturn = new HashMap<>();
        mapReturn.put("token", jwt);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    };

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest){
        userService.checkToken(authRequest);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .get();
        return ResponseEntity.ok(response);
    };

    @RequestMapping(value = "/user/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId, 10, Sort.by("createdAt").descending());
        Page<UserEntity> userEntityPage = userService.getListUser(pageable);
        List<UserDto> userDtoList = userMapper.listUserEntityToDto(userEntityPage.toList());
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(userDtoList)
                .get();
        return ResponseEntity.ok(response);
    }


}
