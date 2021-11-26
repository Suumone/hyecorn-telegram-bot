package com.professional.telegram_hyecorn_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.ws.rs.DefaultValue;
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
    private Integer stateId;
    //private String state;
    private String firstName;
    private String lastName;
    private String phone;
    @Email
    private String email;
    @DefaultValue("0")
    private int couponsNumber;
    private LocalDateTime createDate;

    public User(Long chatId, Integer state, String firstName, String lastName) {
        this.chatId = chatId;
        this.stateId = state;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
