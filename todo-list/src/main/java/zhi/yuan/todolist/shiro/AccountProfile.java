package zhi.yuan.todolist.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LR
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;

}
