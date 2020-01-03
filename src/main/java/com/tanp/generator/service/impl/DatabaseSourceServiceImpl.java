package com.tanp.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanp.generator.entity.DatabaseSource;
import com.tanp.generator.mapper.DatabaseSourceMapper;
import com.tanp.generator.service.DatabaseSourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CodeBricklayer
 * @since 2020-01-03
 */
@Service
public class DatabaseSourceServiceImpl extends
    ServiceImpl<DatabaseSourceMapper, DatabaseSource> implements DatabaseSourceService {

}
