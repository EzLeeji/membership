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
public class MembershipDeleteTest {
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
        userRepository.saveAndFlush(userEntity);

        entityManager.clear();
        entityManager.close();

        MembershipDetailEntity membershipDetailEntity = MembershipDetailEntity.builder()
                .membershipId(membershipEntity)
                .userId(userEntity)
                .point(POINT)
                .membershipStatus("Y")
                .build();
        membershipDetailRepository.saveAndFlush(membershipDetailEntity);
    }

    @Test
    @DisplayName("멤버십 삭제")
    void 멤버십_삭제() throws Exception {
        //given

        //when-then
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/membership/" + MEMBERSHIP_ID1)
                        .header(X_USER_ID, USER_ID))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("멤버십 삭제 시 유저 없을 경우")
    void 멤버십_삭제_시_유저_없을_경우() throws Exception {
        // given

        // when-then
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/membership/" + MEMBERSHIP_ID3)
                        .header(X_USER_ID, USER_ID))
                        .andDo(print())
                        .andExpect(status().is4xxClientError())
                        .andExpect(jsonPath("$.success").value(false))
                        .andExpect(jsonPath("$.response").isEmpty())
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.error.status").value(-1003));
    }
}
