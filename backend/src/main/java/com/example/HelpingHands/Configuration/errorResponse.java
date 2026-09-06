package com.example.HelpingHands.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class errorResponse {
    private HttpStatus statusCode;
    private String message;

}
