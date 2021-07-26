package com.kakao.pay.entity;

import com.kakao.pay.model.UserParam;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "membership_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"userId","membershipId"})
public class MembershipDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long point;

    private String membershipStatus;

    private LocalDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private MembershipEntity membershipId;

    public static MembershipDetailEntity setParam(String userId, UserParam param) {
        return new MembershipDetailEntity(userId, param);
    }

    private MembershipDetailEntity(String userId, UserParam param) {
        UserEntity _userId = UserEntity.builder().userId(userId).build();
        MembershipEntity _membershipId = MembershipEntity.builder().membershipId(param.getMembershipId()).build();

        this.point = param.getPoint();
        this.userId = _userId;
        this.membershipId = _membershipId;
        this.membershipStatus = "Y";
        this.startDate = LocalDateTime.now();

    }

}
