package com.falafelteam.shelfish.model.users;

import com.falafelteam.shelfish.model.DocumentUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.DETACH)
    @Getter
    @Setter
    private Role role;

    public User(String name, String login, String password, String address, String phoneNumber, Role role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

//    @Override
//    public String toString(){
//        return "";
//    }
}
