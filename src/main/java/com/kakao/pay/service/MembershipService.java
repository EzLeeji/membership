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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final UserRepository userRepository;
    private final MembershipDetailRepository membershipDetailRepository;
    private final MembershipRepository membershipRepository;

    @PersistenceContext
    private EntityManager em;

    //1. 멤버십 조회하기
    public List SearchAllMembership(String userId){
        UserEntity userInfo = getUserInfo(userId);
        List resultList= MembershipModel.setEntity(userInfo).getMembershipDetailModels();
        return resultList;
    }

    //2. 멤버십 등록하기
    public List insertMembership(String userId, UserParam param){

        //멤버십 3개 정보 조회
        getMembershipInfo(param.getMembershipId(),param.getMembershipName());

        UserEntity user = UserEntity.builder().userId(userId).build();
        //유저가 없으면 삽입
        UserEntity userInfo = userRepository.findById(userId)
                .orElseGet(()->userRepository.save(user));

        MembershipDetailEntity mDetailEntity = MembershipDetailEntity.setParam(userId,param);

        Optional<MembershipDetailEntity> memDetailInfo = membershipDetailRepository.
                findByUserIdAndMembershipId(mDetailEntity.getUserId(),mDetailEntity.getMembershipId());

        memDetailInfo.ifPresent(membership -> { //가입 정보가 있지만
            if("Y".equals(membership.getMembershipStatus())){ //이미 가입된 사용자라면 에러
                throw new CustomException(CustomTypeException.ALREADY_USER_SIGN_MEMBERSHIP);
            }
            else{ // 가입 -> 해지 -> 가입   : 상태만 update
                //새로운 포인트는 직원이 적립 api로 적립해준다.ㅐ
                membership.setMembershipStatus("Y");
                membershipDetailRepository.saveAndFlush(membership);
            }
        });
        //가입 정보가 없다면
        memDetailInfo.orElseGet(()->{
            return membershipDetailRepository.saveAndFlush(mDetailEntity);
        });


        em.clear();
        em.close();
        List resultList = SearchAllMembership(userId);
        return resultList;
    }

    //3. 멤버십 삭제(비활성화)하기
    public boolean deleteMembership(String userId, String membershipId){
        UserEntity _userId = getUserInfo(userId);
        MembershipEntity _membershipId = getMembershipInfo(membershipId);

        Optional<MembershipDetailEntity> memDetailInfo= membershipDetailRepository.findByUserIdAndMembershipId(_userId, _membershipId);
        memDetailInfo.ifPresent(memDetail-> {
            if("N".equals( memDetail.getMembershipStatus() ) ){
                throw new CustomException(CustomTypeException.ALREADY_USER_DELETE_MEMBERSHIP);
            }
            memDetail.setMembershipStatus("N");
            membershipDetailRepository.save(memDetail);
        });
        memDetailInfo.orElseGet(()->{
            throw new CustomException(CustomTypeException.ALREADY_USER_DELETE_MEMBERSHIP);
        });
        return true;
    }

    //4. 멤버십 상세 조회하기
    public MembershipModel.MembershipDetailModel searchDetailMembership(String userId, String membershipId){

        UserEntity _userId = getUserInfo(userId);
        MembershipEntity _membershipId = getMembershipInfo(membershipId);

        MembershipDetailEntity membershipDetailEntity = membershipDetailRepository.findByUserIdAndMembershipId(_userId,_membershipId)
                .orElseThrow(()-> new CustomException(CustomTypeException.NO_USER_SIGN_MEMBERSHIP));
        return MembershipModel.MembershipDetailModel.setEntity(membershipDetailEntity);
    }

    //5. 멤버십 POINT 적립
    public boolean pointAccumulation(String userId, UserParam param){
        UserEntity user = getUserInfo(userId);
        MembershipEntity _membershipId = getMembershipInfo(param.getMembershipId());

        MembershipDetailEntity membershipDetailEntity = membershipDetailRepository.findByUserIdAndMembershipId(user,_membershipId)
                .orElseThrow(()-> new CustomException(CustomTypeException.NO_USER_SIGN_MEMBERSHIP));

        if( "N".equals(membershipDetailEntity.getMembershipStatus() ) ){
            throw new CustomException(CustomTypeException.NO_USER_SIGN_MEMBERSHIP);
        }
        else{
            Long sumPoint = (long) Math.floor(param.getAmount() * 0.01);
            Long point = membershipDetailEntity.getPoint() + sumPoint;
            membershipDetailEntity.setPoint(point);
            membershipDetailRepository.save(membershipDetailEntity);
        }
        return true;
    }

    //유저 조회 - 없으면 err
    public UserEntity getUserInfo(String userId){
        UserEntity userInfo = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(CustomTypeException.NO_USER_SIGN_MEMBERSHIP));
        return userInfo;
    }

    //멤버십 정보 가져오기 - 없으면 err
    public MembershipEntity getMembershipInfo(String membershipId, String membershipName){
        MembershipEntity getMemberInfo = membershipRepository.findByMembershipIdAndMembershipName(membershipId,membershipName)
                .orElseThrow(()-> new CustomException(CustomTypeException.NO_MEMBERSHIP_COMPANY));
        return getMemberInfo;
    }

    //멤버십 정보 가져오기 - 없으면 err
    public MembershipEntity getMembershipInfo(String membershipId){
        MembershipEntity getMemberInfo = membershipRepository.findById(membershipId)
                .orElseThrow(()-> new CustomException(CustomTypeException.NO_MEMBERSHIP_COMPANY));
        return getMemberInfo;
    }
}
