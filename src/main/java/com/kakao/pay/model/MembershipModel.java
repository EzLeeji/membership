package com.kakao.pay.model;

import com.kakao.pay.entity.MembershipDetailEntity;
import com.kakao.pay.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Builder
public class MembershipModel {
    private List<MembershipDetailModel> membershipDetailModels;

    public static MembershipModel setEntity(UserEntity userEntity) {
        return MembershipModel.builder()
                .membershipDetailModels(userEntity.getMembershipDetailList().stream().map(MembershipDetailModel::setEntity).collect(toList()))
                .build();
    }

    @Builder
    @Getter
    public static class MembershipDetailModel {
        private Long seq;
        private String membershipId;
        private String userId;
        private String membershipName;
        private LocalDateTime startDate;
        private String membershipStatus;
        private Long point;

        public static MembershipDetailModel setEntity(MembershipDetailEntity membershipDetailEntity) {
            return MembershipDetailModel.builder()
                    .seq(membershipDetailEntity.getSeq())
                    .membershipId(membershipDetailEntity.getMembershipId().getMembershipId())
                    .userId(membershipDetailEntity.getUserId().getUserId())
                    .membershipName(membershipDetailEntity.getMembershipId().getMembershipName())
                    .startDate(membershipDetailEntity.getStartDate())
                    .membershipStatus(membershipDetailEntity.getMembershipStatus())
                    .point(membershipDetailEntity.getPoint())
                    .build();
        }
    }
}