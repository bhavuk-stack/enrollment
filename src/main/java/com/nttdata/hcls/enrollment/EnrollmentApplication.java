package com.nttdata.hcls.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EnrollmentApplication{
    /*@Autowired
    private MemberEnrollmentRepository repository;
    @Autowired
    private MemberAddressRepository addressRepository;
    @Autowired
    private MembershipService membershipService;*/

   public static void main(String[] args) {
        SpringApplication.run(EnrollmentApplication.class, args);
    }

    /*@Override
    public void run(String... args) throws Exception {
       /* MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-455238");
        member.setMemberId(id);
        member.setLastName("Soni");
        member.setFirstName("Suresh");
        member.setPersonNumber(01);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");

        member.setDateOfBirth(formatter.parse("20-09-1952"));
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("682-246-0742");
        //address.setMemberMaster(member);
        //address.setSubscriberId(member.getMemberId().getSubscriberId());
        //address.setMemberId(id);
        Set<MemberAddress> addressSet = new HashSet<MemberAddress>();
        addressSet.add(address);

       member.setMemberAddressSet(addressSet);


        repository.save(member);
        MemberMaster master = repository.findByMemberIdSubscriberId(member.getMemberId().getSubscriberId());
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("682-246-0742");
        address.setMemberMaster(master);
        addressRepository.save(address);

        MemberMaster m = repository.findByMemberIdSubscriberId("HP-455234");
        System.out.println(m.getMemberId().getSeqMembId());
        MemberContext ctx = new MemberContext();
        MemberMaster member = new MemberMaster();
        MemberId id = new MemberId();
        id.setSubscriberId("HP-4556666");
        member.setMemberId(id);
        member.setLastName("Rampal");
        member.setFirstName("Amit");
        member.setPersonNumber(01);
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy");

        member.setDateOfBirth(formatter.parse("07-05-1987"));
        MemberAddress address = new MemberAddress();
        address.setPhoneNumber("682-246-0742");
        ctx.setMemberAddress(address);
        ctx.setMemberMaster(member);

        membershipService.createOrUpdateMember(ctx);


    }*/
}
