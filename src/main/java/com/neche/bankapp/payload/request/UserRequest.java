package com.neche.bankapp.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 125, message = "First name must be at least 2 characters long")
    private String firstName;

    private  String lastName;

    private String otherName;

    private String email;

    private String password;

    private String gender;

    private String address;

    private String stateOfOrigin;

    private String phoneNumber;

    private String BVN;

    private String pin;

//    private String accountNumber;
//
//    private String bankName;
//
//    private String profilePicture;
//
//    private String status;


}
