package com.kakao.pay.service;

import com.kakao.pay.entity.MembershipDetailEntity;
import com.kakao.pay.entity.MembershipEntity;
import com.kakao.pay.entity.UserEntity;
import com.kakao.pay.exception.CustomException;
import com.kakao.pay.exception.CustomTypeException;
import com.kakao.pay.model.MembershipModel;
import com.kakao.pay.model.UserParam;
import com.kakao.pay.repository.MembershipDetailRepository;
import com.kakao.pay.repository.MembershipRepository;
import com.kakao.pay.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MembershipServiceTest {
    private static final String userId1 = "test1";
    private static final String userId2 = "test2";
    private static final String userId3 = "test3";

    private static final String membershipId1 = "spc";
    private static final String membershipId2 = "shinsegae";
    private static final String membershipId3 = "cj";
    private static final String membershipId4 = "tempId";

    private static final String membershipIdName1 = "happypoint";
    private static final String membershipIdName2 = "shinsegaepoint";
    private static final String membershipIdName3 = "cjone";
    private static final String membershipIdName4 = "tempName";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipDetailRepository membershipDetailRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipService service;


    @Test
    @DisplayName("유저 조회")
    void 유저테이블_조회() {
        // given
        UserEntity userEntity = UserEntity.builder().userId(userId1).build();

        //when
        userRepository.save(userEntity);
        UserEntity userEntity2 = service.getUserInfo(userId1);

        //then
        Assertions.assertNotNull(userEntity2);
    }

    @Test
    @DisplayName("유저 조회 없으면 에러")
    void 유저테이블_조회_없으면_에러() {
        // given
        UserEntity userEntity = UserEntity.builder().userId(userId1).build();

        //when
        userRepository.save(userEntity);

        Throwable customException = assertThrows(CustomException.class, () ->  service.getUserInfo(userId2));

        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_USER_SIGN_MEMBERSHIP.getMessage());
    }

    @Test
    @DisplayName("멤버십 정보 조회 - 아이디로")
    void 멤버십_정보_조회() {
        //given - when
        MembershipEntity membershipEntity1 = service.getMembershipInfo(membershipId1);
        //then
        Assertions.assertNotNull(membershipEntity1);
    }

    @Test
    @DisplayName("멤버십 정보 조회2 - 아이디와 네임")
    void 멤버십_정보_조회2() {
        //given - when
        MembershipEntity membershipEntity1 = service.getMembershipInfo(membershipId1,membershipIdName1);
        //then
        Assertions.assertNotNull(membershipEntity1);
    }

    @Test
    @DisplayName("멤버십 정보 조회 없으면 에러 - 아이디로")
    void 멤버십_정보_조회_없으면_에러() {
        //given
        //when
        Throwable customException = assertThrows(CustomException.class, () ->  service.getMembershipInfo(membershipId4));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_MEMBERSHIP_COMPANY.getMessage());
    }

    @Test
    @DisplayName("멤버십 정보 조회 없으면 에러 - 아이디와 네임")
    void 멤버십_정보_조회_없으면_에러2() {
        //given
        //when
        Throwable customException = assertThrows(CustomException.class, () ->  service.getMembershipInfo(membershipId4,membershipIdName4));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_MEMBERSHIP_COMPANY.getMessage());
    }

    @Test
    @DisplayName("1. 멤버십 전체 조회하기")
    void 멤버십_전체_조회() {
        //given
        setData();
        //when
        List resultList = service.SearchAllMembership(userId1);
        //then
        Assertions.assertNotNull(resultList);
    }

    @Test
    @DisplayName("1. 멤버십 전체 조회하기 - 유저정보 없을 시")
    void 멤버십_전체_조회_에러() {
        //given
        setData();
        //when
        Throwable customException = assertThrows(CustomException.class, () ->  service.SearchAllMembership(userId2));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_USER_SIGN_MEMBERSHIP.getMessage());
    }

    @Test
    @DisplayName("2. 멤버십 등록하기")
    void 멤버십_등록하기(){
        //given
        UserParam param = new UserParam();
        param.setMembershipId("spc");
        param.setMembershipName("happypoint");
        param.setPoint(1000L);
        //when
        List resultList = service.insertMembership(userId1,param);
        //then
        Assertions.assertNotNull(resultList);
    }

    @Test
    @DisplayName("2. 멤버십 등록하기 - 이미 가입된 사용자")
    void 멤버십_등록하기_이미_가입됨(){
        //given
        UserParam param = new UserParam();
        param.setMembershipId(membershipId1);
        param.setMembershipName(membershipIdName1);
        param.setPoint(1000L);
        setData();
        //when
        Throwable customException = assertThrows(CustomException.class, () ->  service.insertMembership(userId1,param));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.ALREADY_USER_SIGN_MEMBERSHIP.getMessage());
    }

    @Test
    @DisplayName("3. 멤버십 삭제(해지)하기")
    void 멤버십_삭제하기(){
        //given
        setData();
        //when
        Boolean result = service.deleteMembership(userId1,membershipId1);
        //then
        assertThat(true).isEqualTo(result);
    }

    @Test
    @DisplayName("3. 멤버십 삭제(해지)하기 - 가입안한 사람일 경우")
    void 멤버십_삭제하기_가입안한_사람일경우(){
        //given
        setData();
        //when
        Throwable customException = assertThrows(CustomException.class, () -> service.deleteMembership(userId2,membershipId1));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_USER_SIGN_MEMBERSHIP.getMessage());
    }

    @Test
    @DisplayName("4. 멤버십 상세 조회")
    void 멤버십_상세조회(){
        //given
        setData();
        //when
        MembershipModel.MembershipDetailModel result = service.searchDetailMembership(userId1,membershipId1);
        //then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("4. 멤버십 상세 조회- 정보가 없을 시")
    void 멤버십_상세조회_에러(){
        //given
        setData();
        //when
        Throwable customException = assertThrows(CustomException.class, () -> service.searchDetailMembership(userId1,membershipId2));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_USER_SIGN_MEMBERSHIP.getMessage());
    }

    @Test
    @DisplayName("5. 포인트 적립")
    void 멤버십_포인트_적립(){
        //given
        setData();
        UserParam param = new UserParam();
        param.setMembershipId(membershipId1);
        param.setMembershipName(membershipIdName1);
        param.setPoint(100L);
        //when
        Boolean result = service.pointAccumulation(userId1,param);
        //then
        assertThat(true).isEqualTo(result);
    }

    @Test
    @DisplayName("5. 포인트 적립 에러")
    void 멤버십_포인트_적립_에러(){
        //given
        setData();
        UserParam param = new UserParam();
        param.setMembershipId(membershipId3);
        param.setMembershipName(membershipIdName3);
        param.setPoint(100L);
        //when
        Throwable customException = assertThrows(CustomException.class, () -> service.pointAccumulation(userId1,param));
        //then
        assertThat(customException.getMessage()).isEqualTo(CustomTypeException.NO_USER_SIGN_MEMBERSHIP.getMessage());
    }


    private void setData(){
        UserEntity userEntity = UserEntity.builder().userId(userId1).build();
        MembershipEntity membershipEntity = MembershipEntity.builder().membershipId(membershipId1).build();
        userRepository.save(userEntity);
        MembershipDetailEntity membershipDetailEntity = MembershipDetailEntity.builder()
                .point(100000L)
                .membershipStatus("Y")
                .startDate(LocalDateTime.now())
                .userId(userEntity)
                .membershipId(membershipEntity)
                .build();
        membershipDetailRepository.save(membershipDetailEntity);
    }
}