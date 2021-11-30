package com.professional.telegram_hyecorn_bot.service;

import com.professional.telegram_hyecorn_bot.model.User;
import com.professional.telegram_hyecorn_bot.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findByChatId(long id) {
        return userRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(User user){
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user){
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long userId){
        userRepository.deleteById(userId);
    }

}
