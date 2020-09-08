package com.nttdata.hcls.enrollment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.hcls.enrollment.dto.MemberContext;
import com.nttdata.hcls.enrollment.model.MemberAddress;
import com.nttdata.hcls.enrollment.model.MemberId;
import com.nttdata.hcls.enrollment.model.MemberMaster;

import com.nttdata.hcls.enrollment.service.MembershipService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.core.Processor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


import static org.mockito.Matchers.any;

import org.junit.Before;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MembershipServiceControllerTest {

    private MockMvc mvc;
    @Mock
    private MembershipService service;
    @Autowired
    private WebApplicationContext wac;
    @InjectMocks
    private MembershipServiceController membershipServiceController;


    @Before
    public void setUp() throws Exception {

        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Processor processor = Mockito.mock(Processor.class);
        ReflectionTestUtils.setField(MembershipServiceController.class, "processor", processor);
    }

    @Test
    @DisplayName("createOrUpdateMember Test")
    void testCreateOrUpdateMember() throws Exception {
        MemberContext ctx = this.createMemberMaster();
        when(service.createOrUpdateMember(any(MemberContext.class))).thenReturn(ctx.getMemberMaster());
        mvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(ctx)))
                .andExpect(header().string("Location", "/api/member"));

    }

    private MemberContext createMemberMaster(){
        MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-4422238");
        member.setMemberId(id);
        member.setLastName("Doe");
        member.setFirstName("Jhon");
        member.setPersonNumber(01);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");
        try {
            member.setDateOfBirth(formatter.parse("12-04-1962"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("123-456-7890");
        Set<MemberAddress> memberAddressSet = new HashSet<>();
        memberAddressSet.add(address);
        member.setMemberAddressSet(memberAddressSet);
        MemberContext ctx = new MemberContext();
        ctx.setMemberMaster(member);
        ctx.setMemberAddress(address);
        return ctx;
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
