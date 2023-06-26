package org.example.POJO.domainmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.Version;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import java.security.SecureRandom;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;


/**
 * <p>
 * users for system
 * </p>
 *
 * @author edenia
 * @since 2023-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="User对象", description="users for system")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableField("username")
    @Size(max=32)
    private String username;

    @TableField("password")
    private String password;

    @TableField("status")
    private int status;

    @TableField("group_id")
    private int roleId;
    @TableField(value = "salt")
    private byte[] salt;

    public User() {
    }

    public User(String username, String password, byte[] salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    // 密码加密
    public void codePassword(String password) {
        String algorithmName = "MD5"; // 选择 MD5 算法
        int hashIterations = 1; // 加密次数
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);  // 随机生成盐值

        Object hashedPassword = new SimpleHash(algorithmName, password, new SimpleByteSource(salt), hashIterations);
        this.password = hashedPassword.toString();
        this.salt = salt; // 将盐值保存到对象中
    }

    public static String codePassword(String password, byte[] salt) {
        String algorithmName = "MD5"; // 选择 MD5 算法
        int hashIterations = 1; // 加密次数

        Object hashedPassword = new SimpleHash(algorithmName, password, new SimpleByteSource(salt), hashIterations);
        return hashedPassword.toString();
    }
}
