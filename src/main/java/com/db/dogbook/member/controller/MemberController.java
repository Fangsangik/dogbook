package com.db.dogbook.member.controller;

import com.db.dogbook.member.memberDto.MemberDto;
import com.db.dogbook.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/memberId")
    public ResponseEntity<MemberDto> findMember(@PathVariable MemberDto memberDto) {
        MemberDto findMember = memberService.findMemberById(memberDto);
        return ResponseEntity.ok(findMember);
    }

    @PostMapping("/create")
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        MemberDto createMember = memberService.create(memberDto);
        return ResponseEntity.ok(createMember);
    }

    @PostMapping("/update")
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto) {
        MemberDto updateMember = memberService.update(memberDto);
        return ResponseEntity.ok(updateMember);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MemberDto> deleteMember(@RequestParam MemberDto memberDto) {
        memberService.delete(memberDto.getId());
        //return ResponseEntity.ok(memberDto);
        return ResponseEntity.noContent().build(); //삭제 후 응답 No
    }
}
