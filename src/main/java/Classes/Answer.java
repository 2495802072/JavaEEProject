package Classes;

import java.util.Date;

public class Answer {
    private int ans_id;
    private int owner_id;
    private Date date;
    private String answer;
    private int question_id;

    public Answer() {}
    public Answer(int ans_id,int owner_id , Date date, String answer,int question_id) {
        this.ans_id = ans_id;
        this.owner_id = owner_id;
        this.question_id = question_id;
        this.date = date;
        this.answer = answer;
    }

    public int getAns_id() {
        return ans_id;
    }

    public void setAns_id(int ans_id) {
        this.ans_id = ans_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
