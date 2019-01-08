public class ForecastRecord {

    private String location;
    private String forecastTarget;
    private ForecastUnit forecastUnit;
    private String binStartInclusive;
    private String binEndNotInclusive;
    private String value;
    private ForecastType type;

    public ForecastType getType() {
        return type;
    }

    public void setType(ForecastType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getForecastTarget() {
        return forecastTarget;
    }

    public void setForecastTarget(String forecastTarget) {
        this.forecastTarget = forecastTarget;
    }

    public ForecastUnit getForecastUnit() {
        return forecastUnit;
    }

    public void setForecastUnit(ForecastUnit forecastUnit) {
        this.forecastUnit = forecastUnit;
    }

    public String getBinStartInclusive() {
        return binStartInclusive;
    }

    public void setBinStartInclusive(String binStartInclusive) {
        this.binStartInclusive = binStartInclusive;
    }

    public String getBinEndNotInclusive() {
        return binEndNotInclusive;
    }

    public void setBinEndNotInclusive(String binEndNotInclusive) {
        this.binEndNotInclusive = binEndNotInclusive;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
