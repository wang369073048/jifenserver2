package org.trc.domain.luckydraw;

import org.trc.constants.LuckyDraw;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.*;

/**
 * Created by george on 2017/5/3.
 */
public class ActivityPrizesDO implements Serializable, Comparable<ActivityPrizesDO> {

    private final static List<ActivityPrizesDO> defaultActivityPrizes = new ArrayList<>();
    private final static Map<String, ActivityPrizesDO> defaultDataMap = new HashMap<>();

    static{
        ActivityPrizesDO activityPrize1 = new ActivityPrizesDO();
        activityPrize1.setPrizeType(LuckyDraw.PrizyType.TKY_FOR_PARTICIPATING);
        activityPrize1.setPrizeUrl("{\"fileUrl\":\"https://testimg.trc.com/FrqmcdKbqWsDJAW3AnHS2Jvv5IIz\",\"$$hashKey\":109}");
        activityPrize1.setName("谢谢参与");
        defaultActivityPrizes.add(activityPrize1);
        defaultDataMap.put(LuckyDraw.PrizyType.TKY_FOR_PARTICIPATING, activityPrize1);
        ActivityPrizesDO activityPrize2 = new ActivityPrizesDO();
        activityPrize2.setPrizeType(LuckyDraw.PrizyType.TCOIN);
        activityPrize2.setPrizeUrl("{\"fileUrl\":\"https://testimg.trc.com/FuJRHR2KurfHTOj6SGv_TodZD9NT\",\"$$hashKey\":106}");
        activityPrize2.setGoodsType(LuckyDraw.GoodsType.VIRTUAL);
        activityPrize2.setName("T币");
        defaultActivityPrizes.add(activityPrize2);
        defaultDataMap.put(LuckyDraw.PrizyType.TCOIN, activityPrize2);
        ActivityPrizesDO activityPrize3 = new ActivityPrizesDO();
        activityPrize3.setPrizeUrl("{\"fileUrl\":\"https://testimg.trc.com/FhAGGTtVhDIPxpI6tjZxWmqls_rG\",\"$$hashKey\":112}");
        activityPrize3.setPrizeType(LuckyDraw.PrizyType.SCORE);
        activityPrize3.setGoodsType(LuckyDraw.GoodsType.VIRTUAL);
        activityPrize3.setName("积分");
        defaultActivityPrizes.add(activityPrize3);
        defaultDataMap.put(LuckyDraw.PrizyType.SCORE, activityPrize3);
    }

    public static List<ActivityPrizesDO> getDefaultActivityPrizes(){
        List<ActivityPrizesDO> cloneList = new ArrayList<>(defaultActivityPrizes);
        return cloneList;
    }

    public static Map<String, ActivityPrizesDO> getDefaultDataMap(){
        Map<String, ActivityPrizesDO> cloneMap = new HashMap<>(defaultDataMap);
        return cloneMap;
    }

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 抽奖活动id
     */
    private Long luckyDrawId;

    /**
     * 奖品id
     */
    private Long goodsId;

    /**
     * 奖品编码
     */
    private String goodsNo;

    /**
     * 奖品类型 SCORE|TCOIN|GOODS
     */
    private String prizeType;

    /**
     * 商品类型  1虚拟，2实物
     */
    private String goodsType;

    /**
     * 奖品图片
     */
    private String prizeUrl;

    /**
     * 奖品名称
     */
    private String name;

    /**
     * 单次中奖奖品数量
     */
    private Integer numberOfPrizes;

    /**
     * 用户中奖次数限制
     */
    private Integer winningLimit;

    /**
     * 中奖次数限制
     */
    private Integer winningTimes;
    /**
     * 中奖次数限制类型:PER_DAY每天,THE_WHOLE_ACTIVITY全程
     */
    private String winningType;

    /**
     * 中奖几率:单位/万分之一
     */
    private Integer winningProbability;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Long category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getLuckyDrawId() {
        return luckyDrawId;
    }

    public void setLuckyDrawId(Long luckyDrawId) {
        this.luckyDrawId = luckyDrawId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getPrizeUrl() {
        return prizeUrl;
    }

    public void setPrizeUrl(String prizeUrl) {
        this.prizeUrl = prizeUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfPrizes() {
        return numberOfPrizes;
    }

    public void setNumberOfPrizes(Integer numberOfPrizes) {
        this.numberOfPrizes = numberOfPrizes;
    }

    public Integer getWinningLimit() {
        return winningLimit;
    }

    public void setWinningLimit(Integer winningLimit) {
        this.winningLimit = winningLimit;
    }

    public Integer getWinningTimes() {
        return winningTimes;
    }

    public void setWinningTimes(Integer winningTimes) {
        this.winningTimes = winningTimes;
    }

    public String getWinningType() {
        return winningType;
    }

    public void setWinningType(String winningType) {
        this.winningType = winningType;
    }

    public Integer getWinningProbability() {
        return winningProbability;
    }

    public void setWinningProbability(Integer winningProbability) {
        this.winningProbability = winningProbability;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ActivityPrizesDO{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", luckyDrawId=" + luckyDrawId +
                ", goodsId=" + goodsId +
                ", goodsNo=" + goodsNo +
                ", prizeType='" + prizeType + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", prizeUrl='" + prizeUrl + '\'' +
                ", name='" + name + '\'' +
                ", numberOfPrizes=" + numberOfPrizes +
                ", winningLimit='" + winningLimit + '\'' +
                ", winningTimes='" + winningTimes + '\'' +
                ", winningType='" + winningType + '\'' +
                ", winningProbability=" + winningProbability +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(ActivityPrizesDO o) {
        if(null == this.winningProbability && null == o.winningProbability){
            return 0;
        }else if(null == this.winningProbability){
            return -1;
        }else if(null == o.winningProbability){
            return 1;
        }
        return this.winningProbability.compareTo(o.winningProbability);
    }
}