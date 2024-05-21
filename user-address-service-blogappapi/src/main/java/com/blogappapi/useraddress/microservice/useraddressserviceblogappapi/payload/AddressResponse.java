package com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.payload;

import java.util.List;

import com.blogappapi.useraddress.microservice.useraddressserviceblogappapi.dto.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddressResponse {

    private List<AddressDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean lastPage;

}
