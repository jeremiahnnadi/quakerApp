package org.nnadi.jeremiah.quakerapp;

/*
 - Name: Jeremiah Nnadi
 - StudentID: S1903336
*/

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    String title, description, location, link, pubDate, category;
    int id;
    double depth, magnitude, latitude, longitude;

    public Item() {
    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        link = in.readString();
        pubDate = in.readString();
        category = in.readString();
        depth = in.readDouble();
        magnitude = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    /**
     * Flatten the objects in to a Parcel.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(depth);
        dest.writeDouble(magnitude);
    }

    // Getters and Setters
    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * The Item toString() method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", category='" + category + '\'' +
                ", depth=" + depth +
                ", magnitude=" + magnitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
