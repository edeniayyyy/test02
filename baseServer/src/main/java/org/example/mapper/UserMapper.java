package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.POJO.domainmodel.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * users for system Mapper 接口
 * </p>
 *
 * @author edenia
 * @since 2023-06-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
