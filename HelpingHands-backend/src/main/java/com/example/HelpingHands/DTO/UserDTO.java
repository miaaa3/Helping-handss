package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

@Data
public class UserDTO {
    private UserEntity user;
    private int numberOfFollowing;
    private int numberOfFollowers;


}
