package pr.kandru.movieapp;

/**
 * Movie class which holds attributes to display in InfoActivity
 * Title, Poster is an image url, rating, MPAA-PG,PG-13,R, RunTime, Release, Description,
 * Cast is a ResultHolder holding Name,ID,and poster URL to a cast member
 * Similar is a ResultHolder holding Name,ID,and poster URL to a similar movie
 */
public class Movie extends InfoOverview {
    final private String title, poster, rating, mpaa, runtime, releaseDate;
    final private String overview, genres;
    final private ResultHolder cast;
    final private ResultHolder similar;

    public Movie(String title, String poster, String rating, String mpaa, String runtime, String releaseDate, String overview, String genres, ResultHolder cast, ResultHolder similar) {
        this.title = title;
        this.poster = poster;
        this.rating = rating;
        this.mpaa = mpaa;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.genres = genres;
        this.cast = cast;
        this.similar = similar;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getMpaa() {
        return mpaa;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getGenres() {
        return genres;
    }

    public ResultHolder getCast() {
        return cast;
    }

    public ResultHolder getSimilar() {
        return similar;
    }
}
