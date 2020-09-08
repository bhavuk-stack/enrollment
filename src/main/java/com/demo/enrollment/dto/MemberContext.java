package com.demo.enrollment.dto;

import com.demo.enrollment.model.MemberMaster;

public class MemberContext {
    private MemberMaster memberMaster;

    public MemberContext() {
    }

    public MemberContext(MemberMaster memberMaster) {
        this.memberMaster = memberMaster;
    }
    public MemberMaster getMemberMaster() {
        return memberMaster;
    }
    public void setMemberMaster(MemberMaster memberMaster) {
        this.memberMaster = memberMaster;
    }



}
