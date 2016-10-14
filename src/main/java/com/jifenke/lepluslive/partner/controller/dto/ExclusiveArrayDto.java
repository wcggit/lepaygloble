package com.jifenke.lepluslive.partner.controller.dto;

import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;

/**
 * Created by wcg on 16/10/13.
 */
public class ExclusiveArrayDto {

    private Long[] ids;

    private LeJiaUserCriteria leJiaUserCriteria;

    private PartnerWelfareLog partnerWelfareLog;

    private Long[] conflictArray;

    public Long[] getConflictArray() {
        return conflictArray;
    }

    public void setConflictArray(Long[] conflictArray) {
        this.conflictArray = conflictArray;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public LeJiaUserCriteria getLeJiaUserCriteria() {
        return leJiaUserCriteria;
    }

    public void setLeJiaUserCriteria(LeJiaUserCriteria leJiaUserCriteria) {
        this.leJiaUserCriteria = leJiaUserCriteria;
    }

    public PartnerWelfareLog getPartnerWelfareLog() {
        return partnerWelfareLog;
    }

    public void setPartnerWelfareLog(PartnerWelfareLog partnerWelfareLog) {
        this.partnerWelfareLog = partnerWelfareLog;
    }
}
