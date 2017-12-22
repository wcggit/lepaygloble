package com.jifenke.lepluslive.score.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.criteria.ScoreCriteria;
import com.jifenke.lepluslive.score.domain.criteria.ScoreDetailCriteria;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;
import com.jifenke.lepluslive.score.repository.ScoreCDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreCRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 17-2-20.
 */
@Service
@Transactional(readOnly = true)
public class ScoreCService {
    @Inject
    private ScoreCRepository scoreCRepository;

    @Inject
    private ScoreCDetailRepository scoreCDetailRepository;

    /**
     * 保存用户金币  2016/12/26
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveScore(ScoreC scoreC) {
        scoreCRepository.save(scoreC);
    }

    /**
     * 保存用户金币记录  2016/12/26
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveDetail(ScoreCDetail detail) {
        scoreCDetailRepository.save(detail);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ScoreC findScoreCByWeiXinUser(LeJiaUser leJiaUser) {
        return scoreCRepository.findByLeJiaUser(leJiaUser);
    }



}
