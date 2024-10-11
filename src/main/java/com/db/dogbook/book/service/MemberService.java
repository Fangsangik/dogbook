package com.db.dogbook.book.service;

import com.db.dogbook.book.dto.memberDto.MemberConverter;
import com.db.dogbook.book.dto.memberDto.MemberDto;
import com.db.dogbook.book.model.Member;
import com.db.dogbook.book.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    //Read
    @Transactional(readOnly = true)
    public MemberDto findMemberById(MemberDto memberDto) {

        if (memberDto.getUserId() == null) {
            log.warn("Id must not be null");
            throw new IllegalArgumentException("아이디 값이 null이면 안됩니다.");
        }

        Member member = memberRepository.findByUserId(memberDto.getUserId());

        if (member == null) {
            log.warn("member not found" + memberDto.getUserId());
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다.");
        }
        log.info("아이디를 찾았습니다. "  + member.getUserId());

        //converter
        return memberConverter.convertMemberDto(member);
    }

    //create
    @Transactional
    public MemberDto create(MemberDto memberDto) {
        if (memberDto.getUserId() == null) {
            log.warn("Id must not be null");
            throw new IllegalArgumentException("아이디 값이 null 이면 안됩니다.");
        }

        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .password(memberDto.getPassword()) //passwordEncoder 사용 예정
                .email(memberDto.getEmail())
                .age(memberDto.getAge())
                .phoneNumber(String.valueOf(memberDto.getPhoneNumber()))
                .build();

        Member createMember = memberRepository.save(member);

        //converter
        return memberConverter.convertMemberDto(createMember);
    }

    //update
    @Transactional
    public MemberDto update(MemberDto memberDto) {
        Member updateMember = memberRepository.findByUserId(memberDto.getUserId());
        if (updateMember.getUserId() == null) {
            log.warn("Id must not be null");
            throw new IllegalArgumentException("Id가 null 이면 안됩니다.");
        }

        updateMember.setUserId(updateMember.getUserId());
        updateMember.setPassword(updateMember.getPassword());
        updateMember.setEmail(updateMember.getEmail());
        updateMember.setAge(updateMember.getAge());
        updateMember.setPhoneNumber(updateMember.getPhoneNumber());

        Member saveMember = memberRepository.save(updateMember);
        return memberConverter.convertMemberDto(saveMember);
    }

    //delete
    @Transactional
    public MemberDto delete(Long id) {
        // 회원을 ID로 조회, 없으면 예외 처리
        Member deleteMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        // 회원 삭제
        memberRepository.deleteById(deleteMember.getId());
        // 삭제된 회원 정보를 DTO로 변환 후 반환
        return memberConverter.convertMemberDto(deleteMember);
    }
}
