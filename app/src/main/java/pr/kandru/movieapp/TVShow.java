package pr.kandru.movieapp;

/**
 * TV Show class which holds attributes to display in InfoActivity
 * Title, Poster is an image url, Run Time, First Aired, Genres, Network that it is aired,
 * Country of Origin, Number of Episodes, Number of Seasons, General Description,
 * Status - Airing/Canceled/Returning, Content Ratings - PG, TV-14 etc, Cast, and Similar TV Shows
 */
public class TVShow extends InfoOverview {
    private String title, poster, runTime, firstDate, genres, network;
    private String origin, numEpisodes, numSeason, overview, status, contentRatings;
    private ResultHolder cast;
    private ResultHolder similar;

    // Constructor
    public TVShow(String title, String poster, String runTime, String firstDate, String genres, String network, String origin, String numEpisodes, String numSeason, String overview, String status, String contentRatings, ResultHolder cast, ResultHolder similar) {
        this.title = title;
        this.poster = poster;
        this.runTime = runTime;
        this.firstDate = firstDate;
        this.genres = genres;
        this.network = network;
        this.origin = origin;
        this.numEpisodes = numEpisodes;
        this.numSeason = numSeason;
        this.overview = overview;
        this.status = status;
        this.contentRatings = contentRatings;
        this.cast = cast;
        this.similar = similar;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public String getGenres() {
        return genres;
    }

    public String getNetwork() {
        return network;
    }

    public String getOrigin() {
        return origin;
    }

    public String getNumEpisodes() { return numEpisodes; }

    public String getNumSeason() {
        return numSeason;
    }

    public String getOverview() {
        return overview;
    }

    public String getStatus() {
        return status;
    }

    public String getContentRatings() {
        return contentRatings;
    }

    public ResultHolder getCast() {
        return cast;
    }

    public ResultHolder getSimilar() {
        return similar;
    }
}
