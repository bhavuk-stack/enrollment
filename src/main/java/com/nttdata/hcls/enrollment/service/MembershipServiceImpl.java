package com.nttdata.hcls.enrollment.service;

import com.nttdata.hcls.enrollment.model.MemberAddress;
import com.nttdata.hcls.enrollment.model.MemberId;
import com.nttdata.hcls.enrollment.model.MemberMaster;
import com.nttdata.hcls.enrollment.dto.MemberContext;
import com.nttdata.hcls.enrollment.repositories.MemberAddressRepository;
import com.nttdata.hcls.enrollment.repositories.MemberEnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("MembershipService")
@Transactional
public class MembershipServiceImpl implements MembershipService{
    public static final Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);
    @Autowired
    private MemberEnrollmentRepository repository;
    @Autowired
    private MemberAddressRepository addressRepository;

    /**
     * Method creates and updates the MemberMaster and child objects
     * @param member
     * @return
     * @throws ParseException
     */
    @Override
    public MemberMaster createOrUpdateMember(MemberContext member) throws ParseException {
        MemberAddress address = member.getMemberAddress();
        MemberMaster memberMaster = member.getMemberMaster();
        MemberMaster existingMemberMaster = null;
        if(memberMaster.getMemberId().getSubscriberId() != null)
        {
            logger.info("Calling into member enrollment repository");
            existingMemberMaster = repository.findByMemberIdSubscriberId(member.getMemberMaster().getMemberId().getSubscriberId());
            if(existingMemberMaster != null) {
                Optional<MemberMaster> newEntity = repository.findById(existingMemberMaster.getMemberId());
                if (newEntity.isPresent()) {
                    MemberMaster updateMaster = newEntity.get();
                    updateMaster.setFirstName(memberMaster.getFirstName());
                    updateMaster.setLastName(memberMaster.getLastName());
                    updateMaster.setDateOfBirth(memberMaster.getDateOfBirth());
                    updateMaster.setEligStat(memberMaster.getEligStat());
                    repository.save(updateMaster);
                    MemberMaster updatedMaster = repository.findByMemberIdSubscriberId(updateMaster.getMemberId().getSubscriberId());
                    MemberAddress membAddress = new MemberAddress();
                    membAddress.setPhoneNumber(address.getPhoneNumber());
                    membAddress.setMemberMaster(updatedMaster);
                    //update address
                    addressRepository.save(membAddress);
                    return updatedMaster;

                }
            }else {
                MemberMaster master = new MemberMaster();
                master.setMemberId(memberMaster.getMemberId());
                master.setPersonNumber(memberMaster.getPersonNumber());
                master.setFirstName(memberMaster.getFirstName());
                master.setLastName(memberMaster.getLastName());
                master.setDateOfBirth(memberMaster.getDateOfBirth());
                master.setEligStat(memberMaster.getEligStat());
                repository.save(master);
                MemberMaster master1 = repository.findByMemberIdSubscriberId(master.getMemberId().getSubscriberId());

                MemberAddress memberAddress = new MemberAddress();
                memberAddress.setPhoneNumber(address.getPhoneNumber());
                memberAddress.setMemberMaster(master1);
                addressRepository.save(memberAddress);
                repository.save(master);

                MemberMaster master2 = repository.findByMemberIdSubscriberId(master.getMemberId().getSubscriberId());
                return master2;
            }
        }
        return existingMemberMaster;
    }

    /**
     * Method Adds a dependent to an existing Member
     * @param memberContext
     * @param subscriberId
     * @return
     */
    @Override
    public MemberMaster addMemberDependent(MemberContext memberContext, String subscriberId) {
        if(memberContext.getMemberMaster().getPersonNumber() == 1){
            return null;
        }
        //personNumber 1 will always be the primary subscriber
        MemberMaster memberMaster = repository.findPrimarySubscriber(subscriberId, 1);
        if(memberMaster!=null) {
                    MemberMaster dep = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
                    if(dep == null) {
                        MemberMaster dependentRecord = new MemberMaster();
                        MemberId pk = new MemberId();
                        pk.setSubscriberId(memberMaster.getMemberId().getSubscriberId());
                        dependentRecord.setMemberId(pk);
                        dependentRecord.setFirstName(memberContext.getMemberMaster().getFirstName());
                        dependentRecord.setLastName(memberContext.getMemberMaster().getLastName());
                        dependentRecord.setPersonNumber(memberContext.getMemberMaster().getPersonNumber());
                        dependentRecord.setDateOfBirth(memberContext.getMemberMaster().getDateOfBirth());
                        dependentRecord.setEligStat(memberContext.getMemberMaster().getEligStat());
                        MemberMaster depRecord = repository.save(dependentRecord);
                        MemberMaster dependentCreated = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
                        MemberAddress dependentAddress = new MemberAddress();
                        dependentAddress.setPhoneNumber(memberContext.getMemberAddress().getPhoneNumber());
                        dependentAddress.setMemberMaster(dependentCreated);
                        addressRepository.save(dependentAddress);
                        //create Member elig history
                        return dependentRecord;
                    }
        }
        return memberMaster;
    }

    /**
     * Updates an existing dependent record
     * @param memberContext
     * @param subscriberId
     * @return
     */
    @Override
    public MemberMaster updateDependent(MemberContext memberContext, String subscriberId) {
        if(memberContext.getMemberMaster().getPersonNumber() == 1){
            return null;
        }
        //Check whether the Primary Subscriber exists
        //personNumber 1 will always be the primary subscriber
        MemberMaster memberMaster = repository.findPrimarySubscriber(subscriberId, 1);
        if(memberMaster!=null) {
            MemberMaster existingDependent = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
            if(existingDependent != null) {
                //dependent exists
                MemberMaster dependentRecord = new MemberMaster();
                dependentRecord.setMemberId(existingDependent.getMemberId());
                dependentRecord.setFirstName(memberContext.getMemberMaster().getFirstName());
                dependentRecord.setLastName(memberContext.getMemberMaster().getLastName());
                dependentRecord.setPersonNumber(memberContext.getMemberMaster().getPersonNumber());
                dependentRecord.setDateOfBirth(memberContext.getMemberMaster().getDateOfBirth());
                dependentRecord.setEligStat(memberContext.getMemberMaster().getEligStat());
                Set<MemberAddress> addressSet = new HashSet<>();
                MemberAddress address = memberContext.getMemberAddress();
                address.setMemberMaster(dependentRecord);
                addressSet.add(address);
                dependentRecord.setMemberAddressSet(addressSet);// end
                repository.save(dependentRecord);
                return dependentRecord;
            }

        }
        return null;
    }

    /**
     * Cancels/Deletes the existing Subscriber and dependents from the system
     * @param subscriberId
     */
    @Override
    public void cancelEnrollment(String subscriberId) {
        List<MemberMaster> memberMastersList = repository.findAllBySubscriberId(subscriberId);
        //delete Member Master
        for (MemberMaster memberMaster: memberMastersList
             ) {
            Set<MemberAddress> addressSet = memberMaster.getMemberAddressSet();
            addressSet.removeAll(addressSet);
        }
        repository.saveAll(memberMastersList);
        repository.deleteAll(repository.findAllBySubscriberId(subscriberId));
    }

    /**
     * Cancels dependent enrollment associated with the Primary Subscriber.
     * @param memberContext
     * @param subscriberId
     */
    @Override
    public void disEnrollDependent(MemberContext memberContext, String subscriberId) {
        if(memberContext.getMemberMaster().getPersonNumber()!=1) {
            MemberMaster memberMaster = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
            Optional<MemberMaster> savedMasterRecord = repository.findById(memberMaster.getMemberId());
            MemberMaster savedMember = savedMasterRecord.get();
            Set<MemberAddress> memberSet = savedMember.getMemberAddressSet();
            MemberAddress savedAdd = null;
            for (MemberAddress addr:memberSet
            ) {
                savedAdd = addr;
            }
            if(savedAdd!=null) {
                savedMember.getMemberAddressSet().remove(savedAdd);
            }
            repository.save(savedMember);
        }

    }

    /**
     * Gets an enrolled Member
     * @param subscriberId
     * @return
     */
    @Override
    public MemberMaster getEnrolledMember(String subscriberId) {
       return repository.findByMemberIdSubscriberId(subscriberId);

    }

    /**
     * Get all enrolled
     * @return
     */
    @Override
    public List<MemberMaster> getAllEnrolled() {
        return repository.findAll();
    }


}
