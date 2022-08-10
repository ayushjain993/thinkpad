package io.uhha.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 促销统计实体类
 *
 * @author mj created on 2019/4/11
 */
@Data
@ApiModel(description = "促销统计实体类")
public class MarketingStatistics {

    /**
     * 定金预售统计
     */
    @ApiModelProperty(value = "定金预售统计")
    private MarketingOrderStatistics depositPreSaleStatistics;

    /**
     * 全款预售统计
     */
    @ApiModelProperty(value = "全款预售统计")
    private MarketingOrderStatistics fullPreSaleStatistics;

    /**
     * 拼团统计
     */
    @ApiModelProperty(value = "拼团统计")
    private MarketingOrderStatistics groupStatistics;

    /**
     * 众筹全款统计
     */
    @ApiModelProperty(value = "众筹全款统计")
    private MarketingOrderStatistics crowdfundingFullStatistics;

    /**
     * 众筹一元抽奖统计
     */
    @ApiModelProperty(value = "众筹一元抽奖统计")
    private MarketingOrderStatistics crowdfundingOneLotteryStatistics;

    /**
     * 众筹无回报统计
     */
    @ApiModelProperty(value = "众筹无回报统计")
    private MarketingOrderStatistics crowdfundingNoReturnSupportStatistic;

    /**
     * 构建促销统计
     *
     * @param depositPreSaleStatistics             定金预售统计
     * @param fullPreSaleStatistics                全款预售统计
     * @param groupStatistics                      拼团统计
     * @param crowdfundingFullStatistics           众筹全款统计
     * @param crowdfundingOneLotteryStatistics     众筹一元抽奖统计
     * @param crowdfundingNoReturnSupportStatistic 众筹无回报统计
     * @return 促销统计
     */
    public static MarketingStatistics buildMarketingStatistics(MarketingOrderStatistics depositPreSaleStatistics, MarketingOrderStatistics fullPreSaleStatistics, MarketingOrderStatistics groupStatistics, MarketingOrderStatistics crowdfundingFullStatistics, MarketingOrderStatistics crowdfundingOneLotteryStatistics, MarketingOrderStatistics crowdfundingNoReturnSupportStatistic) {
        MarketingStatistics marketingStatistics = new MarketingStatistics();
        marketingStatistics.depositPreSaleStatistics = depositPreSaleStatistics;
        marketingStatistics.fullPreSaleStatistics = fullPreSaleStatistics;
        marketingStatistics.groupStatistics = groupStatistics;
        marketingStatistics.crowdfundingFullStatistics = crowdfundingFullStatistics;
        marketingStatistics.crowdfundingOneLotteryStatistics = crowdfundingOneLotteryStatistics;
        marketingStatistics.crowdfundingNoReturnSupportStatistic = crowdfundingNoReturnSupportStatistic;
        return marketingStatistics;
    }

}
