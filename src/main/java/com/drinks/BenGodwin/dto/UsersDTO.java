package com.drinks.BenGodwin.dto;

import com.drinks.BenGodwin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
public class UsersDTO {

    private Long id;
    private String username;
    private Set<Role> roles;


}
