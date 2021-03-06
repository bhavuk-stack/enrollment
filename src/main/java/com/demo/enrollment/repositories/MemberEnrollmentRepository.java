package com.demo.enrollment.repositories;

import com.demo.enrollment.model.MemberId;
import com.demo.enrollment.model.MemberMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberEnrollmentRepository
        extends JpaRepository<MemberMaster, MemberId> {
    MemberMaster findByMemberIdSubscriberId(String subscriberId);
    MemberMaster findPrimarySubscriber(String subscriberId, long personNumber);
    List<MemberMaster> findAllBySubscriberId(String subscriberId);
}
