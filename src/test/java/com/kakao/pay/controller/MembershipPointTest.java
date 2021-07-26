package com.kakao.pay.controller;

import com.kakao.pay.entity.MembershipDetailEntity;
import com.kakao.pay.entity.MembershipEntity;
import com.kakao.pay.entity.UserEntity;
import com.kakao.pay.repository.MembershipDetailRepository;
import com.kakao.pay.repository.MembershipRepository;
import com.kakao.pay.repository.UserRepository;
import com.kakao.pay.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MembershipPointTest {
    private static final String X_USER_ID = "X-USER-ID";
    private static final String MEMBERSHIP_ID1 = "shinsegae";
    private static final String MEMBERSHIP_ID2 = "spc";
    private static final String MEMBERSHIP_ID3 = "cj";
    private static final String MEMBERSHIP_NAME1 = "shinsegaepoint";
    private static final String MEMBERSHIP_NAME2 = "happypoint";
    private static final String MEMBERSHIP_NAME3 = "cjone";
    private static final String MEMBERSHIP_STATUS = "Y";
    private static final Long POINT = 5000L;

    private static final String USER_ID = "test1";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MembershipDetailRepository membershipDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @InjectMocks
    private MembershipService mbsService;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {

        UserEntity userEntity = UserEntity.builder().userId(USER_ID).build();
        MembershipEntity membershipEntity = MembershipEntity.builder().membershipId(MEMBERSHIP_ID1).build();
        userRepository.saveAndFlush(userEntity.builder().userId(USER_ID).build());

        entityManager.clear();
        entityManager.close();

        MembershipDetailEntity membershipDetailEntity = MembershipDetailEntity.builder()
                .membershipId(membershipEntity)
                .userId(userEntity)
                .point(POINT)
                .membershipStatus("Y")
                .build();
        membershipDetailRepository.saveAndFlush(membershipDetailEntity);

        MembershipEntity membershipEntity2 = MembershipEntity.builder().membershipId(MEMBERSHIP_ID2).build();
        MembershipDetailEntity membershipDetailEntity2 = MembershipDetailEntity.builder()
                .membershipId(membershipEntity2)
                .userId(userEntity)
                .point(POINT)
                .membershipStatus("N")
                .build();
        membershipDetailRepository.saveAndFlush(membershipDetailEntity2);
    }

    @Test
    @DisplayName("포인트 적립")
    void 포인트적립() throws Exception {
        // given

        // when
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/membership/point")
                        .header(X_USER_ID, USER_ID)
                        .header("Content-Type", "application/json")
                        .content("{\"membershipId\":\"shinsegae\",\"amount\":5000}"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("포인트 적립 삭제할 멤버십이 없을 때")
    void 포인트_적립_삭제할_멤버십이_없을_때() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/membership/point")
                        .header(X_USER_ID, USER_ID)
                        .header("Content-Type", "application/json")
                        .content("{\"membershipId\":\"cj\",\"amount\":5000}"))
                        .andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(jsonPath("$.success").value(false))
                        .andExpect(jsonPath("$.response").isEmpty())
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.error.status").value(-1001));
    }


    @Test
    @DisplayName("포인트 적립 해지된 사용자")
    void 포인트_적립_해지된_사용자() throws Exception {
        // given

        // when
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/membership/point")
                        .header(X_USER_ID, USER_ID)
                        .header("Content-Type", "application/json")
                        .content("{\"membershipId\":\"spc\",\"amount\":5000}"))
                        .andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(jsonPath("$.success").value(false))
                        .andExpect(jsonPath("$.response").isEmpty())
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.error.status").value(-1001));
    }
}
