package com.nttdata.hcls.enrollment;

import com.nttdata.hcls.enrollment.model.MemberMaster;
import com.nttdata.hcls.enrollment.repositories.MemberEnrollmentRepository;
import com.nttdata.hcls.enrollment.service.MembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

public class UserRepositoryTest implements CommandLineRunner {
    private static final Logger log =
            LoggerFactory.getLogger(UserRepositoryTest.class);
    @Autowired
    private MemberEnrollmentRepository memberEnrollmentRepository;
    @Autowired
    MembershipService service;
    @Override
    public void run(String... args) throws Exception {
        MemberMaster member = new MemberMaster();
        member.setFirstName("Manisha");
        member.setLastName("Soni");
       // member.setSubscriberId("HP44892");
        member.setPersonNumber(new Long(01));
        //member.setDateOfBirth(new Date());
      //  memberEnrollmentRepository.save(member);
        //service.createOrUpdateMember(member);
        log.info("New User is created : " + member);

        List<MemberMaster> users = memberEnrollmentRepository.findAll();
        log.info("All Users : " + users);

    }
}
