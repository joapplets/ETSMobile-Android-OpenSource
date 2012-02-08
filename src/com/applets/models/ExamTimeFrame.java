package com.applets.models;

import java.util.Date;

public class ExamTimeFrame extends TimeFrame {
    private String comment;
    private Date date;
    private String nameEnd;
    private String nameStart;

    public String getComment() {
	return comment;
    }

    public Date getDate() {
	return date;
    }

    public String getNameEnd() {
	return nameEnd;
    }

    public String getNameStart() {
	return nameStart;
    }

    public void setComment(final String comment) {
	this.comment = comment;
    }

    public void setDate(final Date date) {
	this.date = date;
    }

    public void setNameEnd(final String nameEnd) {
	this.nameEnd = nameEnd;
    }

    public void setNameStart(final String nameStart) {
	this.nameStart = nameStart;
    }
}
