package com.db.dogbook.book.dto.memberDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    private Long id;
    private String userId;
    private String password;
    private int age;
    private String email;
    private String phoneNumber;
}
