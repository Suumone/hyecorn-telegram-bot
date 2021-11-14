package com.professional.telegram_hyecorn_bot.repo;

import com.professional.telegram_hyecorn_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByChatId(long id);

}
