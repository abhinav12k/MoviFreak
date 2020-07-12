package com.apps.movifreak.Database;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Created by abhinav on 5/7/20.
 */
@Entity(tableName = "favMovie")
public class FavMovie implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long movieId;
    private String title;
    private String overview;  //summary
    private int vote_average;     //rating
    private String release_date;
    private String original_language;
    private boolean adult;
    private String poster_path;
    private long vote_count;
    private double popularity;
    private String backdrop;    //landscape picture
    private Date dateSaved;

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }


    @Ignore
    public FavMovie(String title, String overview, int vote_average, String release_date, boolean adult, String poster_path, long vote_count, double popularity, String backdrop,Date dateSaved,long movieId,String original_language) {
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.adult = adult;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.backdrop = backdrop;
        this.dateSaved = dateSaved;
        this.movieId = movieId;
        this.original_language = original_language;
    }

    public FavMovie(int id, String title, String overview, int vote_average, String release_date, boolean adult, String poster_path, long vote_count, double popularity, String backdrop, Date dateSaved,long movieId,String original_language) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.adult = adult;
        this.poster_path = poster_path;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.backdrop = backdrop;
        this.dateSaved = dateSaved;
        this.movieId = movieId;
        this.original_language = original_language;
    }

    protected FavMovie(Parcel in) {
        id = in.readInt();
        movieId = in.readLong();
        title = in.readString();
        overview = in.readString();
        vote_average = in.readInt();
        release_date = in.readString();
        adult = in.readByte() != 0;
        poster_path = in.readString();
        vote_count = in.readLong();
        popularity = in.readDouble();
        backdrop = in.readString();
        original_language = in.readString();
    }

    public static final Creator<FavMovie> CREATOR = new Creator<FavMovie>() {
        @Override
        public FavMovie createFromParcel(Parcel in) {
            return new FavMovie(in);
        }

        @Override
        public FavMovie[] newArray(int size) {
            return new FavMovie[size];
        }
    };

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public Date getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(movieId);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeInt(vote_average);
        dest.writeString(release_date);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(poster_path);
        dest.writeLong(vote_count);
        dest.writeDouble(popularity);
        dest.writeString(backdrop);
        dest.writeString(original_language);
    }

    @Override
    public String toString() {
        return "FavMovie{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", original_language='" + original_language + '\'' +
                ", adult=" + adult +
                ", poster_path='" + poster_path + '\'' +
                ", vote_count=" + vote_count +
                ", popularity=" + popularity +
                ", backdrop='" + backdrop + '\'' +
                ", dateSaved=" + dateSaved +
                '}';
    }
}
