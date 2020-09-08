package com.demo.enrollment.service;

import com.demo.enrollment.dto.MemberContext;
import com.demo.enrollment.model.MemberMaster;

import java.text.ParseException;
import java.util.List;

/**
 * @author Bhavuk Soni
 *
 * Membership service API
 */
public interface MembershipService {

    MemberMaster createOrUpdateMember(MemberContext member) throws ParseException;
    MemberMaster addMemberDependent(MemberContext memberContext, String subscriberId);
    MemberMaster updateDependent(MemberContext memberContext, String subscriberId);
    void cancelEnrollment(String subscriberId);
    void disEnrollDependent(MemberContext memberContext, String subscriberId);
    MemberMaster getEnrolledMember(String subscriberId);
    List<MemberMaster> getAllEnrolled();
}
