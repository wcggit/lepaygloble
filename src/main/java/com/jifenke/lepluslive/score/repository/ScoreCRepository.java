package com.jifenke.lepluslive.score.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by xf on 17-2-20.
 */
public interface ScoreCRepository extends JpaRepository<ScoreC, Long> {
    ScoreC findByLeJiaUser(LeJiaUser leJiaUser);

    Optional<List<ScoreC>> findByScoreGreaterThan(Long i);



    @Query(value = "SELECT SUM(score) from scorec ", nativeQuery = true)
    long findScoreCSum();



    @Query(value = "SELECT SUM(number) from scorec_detail where number>0", nativeQuery = true)
    long findSendScoreCSum();

    @Query(value = "SELECT SUM(number) from scorec_detail where number<0", nativeQuery = true)
    long findUseScoreCSum();



    @Query(value = "SELECT a.sendDays,a.sendScore,b.useScore  from" +
            "  (SELECT  DATE_FORMAT(date_created,'%Y%m%d')as sendDays ,SUM(number) as sendScore from scorec_detail where number>0 GROUP BY DATE_FORMAT(date_created,'%Y%m%d') ORDER BY sendDays DESC)a" +
            "  LEFT JOIN" +
            "  (SELECT  DATE_FORMAT(date_created,'%Y%m%d')as useDays ,SUM(number) as useScore from scorec_detail where number<0 GROUP BY DATE_FORMAT(date_created,'%Y%m%d') ORDER BY useDays DESC)b" +
            "  on a.sendDays=b.useDays where a.sendDays>=?1 and a.sendDays<=?2 ORDER BY a.sendDays DESC ", nativeQuery = true)
    List<Object[]> findScoreCStatistics(String startDate, String endDate);



    @Query(value = "SELECT if(SUM(number) IS NULL,0,SUM(number)) as sendScore from scorec_detail where number>0 and  DATE_FORMAT(date_created,'%Y%m%d')>=?1 and  DATE_FORMAT(date_created,'%Y%m%d')<=?2", nativeQuery = true)
    Long findSendScoreCByDate(String startDate, String endDate);


    @Query(value = "SELECT if(SUM(number) IS NULL,0,SUM(number)) as sendScore from scorec_detail where number<0 and  DATE_FORMAT(date_created,'%Y%m%d')>=?1 and  DATE_FORMAT(date_created,'%Y%m%d')<=?2", nativeQuery = true)
    Long findUseScoreCByDate(String startDate, String endDate);


}
