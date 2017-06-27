package org.trc.form.pagehome;

import org.trc.util.QueryModel;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by hzwzhen on 2017/6/10.
 */
public class BannerForm extends QueryModel{
    @PathParam("id")
    private Long id; //主键
    @PathParam("shopId")
    private Long shopId; //商铺Id
    @QueryParam("name")
    private String name; //名称
    @QueryParam("isUp")
    private Integer isUp; //是否上架,0上架,1下架
    @FormParam("contentId")
    private Long contentId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }


}
