package com.kakao.pay.repository;

import com.kakao.pay.entity.MembershipDetailEntity;
import com.kakao.pay.entity.MembershipEntity;
import com.kakao.pay.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipDetailRepository extends JpaRepository<MembershipDetailEntity, Long> {
    Optional<MembershipDetailEntity> findByUserIdAndMembershipId(UserEntity userId, MembershipEntity membershipId);
    List<MembershipDetailEntity> findByUserId(UserEntity userId);
}
