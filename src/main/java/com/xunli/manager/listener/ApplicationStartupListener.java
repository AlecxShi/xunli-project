package com.xunli.manager.listener;

import com.xunli.manager.cache.DictInfoCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by shihj on 2017/8/21.
 */
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent>{

    private DictInfoCache dictInfoCache = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(dictInfoCache == null)
        {
            ApplicationContext context = contextRefreshedEvent.getApplicationContext();
            dictInfoCache = context.getBean(DictInfoCache.class);
        }
        dictInfoCache.updateCache();
    }
}
