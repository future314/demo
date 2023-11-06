package zhi.yuan.todolist.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import zhi.yuan.todolist.entity.Blog;
import zhi.yuan.todolist.mapper.BlogMapper;
import zhi.yuan.todolist.service.BlogService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
