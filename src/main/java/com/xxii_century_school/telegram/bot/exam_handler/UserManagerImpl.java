package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfo;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.objects.User;

import java.util.List;
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
    public void nextQuestion(User user, boolean wasCorrect) {
        UserInfo userInfo = getUserInfo(user);
        if (userInfo.getCurrentQuestionId() == null) {
            endCurrentExam(user);
            return;
        }
        userInfo.setCurrentQuestionId(userInfo.getCurrentQuestionId() + 1);
        if (userInfo.getCurrentQuestionId() > getCurrentExam(user).getTasks().size()) {
            endCurrentExam(user);
        }
        userInfo.getAnswerResults().add(wasCorrect);
        saveUserInfo(userInfo);
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
            saveUserInfo(userInfo);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public UserInfo saveUserInfo(UserInfo userInfo) {
        UserInfo oldUserInfo = userInfoDao.getByUserId(userInfo.getUserId());
        if (oldUserInfo != null) {
            userInfoDao.deleteAllBy(userInfo.getUserId());
        }
        return userInfoDao.save(userInfo);
    }

    @Override
    public List<Boolean> getAnswerResults(User user) {
        return getUserInfo(user).getAnswerResults();
    }

    @Override
    public void addAnswerResult(User user, boolean result) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.getAnswerResults().add(result);
        saveUserInfo(userInfo);
    }

    @Override
    public void endCurrentExam(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.setCurrentExamId(null);
        userInfo.setCurrentQuestionId(null);
        endCurrentQuestion(user);
        saveUserInfo(userInfo);
    }

    @Override
    public void endCurrentQuestion(User user) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.getCurrentAnswers().clear();
        saveUserInfo(userInfo);
    }

    public UserInfo getUserInfo(User user) {
        UserInfo info = userInfoDao.getByUserId(user.getId());
        if (info == null) {
            info = new UserInfo();
            info.setUserId(user.getId());
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
    public void addCurrendtAnswer(User user, String currentAnswer) {
        UserInfo userInfo = getUserInfo(user);
        userInfo.getCurrentAnswers().add(currentAnswer);
        saveUserInfo(userInfo);
    }

    @Override
    public Integer getCurrentQuestionId(User user) {
        return getUserInfo(user).getCurrentQuestionId();
    }
}
