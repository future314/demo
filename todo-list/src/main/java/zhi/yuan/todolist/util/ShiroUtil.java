package zhi.yuan.todolist.util;


import org.apache.shiro.SecurityUtils;
import zhi.yuan.todolist.shiro.AccountProfile;

/**
 * @author LR
 */
public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
