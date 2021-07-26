package com.kakao.pay.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "membership")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"membershipDetailList"})
public class MembershipEntity {
    @Id
    private String membershipId;

    private String membershipName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private List<MembershipDetailEntity> membershipDetailList;
}
