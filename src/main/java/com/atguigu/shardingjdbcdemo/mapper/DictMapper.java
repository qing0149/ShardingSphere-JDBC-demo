package com.atguigu.shardingjdbcdemo.mapper;

import com.atguigu.shardingjdbcdemo.model.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
}