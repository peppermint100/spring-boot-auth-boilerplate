package io.peppermint100.server.entity.Response.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
}
