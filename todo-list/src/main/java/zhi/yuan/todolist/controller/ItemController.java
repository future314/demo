package zhi.yuan.todolist.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import zhi.yuan.todolist.common.lang.Result;
import zhi.yuan.todolist.entity.Item;
import zhi.yuan.todolist.service.ItemService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LR
 * @since 2023-11-10
 */
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @PostMapping("/save")
    public Result save(@RequestBody Item item) {
        boolean result = itemService.save(item);
        return Result.succ(result);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Item item) {
        UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", item.getId());
        boolean result = itemService.update(item, updateWrapper);
        return Result.succ(result);
    }


}
