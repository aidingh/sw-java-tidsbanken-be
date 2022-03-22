package com.example.timebankapiproject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Auth0User {
    private String id;
    private String email;
    private String nickname;
}
