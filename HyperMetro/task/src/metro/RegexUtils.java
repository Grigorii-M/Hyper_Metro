package metro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private static final String nameCharactersRegex = "[\\w-'.&\\s]";

    public static final String nameInCommandRegex = "([\\w\\-'.&]+|\"[\\w\\-'.&\\s]+\")";

    public static final String transferStationRegex = "(?<transferInfo>\\s*\\{\\s*(?<transferLineField>\"line\":\\s*\"(?<transferLineName>"
            + nameCharactersRegex + "+)\"),\\s*(?<transferStationField>\"station\":\\s*\"(?<transferStationName>"
            + nameCharactersRegex + "+)\")\\s*},?)";

    public static final String metroStationRegex = "(?<station>\\{\\s*\"name\":\\s*\"(?<stationName>" + nameCharactersRegex
            + "+)\",\\s*(?<prevField>\"prev\":\\s*\\[\\s*(\"("
            + nameCharactersRegex + "+)\",?\\s*)*]),\\s*(?<nextField>\"next\":\\s*\\[\\s*((\""
            + nameCharactersRegex + "+)\",?\\s*)*]),\\s*(?<transferField>\"transfer\":\\s*\\["
            + transferStationRegex + "*\\s*]),?\\s*(?<timeField>\"time\":\\s*(?<timeInfo>\\d+))?\\s*},?\\s*)";

    public static final String metroLineRegex = "(?<line>\"(?<lineName>" + nameCharactersRegex + "+)\":\\s*\\[\\s*" + metroStationRegex + "+],?)";
}
