package com.nttdata.hcls.enrollment.service;

import com.nttdata.hcls.enrollment.model.MemberMaster;
import com.nttdata.hcls.enrollment.dto.MemberContext;

import java.text.ParseException;
import java.util.List;

public interface MembershipService {

    MemberMaster createOrUpdateMember(MemberContext member) throws ParseException;
    MemberMaster addMemberDependent(MemberContext memberContext, String subscriberId);
    MemberMaster updateDependent(MemberContext memberContext, String subscriberId);
    void cancelEnrollment(String subscriberId);
    void disEnrollDependent(MemberContext memberContext, String subscriberId);
    MemberMaster getEnrolledMember(String subscriberId);
    List<MemberMaster> getAllEnrolled();
}
