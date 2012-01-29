package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.applets.utils.db.NewsDbAdapter;

public class News extends Model {

    /**
     * Parcel Creator
     */
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
	@Override
	public News createFromParcel(final Parcel in) {
	    return new News(in);
	}

	@Override
	public News[] newArray(final int size) {
	    return new News[size];
	}
    };
    private String creator = "";
    private String description = "";
    private int feed_id;
    private String image = "";
    private String name = "";
    private int news_id;
    private String pubDate = "";

    private String url = "";

    public News() {
    }

    public News(final long _id, final String name, final String url,
	    final String description, final String image, final String creator,
	    final int news_id, final int feed_id, final String pubDate) {
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
    public News(final Parcel in) {
	_id = in.readLong();
	news_id = in.readInt();
	feed_id = in.readInt();
	name = in.readString();
	description = in.readString();
	url = in.readString();
	image = in.readString();
	creator = in.readString();
	pubDate = in.readString();

    }

    @Override
    public int compareTo(final Model another) {
	final News art = (News) another;
	int result = 0;

	if (!art.url.equals(url)) {
	    result = -1;
	}

	return result;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    public String getCreator() {
	return creator;
    }

    /**
     * Returns the description of the news
     */
    public String getDescription() {
	return description;
    }

    public int getFeedId() {
	return feed_id;
    }

    public String getImage() {
	return image;
    }

    public String getName() {
	return name;
    }

    private int getNewsId() {
	return news_id;
    }

    public String getPubDate() {
	return pubDate;
    }

    public String getUrl() {
	return url;
    }

    public void setCreator(final String creator) {
	this.creator = creator;
    }

    public void setDescription(final String description) {
	this.description = description;
    }

    public void setFeedId(final String feed_id) {
	this.feed_id = Integer.parseInt(feed_id);

    }

    public void setImage(final String image) {
	this.image = image;
    }

    public void setNewsId(final String id) {
	news_id = Integer.parseInt(id);

    }

    public void setPubDate(final String pubDate) {
	this.pubDate = pubDate;

    }

    public void setTitle(final String title) {
	name = title;
    }

    public void setUrl(final String link) {
	url = link;
    }

    @Override
    public ContentValues setValues() {
	_values.put(NewsDbAdapter.KEY_NAME, getName());
	_values.put(NewsDbAdapter.KEY_URL, getUrl());
	_values.put(NewsDbAdapter.KEY_DESCRIPTION, getDescription());
	_values.put(NewsDbAdapter.KEY_IMAGE, getImage());
	_values.put(NewsDbAdapter.KEY_CREATOR, getCreator());
	_values.put(NewsDbAdapter.KEY_PUB_DATE, getPubDate());
	_values.put(NewsDbAdapter.KEY_NEWS_ID, getNewsId());
	_values.put(NewsDbAdapter.KEY_FEED_ID, getFeedId());

	return _values;
    }

    @Override
    public String toString() {
	return getName();
    }

    /**
     * Order matters
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
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

}
