package com.nttdata.hcls.enrollment.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nttdata.hcls.enrollment.util.CustomDateDeserializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "MEMBER_MASTER")
@NamedQuery(name = "MemberMaster.findAllBySubscriberId",query = "FROM MemberMaster WHERE memberId.subscriberId = ?1 ")
@NamedQuery(name = "MemberMaster.findPrimarySubscriber", query = "FROM MemberMaster WHERE memberId.subscriberId = ?1 AND personNumber = ?2")
public class MemberMaster implements Serializable {
    @EmbeddedId
    private MemberId memberId;
    @NotNull
    @Column(name = "PERSON_NUMBER")
    private long personNumber;
    @NotBlank
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name="DATE_OF_BIRTH", nullable=false)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private java.util.Date dateOfBirth;
    private String eligStat;

   @OneToMany(fetch = FetchType.LAZY,mappedBy="memberMaster",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberAddress> memberAddressSet;
    public void setPersonNumber(long personNumber) {
        this.personNumber = personNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public java.util.Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(java.util.Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public MemberId getMemberId() {
        return memberId;
    }

    public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }

    public Set<MemberAddress> getMemberAddressSet() {
        return memberAddressSet;
    }

    public void setMemberAddressSet(Set<MemberAddress> memberAddressSet) {
        this.memberAddressSet = memberAddressSet;
    }

    public void setEligStat(String eligStat) {
        this.eligStat = eligStat;
    }

    public String getEligStat() {
        return eligStat;
    }
}
