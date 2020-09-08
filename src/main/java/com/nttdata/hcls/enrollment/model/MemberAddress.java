package com.nttdata.hcls.enrollment.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MEMBER_ADDRESS")
@NamedQuery(name = "MemberAddress.findAllBySubscriberId", query = "FROM MemberAddress WHERE memberMaster.memberId.subscriberId = ?1")
public class MemberAddress implements Serializable {
    @Id
    @Column(name = "SEQ_MEMB_ADDRESS")
    private long seqMembAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEQ_MEMB_ID", referencedColumnName = "SEQ_MEMB_ID")
    @JoinColumn(name = "SUBSCRIBER_ID", referencedColumnName = "SUBSCRIBER_ID")
    private MemberMaster memberMaster;

    @NotBlank
    @Column(name = "HOME_PHONE_NUMBER")
    private String phoneNumber;

    public long getSeqMembAddress() {
        return seqMembAddress;
    }

  /*  public long getSeqMembId() {
        return seqMembId;
    }*/

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /*public void setSeqMembId(long seqMembId) {
        this.seqMembId = seqMembId;
    }*/

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    @Override
    public String toString() {
        return "MemberAddress{" +
                "seqMembAddress=" + seqMembAddress +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAddress that = (MemberAddress) o;
        return seqMembAddress == that.seqMembAddress &&
                phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seqMembAddress, phoneNumber);
    }

    public MemberMaster getMemberMaster() {
        return memberMaster;
    }

    public void setMemberMaster(MemberMaster memberMaster) {
        this.memberMaster = memberMaster;
    }

    /*public String getSubscriberId() {
        return subscriberId;
    }*/

    /*public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }*/

    /*public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }

    public MemberId getMemberId() {
        return memberId;
    }*/
}
