package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfo;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.objects.User;

import java.util.Date;
import java.util.Set;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    ExamManager examManager;


    @Override
    public boolean isInExam(User user) {
        return getUserInfo(user).getCurrentExamId() != null;
    }

    @Override
    public boolean hasTeacher(User user) {
        return getTeacherId(user) != null;
    }

    @Override
    public Integer getTeacherId(User user) {
        return getUserInfo(user).getTeacherId();
    }

    @Override
    public void setTeacherId(User user, Integer teacherId) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setTeacherId(teacherId);
        saveUserInfo(userInfo);
    }

    @Override
    public Exam getCurrentExam(User user) {
        if (!hasTeacher(user)) {
            return null;
        }
        UserInfo userInfo = getUserInfo(user);
        if (userInfo.getCurrentExamId() == null) {
            return null;
        }
        return examManager.getExam(userInfo.getCurrentExamId(), getTeacherId(user));
    }

    @Override
    public Question getCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        try {
            Exam currentExam = getCurrentExam(user);
            if (currentExam != null) {
                return currentExam.getTasks().get(userInfo.getCurrentQuestionId());
            } else {
                return null;
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) { //ignored
        }
        return null;
    }

    @Override
    public void nextQuestion(User user, boolean wasCorrect, boolean skipped) {
        UserInfo userInfo = getUserInfo(user);
        if (userInfo.getCurrentQuestionId() == null) {
            endCurrentExam(user);
            return;
        }
        userInfo.setCurrentQuestionId(userInfo.getCurrentQuestionId() + 1);
        if (userInfo.getCurrentQuestionId() > getCurrentExam(user).getTasks().size()) {
            endCurrentExam(user);
        }
        saveUserInfo(userInfo);
        if (!wasCorrect && !skipped) {
            failCurrentQuestion(user);
        }
        if (skipped) {
            skipCurrentQuestion(user);
        }
        endCurrentQuestion(user);
    }

    @Override
    public boolean startExam(User user, int examId) {
        if (!hasTeacher(user)) {
            return false;
        }
        Exam e = examManager.getExam(examId, getTeacherId(user));
        if (e != null) {
            UserInfo userInfo = getUserInfo(user);
            userInfo.setCurrentExamId(examId);
            userInfo.setCurrentQuestionId(0);
            userInfo.setWrongAnswers(0);
            userInfo.setSkippedAnswers(0);
            userInfo.setExamStart(new Date());
            saveUserInfo(userInfo);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void skipCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setSkippedAnswers(userInfo.getSkippedAnswers() + 1);
        saveUserInfo(userInfo);
    }

    @Override
    public void failCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setWrongAnswers(userInfo.getWrongAnswers() + 1);
        saveUserInfo(userInfo);
    }

    @Transactional
    public UserInfo saveUserInfo(UserInfo userInfo) {
        UserInfo oldUserInfo = userInfoDao.getByUserId(userInfo.getUserId());
        if (oldUserInfo != null) {
            userInfoDao.deleteAllByUserId(userInfo.getUserId());
        }
        return userInfoDao.save(userInfo);
    }


    @Override
    public void endCurrentExam(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setCurrentExamId(null);
        userInfo.setCurrentQuestionId(null);
        userInfo.setExamStart(null);
        endCurrentQuestion(user);
        saveUserInfo(userInfo);
    }

    @Override
    public void endCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.getCurrentAnswers().clear();
        saveUserInfo(userInfo);
    }

    @Override
    public Date getExamStartDate(User user) {
        return null;
    }

    @Override
    public UserInfo getUserInfo(User user) {
        UserInfo info = userInfoDao.getByUserId(user.getId());
        if (info == null) {
            info = new UserInfo();
            info.setUserId(user.getId());
            info.setFirstName(user.getFirstName());
            info.setLastName(user.getLastName());
            info.setUserName(user.getUserName());
            saveUserInfo(info);
        }
        return info;
    }

    @Override
    public Set<String> getCurrentAnswers(User user) {
        UserInfo userInfo = getUserInfo(user);
        return userInfo.getCurrentAnswers();
    }

    @Override
    public void addCurrentAnswer(User user, String currentAnswer) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.getCurrentAnswers().add(currentAnswer);
        saveUserInfo(userInfo);
    }

    @Override
    public Integer getCurrentQuestionId(User user) {
        return getUserInfo(user).getCurrentQuestionId();
    }
}
