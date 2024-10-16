package com.db.dogbook.member.memberDto;

import com.db.dogbook.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MemberDto convertMemberDto(Member member) {
        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .email(member.getEmail())
                .age(member.getAge())
                .phoneNumber(member.getPhoneNumber())
                .password(member.getPassword())
                .build();

        return memberDto;
    }
}
