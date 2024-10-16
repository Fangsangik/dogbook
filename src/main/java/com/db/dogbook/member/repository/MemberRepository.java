package com.db.dogbook.member.repository;

import com.db.dogbook.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUserId(String userId);
}
