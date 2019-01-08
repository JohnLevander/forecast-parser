import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Forecaster {
    private String disease;
    private String forecasterName;
    private String season;
    private List<ForecastSubmission> forecastSubmissions = new ArrayList<>();

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getForecasterName() {
        return forecasterName;
    }

    public void setForecasterName(String forecasterName) {
        this.forecasterName = forecasterName;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<ForecastSubmission> getForecastSubmissions() {
        return forecastSubmissions;
    }

    public void setForecastSubmissions(List<ForecastSubmission> forecastSubmissions) {
        this.forecastSubmissions = forecastSubmissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forecaster that = (Forecaster) o;
        return Objects.equals(disease, that.disease) &&
                Objects.equals(forecasterName, that.forecasterName) &&
                Objects.equals(season, that.season);
    }

    @Override
    public int hashCode() {

        return Objects.hash(disease, forecasterName, season);
    }
}
