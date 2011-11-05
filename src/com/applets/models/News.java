package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.applets.utils.db.NewsDbAdapter;

public class News extends Model {

    private String name = "";
    private String url = "";
    private String description = "";
    private String image = "";
    private String creator = "";
    private int feed_id;
    private String pubDate = "";
    private int news_id;

    public News() {
    }

    public News(long _id, String name, String url, String description,
	    String image, String creator, int news_id, int feed_id,
	    String pubDate) {
	super();
	this._id = _id;
	this.name = name;
	this.url = url;
	this.description = description;
	this.image = image;
	this.creator = creator;
	this.news_id = news_id;
	this.feed_id = feed_id;
	this.pubDate = pubDate;
    }

    /**
     * Order matters
     * 
     * @param in
     */
    public News(Parcel in) {
	this._id = in.readLong();
	this.news_id = in.readInt();
	this.feed_id = in.readInt();
	this.name = in.readString();
	this.description = in.readString();
	this.url = in.readString();
	this.image = in.readString();
	this.creator = in.readString();
	this.pubDate = in.readString();

    }

    @Override
    public String toString() {
	return getName();
    }

    @Override
    public int compareTo(Model another) {
	final News art = (News) another;
	int result = 0;

	if (!art.url.equals(this.url)) {
	    result = -1;
	}

	return result;
    }

    public void setTitle(String title) {
	this.name = title;
    }

    public void setUrl(String link) {
	this.url = link;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public String getImage() {
	return image;
    }

    public String getUrl() {
	return url;
    }

    public String getName() {
	return name;
    }

    /**
     * Returns the description of the news
     */
    public String getDescription() {
	return description;
    }

    public String getCreator() {
	return creator;
    }

    public String getPubDate() {
	return pubDate;
    }

    public int getFeedId() {
	return feed_id;
    }

    @Override
    public ContentValues setValues() {
	this._values.put(NewsDbAdapter.KEY_NAME, getName());
	this._values.put(NewsDbAdapter.KEY_URL, getUrl());
	this._values.put(NewsDbAdapter.KEY_DESCRIPTION, getDescription());
	this._values.put(NewsDbAdapter.KEY_IMAGE, getImage());
	this._values.put(NewsDbAdapter.KEY_CREATOR, getCreator());
	this._values.put(NewsDbAdapter.KEY_PUB_DATE, getPubDate());
	this._values.put(NewsDbAdapter.KEY_NEWS_ID, getNewsId());
	this._values.put(NewsDbAdapter.KEY_FEED_ID, getFeedId());

	return this._values;
    }

    private int getNewsId() {
	return news_id;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    /**
     * Order matters
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeLong(_id);
	dest.writeInt(news_id);
	dest.writeInt(feed_id);
	dest.writeString(name);
	dest.writeString(description);
	dest.writeString(url);
	dest.writeString(image);
	dest.writeString(creator);
	dest.writeString(pubDate);

    }

    public void setCreator(String creator) {
	this.creator = creator;
    }

    public void setFeedId(String feed_id) {
	this.feed_id = Integer.parseInt(feed_id);

    }

    public void setPubDate(String pubDate) {
	this.pubDate = pubDate;

    }

    public void setNewsId(String id) {
	this.news_id = Integer.parseInt(id);

    }

    /**
     * Parcel Creator
     */
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
	@Override
	public News createFromParcel(Parcel in) {
	    return new News(in);
	}

	@Override
	public News[] newArray(int size) {
	    return new News[size];
	}
    };

}
