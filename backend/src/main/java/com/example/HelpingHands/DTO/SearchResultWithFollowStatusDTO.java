package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

@Data
public class SearchResultWithFollowStatusDTO {
    private UserEntity user;
    private boolean isFollowed;
}
