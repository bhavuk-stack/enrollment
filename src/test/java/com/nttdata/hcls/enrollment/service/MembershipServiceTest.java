package com.nttdata.hcls.enrollment.service;

import com.nttdata.hcls.enrollment.dto.MemberContext;
import com.nttdata.hcls.enrollment.model.MemberAddress;
import com.nttdata.hcls.enrollment.model.MemberId;
import com.nttdata.hcls.enrollment.model.MemberMaster;
import com.nttdata.hcls.enrollment.repositories.MemberAddressRepository;
import com.nttdata.hcls.enrollment.repositories.MemberEnrollmentRepository;
import com.nttdata.hcls.enrollment.service.MembershipService;
import com.nttdata.hcls.enrollment.service.MembershipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MembershipServiceTest {
    @Mock
    private MemberEnrollmentRepository repository;
    @Mock
    private MemberAddressRepository addressRepository;

    @InjectMocks
    private MembershipService membershipService = new MembershipServiceImpl();

    @BeforeEach
    void setMockOutput() throws ParseException {
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
        when(repository.findByMemberIdSubscriberId(member.getMemberId().getSubscriberId())).thenReturn(member);
        when(repository.findPrimarySubscriber(member.getMemberId().getSubscriberId(),01)).thenReturn(member);
        when(repository.findPrimarySubscriber(member.getMemberId().getSubscriberId(),member.getPersonNumber())).thenReturn(member);


    }
    @DisplayName("Test Create and Update Member")
    @Test
    void testCreateOrUpdateMember() throws ParseException {
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

        assertEquals("HP-4422238",membershipService.createOrUpdateMember(ctx).getMemberId().getSubscriberId());

    }
    @DisplayName("Test Member dependent addition")
    @Test
    void testAddMemberDependent() {
        MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-4422238");
        member.setMemberId(id);
        member.setLastName("Doe");
        member.setFirstName("Cary");
        member.setPersonNumber(02);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");
        try {
            member.setDateOfBirth(formatter.parse("12-04-1969"));
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
        when(repository.save(member)).thenReturn(member);
        when(addressRepository.save(address)).thenReturn(address);
        assertEquals(member.getPersonNumber(),membershipService.addMemberDependent(ctx,member.getMemberId().getSubscriberId()).getPersonNumber());
        assertEquals(member.getMemberId(),membershipService.addMemberDependent(ctx,member.getMemberId().getSubscriberId()).getMemberId());
    }
    @DisplayName("Test Member dependent update")
    @Test
    void testUpdateDependent() {
        MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-4422238");
        member.setMemberId(id);
        member.setLastName("Doe");
        member.setFirstName("Cary");
        member.setPersonNumber(02);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");
        try {
            member.setDateOfBirth(formatter.parse("12-04-1969"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("999-456-7890");
        Set<MemberAddress> memberAddressSet = new HashSet<>();
        memberAddressSet.add(address);
        member.setMemberAddressSet(memberAddressSet);

        MemberContext ctx = new MemberContext();
        ctx.setMemberMaster(member);
        ctx.setMemberAddress(address);
        when(repository.findPrimarySubscriber(member.getMemberId().getSubscriberId(), ctx.getMemberMaster().getPersonNumber())).thenReturn(member);
        when(repository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        MemberMaster m = membershipService.updateDependent(ctx,member.getMemberId().getSubscriberId());
        Set<MemberAddress> addressSet = m.getMemberAddressSet();
        for (MemberAddress ma: addressSet
             ) {
            assertEquals(address.getPhoneNumber(),ma.getPhoneNumber());
        }

    }
    @DisplayName("Test Member deletion")
    @Test
    void testCancelEnrollment() {
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
        List<MemberMaster> memberMasterList = new ArrayList<MemberMaster>();
        memberMasterList.add(member);
        when(repository.findAllBySubscriberId(member.getMemberId().getSubscriberId())).thenReturn(memberMasterList);
        membershipService.cancelEnrollment(member.getMemberId().getSubscriberId());
        assertTrue(true);
    }

    void testDisEnrollDependent(){
        MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-4422238");
        member.setMemberId(id);
        member.setLastName("Doe");
        member.setFirstName("Cary");
        member.setPersonNumber(02);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");
        try {
            member.setDateOfBirth(formatter.parse("12-04-1969"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("999-456-7890");
        Set<MemberAddress> memberAddressSet = new HashSet<>();
        memberAddressSet.add(address);
        member.setMemberAddressSet(memberAddressSet);

        MemberContext ctx = new MemberContext();
        ctx.setMemberMaster(member);
        ctx.setMemberAddress(address);
        when(repository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        membershipService.disEnrollDependent(ctx,member.getMemberId().getSubscriberId());
        assertTrue(true);
    }

}
