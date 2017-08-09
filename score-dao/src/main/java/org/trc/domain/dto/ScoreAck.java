package org.trc.domain.dto;

import java.io.Serializable;

/**
 * Created by wangzhen
 */
public class ScoreAck<T> implements Serializable{

    private String respCode;            //默认成功编码 SCORE000001，默认未知编码 SCORE000000

    private String respMsg;

    private String orderCode;

    private T result;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ScoreAck{" +
                "respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", result=" + result +
                '}';
    }

    public static ScoreAck renderSuccess(String respCode, String orderCode){
        ScoreAck scoreAck = new ScoreAck();
        scoreAck.setRespCode(respCode);
        scoreAck.setRespMsg("操作成功!");
        scoreAck.setOrderCode(orderCode);
        return scoreAck;
    }

    public static ScoreAck renderUnknown(String orderCode){
        ScoreAck scoreAck = new ScoreAck();
        scoreAck.setRespCode("SCORE000000");
        scoreAck.setRespMsg("T币兑换超时，兑换结果未知!");
        scoreAck.setOrderCode(orderCode);
        return scoreAck;
    }

    public static ScoreAck renderFailure(String respCode,String orderCode){
        ScoreAck scoreAck = new ScoreAck();
        scoreAck.setRespCode(respCode);
        scoreAck.setRespMsg("操作失败!");
        scoreAck.setOrderCode(orderCode);
        return scoreAck;
    }

    public static ScoreAck renderFailure(String respCode, String respMsg, String orderCode){
        ScoreAck scoreAck = new ScoreAck();
        scoreAck.setRespCode(respCode);
        scoreAck.setRespMsg(respMsg);
        scoreAck.setOrderCode(orderCode);
        return scoreAck;
    }
}
