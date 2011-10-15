package com.applets.models;

import java.util.Date;

public class ExamTimeFrame extends TimeFrame {
    private Date date;
    private String nameStart;
    private String nameEnd;
    private String comment;

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public String getNameStart() {
	return nameStart;
    }

    public void setNameStart(String nameStart) {
	this.nameStart = nameStart;
    }

    public String getNameEnd() {
	return nameEnd;
    }

    public void setNameEnd(String nameEnd) {
	this.nameEnd = nameEnd;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}
