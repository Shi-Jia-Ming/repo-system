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
@TableName("t_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 客户 ID
     */
    @TableId(value = "CUSTOMER_ID", type = IdType.AUTO)
    private Long customerId;

    /**
     * 客户姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 客户性别
     */
    @TableField("GENDER")
    private String gender;

    /**
     * 联系电话
     */
    @TableField("PHONE")
    private Long phone;

    public Long getId() {
        return customerId;
    }
}
