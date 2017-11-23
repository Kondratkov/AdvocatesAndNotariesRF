package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;

/**
 * Created by xaratyan on 02.12.2016.
 */

public class TopQuestion {
    public int Id;
    /// Вопрос
    public String Question = "";
    /// Отрасль вопроса
    public JuristSpecializationSector JuristSpecializationSector = null;
    /// Тело вопроса
    public String QuestionBody = "";
    /// Тип отвечающего
    public answerOwnerType AnswerOwnerType = null;
    /// Тело ответа на вопрос
    public String AnswerBody = "";

    public enum answerOwnerType {
        /// Нотариус
        Notary,
        /// Адвокат(юрист)
        Jurist,
        /// Колл-Центр
        CallCenter
    }
}
