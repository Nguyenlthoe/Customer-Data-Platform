package bk.edu.data.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {
    private String phoneNumber;

    private String address;

    private String name;
}
