package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.User;

import java.util.HashMap;
import java.util.Map;

public class UserManagerImpl implements UserManager {

    Map<Integer, UserInfo> userInfoMap = new HashMap<>();

    @Autowired
    ExamManager examManager;

    @Override
    public boolean isInExam(User user) {
        return getUserInfo(user).getCurrentExamId() != null;
    }

    @Override
    public Exam getCurrentExam(User user) {
        UserInfo userInfo = getUserInfo(user);
        return examManager.getExam(userInfo.getCurrentExamId());
    }

    @Override
    public Question getCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        try {
            return getCurrentExam(user).getTasks().get(userInfo.getCurrentQuestionId());
        } catch (IndexOutOfBoundsException e) { //ignored
        }
        return null;
    }

    @Override
    public void nextQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setCurrentQuestionId(userInfo.getCurrentQuestionId() + 1);
        if (userInfo.getCurrentQuestionId() > getCurrentExam(user).getTasks().size()) {
            userInfo.setCurrentQuestionId(null);
            userInfo.setCurrentExamId(null);
        }
    }

    @Override
    public void startExam(User user, int examId) {
        getUserInfo(user).setCurrentExamId(examId);
        getUserInfo(user).setCurrentQuestionId(0);
        Exam e = examManager.getExam(examId);
    }

    public UserInfo getUserInfo(User user) {
        UserInfo info = userInfoMap.get(user.getId());
        if (info == null) {
            info = new UserInfo();
            userInfoMap.put(user.getId(), info);
        }
        return info;
    }
}
