package com.professional.telegram_hyecorn_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usr")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    //private Integer stateId;
    private String userState = "start";
    private String firstName;
    private String lastName;
    private String phone;
    @Email
    private String email;
    private int couponsNumber = 0;
    private LocalDateTime createDate = LocalDateTime.now();

    public User(Long chatId, String userState, String firstName, String lastName) {
        this.chatId = chatId;
        this.userState = userState;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
