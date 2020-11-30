package cn.edu.hit.spat.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XuJian
 */
@Data
@TableName("t_goods")
public class Goods implements Serializable {
    /**
     * 货品 ID
     */
    @TableId(value = "GOODS_ID", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 货品名
     */
    @TableField("NAME")
    private String name;

    /**
     * 进货价格
     */
    @TableField("PURCHASE_PRICE")
    private String purchasePrice;

    /**
     * 批发价格
     */
    @TableField("WHOLESALE_PRICE")
    private String wholesalePrice;

    /**
     * 零售价格
     */
    @TableField("RETAIL_PRICE")
    private String retailPrice;

    public Long getId() {
        return goodsId;
    }
}
