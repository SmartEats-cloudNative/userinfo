package com.example.userinfo.service;

import com.example.userinfo.dto.UserDto;
import com.example.userinfo.entity.User;
import com.example.userinfo.mapper.UserMapper;
import com.example.userinfo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;


    public UserDto addUser(UserDto userDto) {
       User userEntity =  userRepo.save(UserMapper.INSTANCE.toEntity(userDto));
        return UserMapper.INSTANCE.toDto(userEntity);
    }

    public ResponseEntity<UserDto> fetchUserById(Integer UserId) {
        Optional<User> user =  userRepo.findById(UserId);
        return user.map(value -> new ResponseEntity<>(UserMapper.INSTANCE.toDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
