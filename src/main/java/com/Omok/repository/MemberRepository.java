package com.Omok.repository;

import com.Omok.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByMemberId(String memberId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
