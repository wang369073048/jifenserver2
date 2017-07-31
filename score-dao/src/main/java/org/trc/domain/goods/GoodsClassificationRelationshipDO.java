package org.trc.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by george on 2017/7/5.
 */
@Table(name = "goods_classification_relationship")
public class GoodsClassificationRelationshipDO implements Serializable{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品id
     */
    @Column(name = "goodsId")
    private Long goodsId;

    /**
     * 展示分类id
     */
    @Column(name = "shopClassificationId")
    private Long shopClassificationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getShopClassificationId() {
        return shopClassificationId;
    }

    public void setShopClassificationId(Long shopClassificationId) {
        this.shopClassificationId = shopClassificationId;
    }

    @Override
    public String toString() {
        return "GoodsClassificationRelationshipDO{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", shopClassificationId=" + shopClassificationId +
                '}';
    }

}
