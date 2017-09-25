package com.jifenke.lepluslive.cmbc.service;

import com.jifenke.lepluslive.cmbc.domain.criteria.CmbcSettlementCriteria;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSettlement;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerNexus;
import com.jifenke.lepluslive.cmbc.repository.CmbcSettlementRepository;
import com.jifenke.lepluslive.global.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 民生结算单
 * Created by zhangwen on 2017/9/21.
 */
@Service
public class CmbcSettlementService {

    @Autowired
    private CmbcSettlementRepository repository;

    @Autowired
    private CmbcSubMerNexusService cmbcSubMerNexusService;

    @Autowired
    private CmbcSubMerchantService cmbcSubMerchantService;

    @Autowired
    private SqlService sqlService;

    public CmbcSettlement findById(Long id) {
        return repository.findOne(id);
    }

    /**
     * 民生结算单列表
     *
     * @param cmbcMerchantNo 子商户号
     * @param offset         当前页码
     */
    public List<Map<String, Object>> listByMerchantNoAndPage(String cmbcMerchantNo, Integer offset) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id AS id,settle_date AS settleDate,wx_actual AS wxActual,ali_actual AS aliActual,total_actual AS totalActual,state AS state FROM cmbc_settlement WHERE cmbc_merchant_no = '");
        sql.append(cmbcMerchantNo).append("' ORDER BY id DESC LIMIT ").append((offset - 1) * 10).append(",10");
        return sqlService.listBySql(sql.toString());
    }


    public List<Map<String, Object>> listByCriteria(CmbcSettlementCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id AS id,settle_date AS settleDate,wx_actual AS wxActual,ali_actual AS aliActual,total_actual AS totalActual,le_actual AS leActual,state AS state,acc_name AS accName,acc_no AS accNo FROM cmbc_settlement ");
        sql.append(" WHERE cmbc_merchant_no = '"+criteria.getCmbcMerchantNo()+"' ");
        if(criteria.getStartDate()!=null && !"".equals(criteria.getStartDate())) {
            String startDate = criteria.getStartDate().replace("/","-");
            String endDate = criteria.getEndDate().replace("/","-");
            sql.append(" AND settle_date>= '"+startDate+"' AND settle_date<='"+endDate+"' ");
        }
        if(criteria.getState()!=null) {
            sql.append(" AND state = "+criteria.getState());
        }
        sql.append(" ORDER BY id DESC LIMIT ").append((criteria.getOffset() - 1) * 10).append(",10");
        return sqlService.listBySql(sql.toString());
    }

    public Map<String, Object> countByCriteria(CmbcSettlementCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(*) AS totalElements FROM cmbc_settlement ");
        sql.append(" WHERE cmbc_merchant_no = '"+criteria.getCmbcMerchantNo()+"' ");
        if(criteria.getStartDate()!=null && !"".equals(criteria.getStartDate())) {
            sql.append(" AND settle_date>= '"+criteria.getStartDate()+"' AND settle_date<='"+criteria.getEndDate()+"' ");
        }
        if(criteria.getState()!=null) {
            sql.append(" AND state = "+criteria.getState());
        }
        return sqlService.listBySql(sql.toString()).get(0);
    }

    /**
     * 民生结算单详情页获取某一种类型的列表数据
     *
     * @param id   结算单ID
     * @param type 类型 0=普通微信|1=普通支付宝|2=乐加
     */
    public Map<String, Object> settlementDetailList(Long id, Integer type) {
        Map<String, Object> result = new HashMap<>();
        CmbcSettlement settlement = repository.findOne(id);
        StringBuffer sql = new StringBuffer();
        //获取该类型该结算单的退款记录
        if (type == 0 || type == 1) {//tradeDate=settlement.settleDate、real_mer_num=settlement.cmbcMerchantNo、state=2、orderType=0、payType=type
            sql.append("SELECT m.`name` AS shopName,o.total_amount AS totalAmount,o.date_created AS dateCreated,o.merchant_rate AS merchantRate FROM channel_refund_order o INNER JOIN merchant m ON o.merchant_id = m.id")
                    .append(" WHERE o.trade_date = '").append(settlement.getSettleDate()).append("'")
                    .append(" AND o.real_mer_num = '").append(settlement.getCmbcMerchantNo()).append("'")
                    .append(" AND o.state = 2 AND o.order_type = 0 AND o.pay_type = ").append(type);
        } else {//tradeDate=settlement.settleDate、real_mer_num=settlement.cmbcMerchantNo、state=2、orderType=1
            sql.append("SELECT m.`name` AS shopName,o.total_amount AS totalAmount,o.date_created AS dateCreated,o.transfer_money AS transferMoney FROM channel_refund_order o INNER JOIN merchant m ON o.merchant_id = m.id")
                    .append(" WHERE o.trade_date = '").append(settlement.getSettleDate()).append("'")
                    .append(" AND o.real_mer_num = '").append(settlement.getCmbcMerchantNo()).append("'")
                    .append(" AND o.state = 2 AND o.order_type = 1");
        }
        result.put("refundList", sqlService.listBySql(sql.toString()));
        //统计使用该商户号的门店列表
        List<CmbcSubMerNexus> nexusList = cmbcSubMerNexusService.findAllByCmbcSubMerchant(cmbcSubMerchantService.findBySubMerchantNo(settlement.getCmbcMerchantNo()));
        Map<String, Object> shopOrderList = new HashMap<>();
        if (nexusList != null && nexusList.size() > 0) {
            for (CmbcSubMerNexus nexus : nexusList) {
                sql.setLength(0);
                //查询某个门店某结算单产生的某种类型订单列表
                if (type == 0 || type == 1) {
                    //settleDate=settlement.settleDate、merchant=nexus.merchant、realMerNum=settlement.cmbcMerchantNo、basicType=0、payType=type、state in (1,2)
                    sql.append("SELECT o.complete_date AS completeDate,o.total_price AS totalPrice,e.merchant_rate AS merchantRate FROM scan_code_order o INNER JOIN scan_code_order_ext e ON o.scan_code_order_ext_id = e.id")
                            .append(" WHERE o.settle_date = '").append(settlement.getSettleDate()).append("'")
                            .append(" AND o.merchant_id = ").append(nexus.getMerchant().getId())
                            .append(" AND e.real_mer_num = '").append(settlement.getCmbcMerchantNo()).append("'")
                            .append(" AND e.basic_type = 0 AND e.pay_type = ").append(type)
                            .append(" AND o.state IN (1,2)");
                } else {
                    //settleDate=settlement.settleDate、merchant=nexus.merchant、realMerNum=settlement.cmbcMerchantNo、basicType=1、state in (1,2)
                    sql.append("SELECT o.complete_date AS completeDate,o.total_price AS totalPrice,e.merchant_rate AS merchantRate,o.transfer_money AS transferMoney FROM scan_code_order o INNER JOIN scan_code_order_ext e ON o.scan_code_order_ext_id = e.id")
                            .append(" WHERE o.settle_date = '").append(settlement.getSettleDate()).append("'")
                            .append(" AND o.merchant_id = ").append(nexus.getMerchant().getId())
                            .append(" AND e.real_mer_num = '").append(settlement.getCmbcMerchantNo()).append("'")
                            .append(" AND e.basic_type = 1 AND o.state IN (1,2)");
                }
                shopOrderList.put(nexus.getMerchant().getName(), sqlService.listBySql(sql.toString()));
            }
        }
        result.put("shopOrderList", shopOrderList);
        return result;
    }
}
