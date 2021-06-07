package com.exadel.discountwebapp.user.service;

import com.exadel.discountwebapp.user.entity.User;
import com.exadel.discountwebapp.user.repository.UserRepository;
import com.exadel.discountwebapp.user.vo.UserResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseVO findById(long id) {
        User user = userRepository.findById(id).orElse(null);
        return user!=null ? UserResponseVO.fromUser(user) : null;
    }

    public List<UserResponseVO> findAll() {
        List<UserResponseVO> response = new ArrayList<>();
        userRepository.findAll()
                        .forEach(user -> response
                                .add(UserResponseVO.fromUser(user)));
        return response;
    }
}