package com.demo.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext implements Serializable {

    private static final long serialVersionUID = -8099349063069095844L;

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String type;
}

