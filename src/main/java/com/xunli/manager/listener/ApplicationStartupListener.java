package com.xunli.manager.listener;

import com.xunli.manager.cache.ColleageCache;
import com.xunli.manager.cache.DictInfoCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by shihj on 2017/8/21.
 */
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent>{

    private DictInfoCache dictInfoCache = null;

    private ColleageCache colleageCache = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        //加载字典缓存信息
        if(dictInfoCache == null)
        {
            dictInfoCache = context.getBean(DictInfoCache.class);
            dictInfoCache.updateCache();
        }
        //加载大学信息
        if(colleageCache == null)
        {
            colleageCache = context.getBean(ColleageCache.class);
            colleageCache.load();
        }
    }
}
