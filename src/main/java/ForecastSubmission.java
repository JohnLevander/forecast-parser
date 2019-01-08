import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastSubmission {
    private Integer mmwrWeek;
    private Date dateOfForecastSubmission;
    private String reportedAlgorithmName;
    private List<ForecastRecord> forecastRecordList = new ArrayList<>();

    public Integer getMmwrWeek() {
        return mmwrWeek;
    }

    public void setMmwrWeek(Integer mmwrWeek) {
        this.mmwrWeek = mmwrWeek;
    }

    public Date getDateOfForecastSubmission() {
        return dateOfForecastSubmission;
    }

    public void setDateOfForecastSubmission(Date dateOfForecastSubmission) {
        this.dateOfForecastSubmission = dateOfForecastSubmission;
    }

    public String getReportedAlgorithmName() {
        return reportedAlgorithmName;
    }

    public void setReportedAlgorithmName(String reportedAlgorithmName) {
        this.reportedAlgorithmName = reportedAlgorithmName;
    }

    public List<ForecastRecord> getForecastRecordList() {
        return forecastRecordList;
    }

    public void setForecastRecordList(List<ForecastRecord> forecastRecordList) {
        this.forecastRecordList = forecastRecordList;
    }
}
