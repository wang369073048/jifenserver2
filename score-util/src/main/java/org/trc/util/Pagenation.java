package org.trc.util;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzwdx on 2017/4/22.
 */
public class Pagenation<T> implements Serializable {
    /**
     * 每页最大记录条数
     */
    private static final int MAX_PAGE_SIZE = 100;

    // -- 分页参数 --//
    /*
    当前页数
     */
    @DefaultValue("1")
    @QueryParam("pageIndex")
    protected int pageIndex = 1;
    /*
    开始记录行数
     */
    @QueryParam("start")
    protected int start = 0;
    /*
    每页记录条数
     */
    @DefaultValue("20")
    @QueryParam("pageSize")
    protected int pageSize = 20;

    // -- 返回结果 --//
    protected List<T> infos = new ArrayList<T>();
    protected long totalData = -1;

    public String appcode = "1";

    private Integer totalExchangeCount;
    private Long exchangeNum;
    private Integer totalConsumptionCount;
    private Long consumptionNum;

    // -- 构造函数 --//
    public Pagenation() {

    }

    public Pagenation(int pageSize) {
        this.pageSize = pageSize;
    }

    // -- 分页参数访问函数 --//

    /**
     *获取记录开始行数
     */
    public int getStart() {
        return start;
    }
    /**
     *设置记录开始行数
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setPageIndex(final int pageIndex) {
        this.pageIndex = pageIndex;
        if (pageIndex < 1) {
            this.pageIndex = 1;
        }
    }

    /**
     * 返回Page对象自身的setpageIndex函数,可用于连续设置。
     */
    public Pagenation<T> pageIndex(final int thePageIndex) {
        setPageIndex(thePageIndex);
        return this;
    }

    /**
     * 获得每页的记录数量, 默认为-1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量.
     */
    public void setPageSize(final int pageSize) {
        if(pageSize < 0)
            this.pageSize = -1;
        else if(pageSize >= MAX_PAGE_SIZE)
            this.pageSize = MAX_PAGE_SIZE;
        else
            this.pageSize = pageSize;
    }

    /**
     * 返回Page对象自身的setPageSize函数,可用于连续设置。
     */
    public Pagenation<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 根据pageIndex和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((pageIndex - 1) * pageSize) + 1;
    }

    /**
     * 获得页内的记录列表.
     */
    public List<T> getInfos() {
        return infos;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setInfos(final List<T> infos) {
        this.infos = infos;
    }

    /**
     * 获得总记录数, 默认值为-1.
     */
    public long getTotalData() {
        return totalData;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalData(final long totalData) {
        this.totalData = totalData;
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPage() {
        if (totalData < 0) {
            return -1;
        }
        long count = totalData / pageSize;
        if (totalData % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageIndex + 1 <= getTotalPage());
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageIndex + 1;
        } else {
            return pageIndex;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageIndex - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageIndex - 1;
        } else {
            return pageIndex;
        }
    }

    public Integer getTotalExchangeCount() {
        return totalExchangeCount;
    }

    public void setTotalExchangeCount(Integer totalExchangeCount) {
        this.totalExchangeCount = totalExchangeCount;
    }

    public Long getExchangeNum() {
        return exchangeNum;
    }

    public void setExchangeNum(Long exchangeNum) {
        this.exchangeNum = exchangeNum;
    }

    public Integer getTotalConsumptionCount() {
        return totalConsumptionCount;
    }

    public void setTotalConsumptionCount(Integer totalConsumptionCount) {
        this.totalConsumptionCount = totalConsumptionCount;
    }

    public Long getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(Long consumptionNum) {
        this.consumptionNum = consumptionNum;
    }
}

