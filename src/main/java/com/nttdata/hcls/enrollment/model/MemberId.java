package com.nttdata.hcls.enrollment.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MemberId implements Serializable {
    @NotNull
    @Column(name = "SEQ_MEMB_ID")
    private long seqMembId;
    @NotNull
    @Size(max = 50)
    @Column(name = "SUBSCRIBER_ID")
    private String subscriberId;

    public MemberId() {
    }

    public MemberId(long seqMembId, String subscriberId) {
        this.seqMembId = seqMembId;
        this.subscriberId = subscriberId;
    }
    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setSeqMembId(long seqMembId) {
        this.seqMembId = seqMembId;
    }

    public long getSeqMembId() {
        return seqMembId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberId memberId = (MemberId) o;
        return seqMembId == memberId.seqMembId &&
                subscriberId.equals(memberId.subscriberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seqMembId, subscriberId);
    }
}
