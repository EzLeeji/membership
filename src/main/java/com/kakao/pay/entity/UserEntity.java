package com.kakao.pay.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uesrs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"membershipDetailList"})
public class UserEntity {
    @Id
    private String userId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<MembershipDetailEntity> membershipDetailList;
}
