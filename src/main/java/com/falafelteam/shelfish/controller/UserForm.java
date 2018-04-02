package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.users.Role;
import com.falafelteam.shelfish.repository.RoleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class UserForm {

    @NotEmpty
    private String name;
    private String login;
    private String password;
    @NotEmpty
    private String address;
    private String role;
    private String phoneNumber;

    public void validate() throws Exception {
        Pattern loginPattern = Pattern.compile("[a-z]*[.][a-z]*@innopolis.ru");
        Matcher loginMatcher = loginPattern.matcher(login);
        if(!loginMatcher.matches()){
            throw new Exception("Login should be in format \"m.khazeev@innopolis.ru\"");
        }

        Pattern passwordPattern = Pattern.compile("[A-Za-z0-9]{6,}");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if(!passwordMatcher.matches()){
            throw new Exception("Password should contain at least 6 characters, that includes upper/lower case letters and digits 0-9");
        }
        Pattern phonePattern = Pattern.compile("[+][0-9]{11}");
        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);
        if(!phoneMatcher.matches()){
            throw new Exception("Password should contain at least 6 characters, that includes upper/lower case letters and digits 0-9");
        }
    }

}
