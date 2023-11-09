package zhi.yuan.todolist.service.impl;

import zhi.yuan.todolist.entity.Item;
import zhi.yuan.todolist.mapper.ItemMapper;
import zhi.yuan.todolist.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LR
 * @since 2023-11-10
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Override
    public boolean save(Item entity) {
        Date date = new Date();
        entity.setCreateTime(date);
        entity.setUpdateTime(date);
        return super.save(entity);
    }
}
