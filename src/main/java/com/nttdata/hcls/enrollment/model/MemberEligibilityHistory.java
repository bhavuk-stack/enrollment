package com.nttdata.hcls.enrollment.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MEMBER_ELIG_HISTORY")
public class MemberEligibilityHistory implements Serializable {
    @Id
    @Column(name = "SEQ_ELIG_HIST")
    private long seqEligHist;
    @ManyToOne()
    @JoinColumn(name = "SEQ_MEMB_ID", referencedColumnName = "SEQ_MEMB_ID")
    @JoinColumn(name = "SUBSCRIBER_ID", referencedColumnName = "SUBSCRIBER_ID")
    private MemberMaster memberMaster;
    @NotNull
    @Column(name = "PERSON_NUMBER")
    private long personNumber;
    @NotBlank
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @NotBlank
    @Column(name = "ELIG_STAT")
    private String eligStatus;


    public void setPersonNumber(long personNumber) {
        this.personNumber = personNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEligStatus(String eligStatus) {
        this.eligStatus = eligStatus;
    }

    public long getSeqEligHist() {
        return seqEligHist;
    }

    public long getPersonNumber() {
        return personNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEligStatus() {
        return eligStatus;
    }

    public MemberMaster getMemberMaster() {
        return memberMaster;
    }

    public void setMemberMaster(MemberMaster memberMaster) {
        this.memberMaster = memberMaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEligibilityHistory that = (MemberEligibilityHistory) o;
        return seqEligHist == that.seqEligHist &&
                personNumber == that.personNumber &&
                memberMaster.equals(that.memberMaster) &&
                lastName.equals(that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                eligStatus.equals(that.eligStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seqEligHist, memberMaster, personNumber, lastName, firstName, eligStatus);
    }
}
