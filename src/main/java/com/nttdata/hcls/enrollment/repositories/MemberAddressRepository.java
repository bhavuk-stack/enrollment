package com.nttdata.hcls.enrollment.repositories;

import com.nttdata.hcls.enrollment.model.MemberAddress;
import com.nttdata.hcls.enrollment.model.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, MemberId> {
    List<MemberAddress> findAllBySubscriberId(String subscriberId);
}
