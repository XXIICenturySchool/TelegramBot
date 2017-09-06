package com.xxii_century_school.telegram.bot.localization;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalNotification;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Scope("singleton")
public class LocalizationImpl implements Localization {

    private void onRemove(RemovalNotification<Object, Object> removalNotification) {
        //log.info("cache removed " + removalNotification.getValue() + " cause: " + removalNotification.getCause());
    }


    @Override
    public Localizer get(String locale) {
        try {
            Localizer localizer = cache.get(locale, () -> new Localizer(locale));
            return localizer;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    Cache<String, Localizer> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Localizer>() {
                @Override
                public Localizer load(String key) throws Exception {
                    return new Localizer(key);
                }
            });

}
