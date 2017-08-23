package org.trc.domain.admin;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月22日 上午10:18:14
 */
@Table(name = "request_flow")
public class RequestFlow implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	/**
     * 主键
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 请求发起方
     */
    @Column(name = "requester")
    private String requester;

    /**
     * 请求响应方
     */
    @Column(name = "responder")
    private String responder;

    /**
     * 类型
     */
    private String type;

    /**
     *  请求流水号
     */
    @Column(name = "requestNum")
    private String requestNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 请求参数
     */
    @Column(name = "requestParam")
    private String requestParam;

    /**
     * 应答
     */
    @Column(name = "responseResult")
    private String responseResult;

    /**
     * 请求时间
     */
    @Column(name = "requestTime")
    private Date requestTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "updateTime")
    private Date updateTime;
    @Transient
    private List<String> statusList;

    public RequestFlow(){

    }

    public RequestFlow(String requester, String responder, String type, String requestNum, String status, String requestParam, String responseResult, Date requestTime, String remark){
        this.requester = requester;
        this.responder = responder;
        this.type = type;
        this.requestNum = requestNum;
        this.status = status;
        this.requestParam = requestParam;
        this.responseResult = responseResult;
        this.requestTime = requestTime;
        this.remark = remark;
        Date time = Calendar.getInstance().getTime();
        this.createTime = time;
        this.updateTime = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(String requestNum) {
        this.requestNum = requestNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    @Override
    public String toString() {
        return "RequestFlow{" +
                "id=" + id +
                ", requester='" + requester + '\'' +
                ", responder='" + responder + '\'' +
                ", type='" + type + '\'' +
                ", requestNum='" + requestNum + '\'' +
                ", status='" + status + '\'' +
                ", requestParam='" + requestParam + '\'' +
                ", responseResult='" + responseResult + '\'' +
                ", requestTime=" + requestTime +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public static class Status{

        /**
         * 初始状态
         */
        public static final String INITIAL = "INITIAL";

        /**
         * 成功状态
         */
        public static final String SUCCESS = "SUCCESS";

        /**
         * T币发放成功，兑换失败
         */
        public static final String GAIN_SUCCESS_EXCHANGE_FAILURE = "GAIN_SUCCESS_EXCHANGE_FAILURE";

        /**
         * T币扣减成功，积分生成失败
         */
        public static final String EXCHANGE_SUCCESS_PRODUCE_FAILURE = "EXCHANGE_SUCCESS_PRODUCE_FAILURE";

        /**
         * 兑换额度不足
         */
        public static final String LACK_OF_EXCHANGE_AMOUNT = "LACK_OF_EXCHANGE_AMOUNT";

        /**
         * 应答超时
         */
        public static final String SOCKET_TIME_OUT = "SOCKET_TIME_OUT";

        /**
         * 获取T币超时
         */
        public static final String GAIN_SOCKET_TIME_OUT = "GAIN_SOCKET_TIME_OUT";

        /**
         * 获取T币失败
         */
        public static final String GAIN_FAILURE = "GAIN_FAILURE";

        /**
         * 兑换T币T币扣减超时
         */
        public static final String EXCHANGE_SOCKET_TIME_OUT = "EXCHANGE_SOCKET_TIME_OUT";

        /**
         * T币兑换失败
         */
        public static final String EXCHANGE_FAILURE = "EXCHANGE_FAILURE";

        /**
         * 未知错误
         */
        public static final String UNKNOWN = "UNKNOWN";

        /**
         * 积分抽奖中积分T币获取成功，积分发放未知错误
         */
        public static final String GAIN_UNKNOWN = "GAIN_UNKNOWN";

        /**
         * 积分抽奖中积分T币获取成功，积分兑换未知错误
         */
        public static final String GAIN_SUCCESS_EXCHANGE_UNKNOWN = "GAIN_SUCCESS_EXCHANGE_UNKNOWN";

        /**
         * 失败
         */
        public static final String FAILURE = "FAILURE";

        /**
         * 错误
         */
        public static final String ERROR = "ERROR";

    }

}
