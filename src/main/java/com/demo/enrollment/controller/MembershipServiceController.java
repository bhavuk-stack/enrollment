package com.demo.enrollment.controller;

import com.demo.enrollment.dto.MemberContext;
import com.demo.enrollment.util.ErrorType;
import com.demo.enrollment.model.MemberMaster;
import com.demo.enrollment.service.MembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.util.List;

/**
 * @author Bhavuk Soni
 */
@RestController
@RequestMapping("/api")
public class MembershipServiceController {

    public static final Logger logger = LoggerFactory.getLogger(MembershipServiceController.class);
    @Autowired
    private MembershipService service;

    /**
     * Method Consumes Member Creation and Update request
     * @param memberCtx
     * @return ResponseEntity
     * @throws ParseException
     */
    @PostMapping(value = "/member")
    public ResponseEntity<?> createOrUpdateMember(@RequestBody MemberContext memberCtx, UriComponentsBuilder ucBuilder) throws ParseException {
        logger.info("Creating Member : {}", memberCtx);
        MemberMaster created = null;
        MemberMaster existingMasterRec = service.getEnrolledMember(memberCtx.getMemberMaster().getMemberId().getSubscriberId());
        if(existingMasterRec != null){
            if(existingMasterRec.equals(memberCtx.getMemberMaster())){
                //comparing unique attributes of existing Member Master
                created = service.createOrUpdateMember(memberCtx);
            }else{
                return new ResponseEntity(new ErrorType("Person Number Cannot be updated for " + memberCtx.getMemberMaster().getMemberId().getSubscriberId()),
                        HttpStatus.NOT_FOUND);
            }
        }else if(memberCtx.getMemberMaster().getPersonNumber()!=1){
            return new ResponseEntity(new ErrorType("Subscriber Cannot be Created for Person number: " + memberCtx.getMemberMaster().getPersonNumber()),
                    HttpStatus.NOT_FOUND);
        }else {
            created = service.createOrUpdateMember(memberCtx);
        }
        if(created == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to update. Member with  " + memberCtx.getMemberMaster().getMemberId().getSubscriberId() + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/member/{id}").buildAndExpand(memberCtx.getMemberMaster().getMemberId().getSubscriberId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /**
     * Method takes a dependent addition request to existing Member
     * @param id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/enrollDependent/member/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MemberMaster> addMemberDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        logger.info("Calling addMemberDependent id: ", id);
        MemberMaster master = service.addMemberDependent(memberContext,id);
        if(master == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to add dependent. Member with  " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(master,new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method updates an existing dependent
     * @param id
     * @param memberContext
     * @return ResponseEntity
     */
    @RequestMapping(value = "/updateDependent/member/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MemberMaster> updateDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        logger.info("Calling updateDependent id: ", id);
        MemberMaster master = service.updateDependent(memberContext,id);
        if(master == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to update dependent. Member with  " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(master,new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method takes delete enrollment request
     * @param id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/cancelEnrollment/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEnrollment(@PathVariable("id") String id) {
        logger.info("Calling deleteEnrollment id: ", id);
        service.cancelEnrollment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method cancels the enrollment of a particular member
     * @param id
     * @param memberContext
     * @return ResponseEntity
     */
    @RequestMapping(value = "/disEnrollDependent/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> disEnrollDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        logger.info("Calling disEnrollDependent id: ", id);
        service.disEnrollDependent(memberContext,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves all members
     * @return ResponseEntity
     */
    @RequestMapping(value = "/member/", method = RequestMethod.GET)
    public ResponseEntity<List<MemberMaster>> getAllMembers() {
        logger.info("Calling getAllMembers id");
        List<MemberMaster> memberMasterList = service.getAllEnrolled();
        if (memberMasterList.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<MemberMaster>>(memberMasterList, HttpStatus.OK);
    }

    /**
     * Retrieves member by Id
     * @param id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMemberById(@PathVariable("id") String id) {
        logger.info("Calling getMemberById id: ", id);
        MemberMaster memberMaster = service.getEnrolledMember(id);
        if (memberMaster == null) {
            logger.error("Member with id {} not found.", id);
            return new ResponseEntity(new ErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<MemberMaster>(memberMaster, HttpStatus.OK);
    }
}
