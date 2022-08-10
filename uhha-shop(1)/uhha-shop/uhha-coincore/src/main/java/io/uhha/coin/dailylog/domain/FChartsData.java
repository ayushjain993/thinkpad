package io.uhha.coin.dailylog.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册人数图表数据模型
 * @author ZKF
 */
public class FChartsData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer regnum;
	List<FCoinCharts> coinCharts = new ArrayList<>();

	public Integer getRegnum() {
		return regnum;
	}
	public void setRegnum(Integer regnum) {
		this.regnum = regnum;
	}

	public List<FCoinCharts> getCoinCharts() {
		return coinCharts;
	}

}
