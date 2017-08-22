package com.xunli.manager.cache;

import com.sun.org.apache.regexp.internal.RE;
import com.xunli.manager.model.DictInfo;
import com.xunli.manager.repository.DictInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shihj on 2017/8/21.
 */
@Component
public class DictInfoCache {
    private final static Logger log = LoggerFactory.getLogger(DictInfoCache.class);

    public static List<DictInfo> dictInfos;

    public final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private DictInfoRepository dictInfoRepository;

    public void updateCache()
    {
        log.info("正在加载字典缓存信息.........");
        lock.lock();
        try
        {
            dictInfos = dictInfoRepository.findAll();
        }
        finally
        {
            lock.unlock();
        }
        log.info(String.format("字典缓存信息加载完成,共 %s 条",dictInfos.size()));
    }
}
