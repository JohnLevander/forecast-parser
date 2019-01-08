import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForecastSubmissionParser {

    //File directory = new File("/Users/jdl50/dev/Hospitalization-Forecasts/2017-2018");

    public static Logger logger = LoggerFactory.getLogger(ForecastSubmissionParser.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, Forecaster> forecasters = new HashMap<>();


    public ForecastSubmissionParser() {

    }

    public static void main(String[] args) throws ParseException, IOException {
        ForecastSubmissionParser forecastSubmissionParser = new ForecastSubmissionParser();
        //forecastSubmissionParser.parseDirectory(new File("/Users/jdl50/dev/Hospitalization-Forecasts/2017-2018"));
        List<Forecaster> forecasters = new ArrayList<>();
        forecastSubmissionParser.parseDirectory("influenza", "2018-2019", new File("/Users/jdl50/dev/FluSight-forecasts/2018-2019"));
        logger.debug(Integer.toString((forecasters.size())));
    }

    private boolean isRowEmpty(CSVRecord csvRecord, Set<String> headers) {
        boolean thereIsAValueInTheLine = false;
        Iterator<String> it = headers.iterator();
        while (it.hasNext()) {
            String column = it.next();
            String value = csvRecord.get(column);
            thereIsAValueInTheLine = thereIsAValueInTheLine || !value.isEmpty();
        }
        return thereIsAValueInTheLine;
    }

    /***
     * We assume the file is going to be processed, we strip away the bad records.
     * @param file
     * @param forecastSubmission
     * @throws IOException
     */
    public void readFile(File file, ForecastSubmission forecastSubmission) throws IOException {
        CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withIgnoreEmptyLines().withTrim());
        for (CSVRecord csvRecord : parser) {
            boolean badRecord = false;
            boolean thereIsAValueInTheLine = isRowEmpty(csvRecord, parser.getHeaderMap().keySet());
            String loggerErrorPrefix = "Error in file: " + file.getAbsolutePath() + ".  Error was:  ";
            if (thereIsAValueInTheLine) {
                ForecastRecord forecastRecord = new ForecastRecord();

                String location = csvRecord.get("location");
                if (!location.isEmpty()) {
                    forecastRecord.setLocation(location);
                } else {
                    logger.error(loggerErrorPrefix + "location is empty");
                    badRecord = true;
                }

                String target = csvRecord.get("target");
                if (!target.isEmpty()) {
                    forecastRecord.setForecastTarget(target);
                } else {
                    logger.error(loggerErrorPrefix + "target is empty");
                    badRecord = true;
                }


                String value = csvRecord.get("value");
                if (!value.isEmpty()) {
                    forecastRecord.setValue(value);
                } else {
                    logger.error(loggerErrorPrefix + "value is empty");
                    badRecord = true;
                }

                if (!csvRecord.get("unit").isEmpty()) {
                    forecastRecord.setForecastUnit(ForecastUnit.valueOf(csvRecord.get("unit").toUpperCase()));
                } else {
                    logger.error(loggerErrorPrefix + " - This record has no unit!");
                    badRecord = true;
                }

                if (!csvRecord.get("type").isEmpty()) {
                    forecastRecord.setType(ForecastType.valueOf(csvRecord.get("type").toUpperCase()));
                } else {
                    logger.error(loggerErrorPrefix + " - This record has no type!");
                    badRecord = true;
                }

                if (forecastRecord.getType() == ForecastType.BIN) {
                    String binStartIncl = csvRecord.get("bin_start_incl");
                    if (!binStartIncl.isEmpty()) {
                        forecastRecord.setBinStartInclusive(binStartIncl);
                    } else {
                        logger.error(loggerErrorPrefix + "bin_start_incl is empty in record:" + csvRecord.toString());
                        badRecord = true;
                    }

                    String binStartNotIncl = csvRecord.get("bin_end_notincl");
                    if (!binStartNotIncl.isEmpty()) {
                        forecastRecord.setBinEndNotInclusive(binStartNotIncl);
                    } else {
                        logger.error(loggerErrorPrefix + "bin_end_notincl is empty");
                        badRecord = true;
                    }
                }
                forecastSubmission.getForecastRecordList().add(forecastRecord);
            } else {
                logger.debug("Warning: Skipping empty record: {}", csvRecord.toString());
            }

            if (!badRecord) {
                //add record to master list
            }
        } //record processing loop

    }

    public boolean parseFile(Forecaster forecaster, File file) throws IOException {
        if (file.isFile()) {
            String filename = file.getName();
            String epiWeek = filename.substring(2, 4);
            Integer dateEnd = filename.indexOf(".csv");
            Integer dateBegin = dateEnd - 10;
            String submissionDate = filename.substring(dateBegin, dateEnd);
            //we use 5 and dateBegin-1 to remove the separators between the week and algorithm name,
            //and the separators between the algorithm name and the date
            String algorithm = filename.substring(5, dateBegin - 1);

            ForecastSubmission forecastSubmission = new ForecastSubmission();
            forecastSubmission.setMmwrWeek(Integer.valueOf(epiWeek));
            try {
                forecastSubmission.setDateOfForecastSubmission(sdf.parse(submissionDate));
            } catch (ParseException e) {
                logger.error("Invalid submission date:" + submissionDate + " -- " + file.getName());
                //parsing not successful
                return false;
            }
            forecastSubmission.setReportedAlgorithmName(algorithm);
            forecaster.getForecastSubmissions().add(forecastSubmission);
            logger.debug("Algorithm: " + forecaster.getForecasterName() + "\tEpiWeek: " + epiWeek + "\tSubmission Date: " + submissionDate);
            readFile(file, forecastSubmission);

        }
        //parsing was successful
        return true;
    }

    public void parseDirectory(String disease, String season, File directory) throws ParseException, IOException {
        //each forecaster has multiple files
        logger.debug("Processing directory: {}, season: {}, disease: {}", directory, season, disease);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                parseDirectory(disease, season, file);
            }

            String forecasterKey = directory.getAbsolutePath() + " " + disease + " " + season;
            Forecaster forecaster = forecasters.get(forecasterKey);
            if (forecaster == null) {
                forecaster = new Forecaster();
                forecaster.setForecasterName(directory.getName());
                forecaster.setDisease(disease);
                forecaster.setSeason(season);
                forecasters.put(forecasterKey, forecaster);
            }
            parseFile(forecaster, file);
        }
    }
}
