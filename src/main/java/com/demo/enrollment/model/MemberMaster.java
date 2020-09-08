package com.demo.enrollment.model;

import com.demo.enrollment.util.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "MEMBER_MASTER")
@NamedQuery(name = "MemberMaster.findAllBySubscriberId",query = "SELECT m FROM MemberMaster m WHERE m.memberId.subscriberId = ?1 ")
@NamedQuery(name = "MemberMaster.findPrimarySubscriber", query = "SELECT a FROM MemberMaster a WHERE a.memberId.subscriberId = ?1 AND a.personNumber = ?2")
@NamedQuery(name = "MemberMaster.findByMemberIdSubscriberId", query = "SELECT a FROM MemberMaster a WHERE a.memberId.subscriberId = ?1")
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
    @NotBlank
    @Column(name= "ELIG_STAT")
    private String eligibilityStatus;
    @NotBlank
    @Column(name = "HOME_PHONE_NUMBER")
    private String homePhoneNumber;

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
    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberMaster that = (MemberMaster) o;
        return personNumber == that.personNumber &&
                memberId.getSubscriberId().equals(that.memberId.getSubscriberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId.getSubscriberId(), personNumber);
    }
}
