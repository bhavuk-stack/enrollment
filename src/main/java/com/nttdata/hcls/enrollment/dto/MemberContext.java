package com.nttdata.hcls.enrollment.dto;

import com.nttdata.hcls.enrollment.model.MemberAddress;
import com.nttdata.hcls.enrollment.model.MemberEligibilityHistory;
import com.nttdata.hcls.enrollment.model.MemberMaster;

public class MemberContext {
    private MemberMaster memberMaster;
    private MemberAddress memberAddress;
    private MemberEligibilityHistory memberEligibilityHistory;

    public MemberContext() {
    }

    public MemberContext(MemberMaster memberMaster, MemberAddress memberAddress, MemberEligibilityHistory memberEligibilityHistory) {
        this.memberMaster = memberMaster;
        this.memberAddress = memberAddress;
        this.memberEligibilityHistory = memberEligibilityHistory;
    }

    public MemberMaster getMemberMaster() {
        return memberMaster;
    }

    public MemberAddress getMemberAddress() {
        return memberAddress;
    }

    public void setMemberMaster(MemberMaster memberMaster) {
        this.memberMaster = memberMaster;
    }

    public void setMemberAddress(MemberAddress memberAddress) {
        this.memberAddress = memberAddress;
    }

    public MemberEligibilityHistory getMemberEligibilityHistory() {
        return memberEligibilityHistory;
    }

    public void setMemberEligibilityHistory(MemberEligibilityHistory memberEligibilityHistory) {
        this.memberEligibilityHistory = memberEligibilityHistory;
    }
}
