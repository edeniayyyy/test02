package org.example.POJO.domainmodel;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author edenia
 * @version 1.0
 * @date 2023/6/5 16:04
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    @TableField(value = "create_time",
            fill = FieldFill.INSERT)
    Date createTime;

    @TableField(value = "create_by",
            fill = FieldFill.INSERT)
    String createBy;

    @TableField(value = "updated_time",
            fill = FieldFill.UPDATE)
    Date updateTime;

    @TableField(value ="updated_By",
            fill = FieldFill.UPDATE)
    String updateBy;


}
