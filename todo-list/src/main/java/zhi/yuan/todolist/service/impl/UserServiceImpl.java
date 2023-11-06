package zhi.yuan.todolist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zhi.yuan.todolist.entity.User;
import zhi.yuan.todolist.mapper.UserMapper;
import zhi.yuan.todolist.service.UserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
