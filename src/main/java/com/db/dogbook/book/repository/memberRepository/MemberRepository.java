package com.db.dogbook.book.repository.memberRepository;

import com.db.dogbook.book.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUserId(String userId);
}
