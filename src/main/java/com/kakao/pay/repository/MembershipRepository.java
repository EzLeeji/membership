package com.kakao.pay.repository;

import com.kakao.pay.entity.MembershipDetailEntity;
import com.kakao.pay.entity.MembershipEntity;
import com.kakao.pay.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<MembershipEntity, String> {
    Optional<MembershipEntity> findByMembershipIdAndMembershipName(String membershipId, String membershipName);
}
