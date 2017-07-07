package org.trc.form.pagehome;

import org.trc.util.QueryModel;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by hzwzhen on 2017/6/13.
 */
public class BannerContentForm extends QueryModel{
    @PathParam("shopId")
    private Long shopId; //商铺Id
    @QueryParam("title")
    private String title; //标题
    @QueryParam("type")
    private String type; //类型

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
