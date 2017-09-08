package com.xxii_century_school.telegram.bot.exam_handler.model;

import org.springframework.data.repository.CrudRepository;

public interface UserInfoDao extends CrudRepository<UserInfo, Long> {
    UserInfo getByUserId(int userId);

    void deleteAllByUserId(int userId);
}
