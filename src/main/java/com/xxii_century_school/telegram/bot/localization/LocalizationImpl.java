package com.xxii_century_school.telegram.bot.localization;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalNotification;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Scope("singleton")
public class LocalizationImpl implements Localization {

    private void onRemove(RemovalNotification<Object, Object> removalNotification) {
        //log.info("cache removed " + removalNotification.getValue() + " cause: " + removalNotification.getCause());
    }


    @Override
    public Localizer get(String locale) {
        try {
            if (locale == null) {
                locale = "en-US";
            }
            String finalLocale = locale;
            Localizer localizer = cache.get(locale, () -> new Localizer(finalLocale));
            return localizer;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    Cache<String, Localizer> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Localizer>() {
                @Override
                public Localizer load(String key) throws Exception {
                    return new Localizer(key);
                }
            });


}
