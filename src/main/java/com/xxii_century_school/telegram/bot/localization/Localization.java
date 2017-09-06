package com.xxii_century_school.telegram.bot.localization;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public interface Localization {
    Localizer get(String locale);
}
