package com.jifenke.lepluslive.partner.controller.dto;

import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;

/**
 * Created by wcg on 16/10/13.
 */
public class ExclusiveArrayDto {

    private Long[] ids;

    private LeJiaUserCriteria leJiaUserCriteria;

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
}
