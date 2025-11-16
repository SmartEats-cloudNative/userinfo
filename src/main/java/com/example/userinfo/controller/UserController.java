package com.example.userinfo.controller;

import com.example.userinfo.dto.UserDto;
import com.example.userinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto  userDto){
    UserDto result = userService.addUser(userDto);
    return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/fetchByUserId/{userId}")
    public ResponseEntity<UserDto> fetchUserById(@PathVariable Integer userId){
        return  userService.fetchUserById(userId);

    }


}
