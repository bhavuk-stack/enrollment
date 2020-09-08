package com.nttdata.hcls.enrollment.controller;

import com.nttdata.hcls.enrollment.model.MemberMaster;
import com.nttdata.hcls.enrollment.dto.MemberContext;
import com.nttdata.hcls.enrollment.service.MembershipService;
import com.nttdata.hcls.enrollment.util.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MembershipServiceController {

    public static final Logger logger = LoggerFactory.getLogger(MembershipServiceController.class);
    @Autowired
    private MembershipService service;

    /**
     * Method Consumes Member Creation and Update request
     * @param memberCtx
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/member")
    public ResponseEntity<MemberMaster> createOrUpdateMember(@RequestBody MemberContext memberCtx) throws ParseException {
        logger.info("Creating Member : {}", memberCtx);
        MemberMaster updated = service.createOrUpdateMember(memberCtx);
        if(updated == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to upate. Member with  " + memberCtx.getMemberMaster().getMemberId() + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<MemberMaster>(updated,new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method takes a dependent addition request to existing Member
     * @param id
     * @param memberContext
     * @return
     */
    @RequestMapping(value = "/enrollDependent/member/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MemberMaster> addMemberDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        MemberMaster master = service.addMemberDependent(memberContext,id);
        if(master == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to add dependent. Member with  " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<MemberMaster>(master,new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method updates an existing dependent
     * @param id
     * @param memberContext
     * @return
     */
    @RequestMapping(value = "/updateDependent/member/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MemberMaster> updateDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        MemberMaster master = service.updateDependent(memberContext,id);
        if(master == null){
            logger.error("Unable to update Member");
            return new ResponseEntity(new ErrorType("Unable to update dependent. Member with  " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<MemberMaster>(master,new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method takes delete enrollment request
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelEnrollment/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEnrollment(@PathVariable("id") String id) {
        service.cancelEnrollment(id);
        return new ResponseEntity<MemberMaster>(HttpStatus.OK);
    }

    /**
     * Method cancels the enrollment of a particular member
     * @param id
     * @param memberContext
     * @return
     */
    @RequestMapping(value = "/disEnrollDependent/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> disEnrollDependent(@PathVariable("id") String id, @RequestBody MemberContext memberContext) {
        service.disEnrollDependent(memberContext,id);
        return new ResponseEntity<MemberMaster>(HttpStatus.OK);
    }

    /**
     * Retrieves all members
     * @return
     */
    @RequestMapping(value = "/member/", method = RequestMethod.GET)
    public ResponseEntity<List<MemberMaster>> getAllMembers() {
        List<MemberMaster> memberMasterList = service.getAllEnrolled();
        if (memberMasterList.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<MemberMaster>>(memberMasterList, HttpStatus.OK);
    }

    /**
     * Retrieves member by Id
     * @param id
     * @return
     */
    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMemberById(@PathVariable("id") String id) {
        MemberMaster memberMaster = service.getEnrolledMember(id);
        if (memberMaster == null) {
            logger.error("Member with id {} not found.", id);
            return new ResponseEntity(new ErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<MemberMaster>(memberMaster, HttpStatus.OK);
    }
}
