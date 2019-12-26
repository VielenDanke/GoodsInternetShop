package kz.epam.InternetShop.payload;

import lombok.Data;

@Data
public class UpdateRequest {

    private String fullName;
    private String address;
}
