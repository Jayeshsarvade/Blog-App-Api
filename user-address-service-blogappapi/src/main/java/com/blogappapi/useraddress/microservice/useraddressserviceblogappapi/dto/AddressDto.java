package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddressDto {

    private int id;

    @NotBlank(message = "Lane 1 cannot be null or empty.")
    private String lane1;

    @NotBlank(message = "Lane 1 cannot be null or empty.")
    private String lane2;

    @NotBlank(message = "City cannot be null or empty.")
    private String city;

    @NotBlank(message = "State cannot be null or empty.")
    private String state;
}
