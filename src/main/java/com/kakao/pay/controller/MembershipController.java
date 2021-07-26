package com.kakao.pay.controller;

import com.kakao.pay.model.MembershipModel;
import com.kakao.pay.model.UserParam;
import com.kakao.pay.response.Response;
import com.kakao.pay.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Boolean.valueOf;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/membership")
public class MembershipController {
    private static final String X_USER_ID = "X-USER-ID";

    private final MembershipService membershipService;

    //1. 멤버십 조회하기
    @GetMapping
    public ResponseEntity SearchAllMembership(@RequestHeader(X_USER_ID) String id){
        List resultList = membershipService.SearchAllMembership(id);
        return ResponseEntity.ok().body(new Response(resultList));
    }

    //2. 멤버십 등록하기
    @PostMapping
    public ResponseEntity insertMembership(@RequestHeader(X_USER_ID) String id, @RequestBody UserParam param){
        List resultList = membershipService.insertMembership(id,param);
        return ResponseEntity.ok().body(new Response(resultList));
    }

    //3. 멤버십 삭제(비활성화)하기
    @DeleteMapping("{membershipId}")
    public ResponseEntity deleteMembership(@RequestHeader(X_USER_ID) String id, @PathVariable(name = "membershipId") String membershipId){
        Boolean result = membershipService.deleteMembership(id,membershipId);
        return ResponseEntity.ok().body(new Response(valueOf(result)));
    }

    //4. 멤버십 상세 상세조회하기
    @GetMapping("{membershipId}")
    public ResponseEntity searchDetailMembership(@RequestHeader(X_USER_ID) String id, @PathVariable(name = "membershipId") String membershipId){
        MembershipModel.MembershipDetailModel result = membershipService.searchDetailMembership(id,membershipId);
        return ResponseEntity.ok().body(new Response(result));
    }

    //5. 멤버십 포인트 적립
    @PutMapping("/point")
    public ResponseEntity pointAccumulation(@RequestHeader(X_USER_ID) String id, @RequestBody UserParam param){
        Boolean result = membershipService.pointAccumulation(id,param);
        return ResponseEntity.ok().body(new Response(valueOf(result)));
    }
}
