package com.demo.enrollment.service;

import com.demo.enrollment.dto.MemberContext;
import com.demo.enrollment.model.MemberId;
import com.demo.enrollment.model.MemberMaster;
import com.demo.enrollment.repositories.MemberEnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author Bhavuk Soni
 * Implementation class for Membership Service
 */
@Service("MembershipService")
@Transactional
public class MembershipServiceImpl implements MembershipService{
    public static final Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);
    @Autowired
    private MemberEnrollmentRepository repository;

    /**
     * Method creates and updates the MemberMaster and child objects
     * @param memberCtx
     * @return
     * @throws ParseException
     */
    @Override
    public MemberMaster createOrUpdateMember(MemberContext memberCtx) throws ParseException {
        MemberMaster memberMaster = memberCtx.getMemberMaster();
        MemberMaster existingMemberMaster = null;
        if(memberMaster.getMemberId().getSubscriberId() != null)
        {
            logger.info("Calling into member enrollment repository");
            existingMemberMaster = repository.findByMemberIdSubscriberId(memberCtx.getMemberMaster().getMemberId().getSubscriberId());
            if(existingMemberMaster != null) {
                Optional<MemberMaster> newEntity = repository.findById(existingMemberMaster.getMemberId());
                if (newEntity.isPresent()) {
                    MemberMaster updateMaster = newEntity.get();
                    updateMaster.setFirstName(memberMaster.getFirstName());
                    updateMaster.setLastName(memberMaster.getLastName());
                    updateMaster.setDateOfBirth(memberMaster.getDateOfBirth());
                    updateMaster.setEligibilityStatus(memberMaster.getEligibilityStatus());
                    updateMaster.setHomePhoneNumber(memberMaster.getHomePhoneNumber());
                    repository.save(updateMaster);
                    MemberMaster updatedMaster = repository.findByMemberIdSubscriberId(updateMaster.getMemberId().getSubscriberId());
                    return updatedMaster;
                }
            }else {
                    MemberMaster master = createMemberMaster(memberCtx);
                    master.setMemberId(memberCtx.getMemberMaster().getMemberId());
                    return repository.saveAndFlush(master);
                /*MemberMaster master1 = repository.findByMemberIdSubscriberId(memberCtx.getMemberId().getSubscriberId());
                return repository.save(master);*/
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
        logger.info("Calling addMemberDependent");
        if(memberContext.getMemberMaster().getPersonNumber() == 1){
            return null;
        }
        //personNumber 1 will always be the primary subscriber
        MemberMaster memberMaster = repository.findPrimarySubscriber(subscriberId, 1);
        if(memberMaster!=null) {
                    MemberMaster dep = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
                    if(dep == null) {
                        MemberMaster dependentRecord = createMemberMaster(memberContext);
                        MemberId pk = new MemberId();
                        pk.setSubscriberId(memberMaster.getMemberId().getSubscriberId());
                        dependentRecord.setMemberId(pk);
                        MemberMaster depRecord = repository.save(dependentRecord);
                        MemberMaster dependentCreated = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
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
        logger.info("Calling updateDependent");
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
                MemberMaster dependentRecord = createMemberMaster(memberContext);
                dependentRecord.setMemberId(existingDependent.getMemberId());
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
        logger.info("Calling cancelEnrollment");
        List<MemberMaster> memberMastersList = repository.findAllBySubscriberId(subscriberId);
        repository.deleteAll(memberMastersList);
    }

    /**
     * Cancels dependent enrollment associated with the Primary Subscriber.
     * @param memberContext
     * @param subscriberId
     */
    @Override
    public void disEnrollDependent(MemberContext memberContext, String subscriberId) {
        logger.info("Calling disEnrollDependent");
        if(memberContext.getMemberMaster().getPersonNumber()!=1) {
            MemberMaster memberMaster = repository.findPrimarySubscriber(subscriberId, memberContext.getMemberMaster().getPersonNumber());
            repository.delete(memberMaster);
        }

    }

    /**
     * Gets an enrolled Member
     * @param subscriberId
     * @return
     */
    @Override
    public MemberMaster getEnrolledMember(String subscriberId) {
        logger.info("Calling getEnrolledMember");
       return repository.findByMemberIdSubscriberId(subscriberId);

    }

    /**
     * Get all enrolled
     * @return
     */
    @Override
    public List<MemberMaster> getAllEnrolled() {
        logger.info("Calling getAllEnrolled");
        return repository.findAll();
    }

    /**
     *
     * @param memberContext
     * @return
     */
    private MemberMaster createMemberMaster(MemberContext memberContext) {
        MemberMaster master = new MemberMaster();
        master.setPersonNumber(memberContext.getMemberMaster().getPersonNumber());
        master.setFirstName(memberContext.getMemberMaster().getFirstName());
        master.setLastName(memberContext.getMemberMaster().getLastName());
        master.setDateOfBirth(memberContext.getMemberMaster().getDateOfBirth());
        master.setEligibilityStatus(memberContext.getMemberMaster().getEligibilityStatus());
        master.setHomePhoneNumber(memberContext.getMemberMaster().getHomePhoneNumber());
        return master;
    }

}
