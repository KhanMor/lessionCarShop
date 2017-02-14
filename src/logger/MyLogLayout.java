package logger;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;

/**
 * Created by Mordr on 14.02.2017.
 */
public class MyLogLayout extends PatternLayout {
    @Override
    public String format(LoggingEvent event) {
        String eventLevel = event.getLevel().toString();
        String eventMessage = event.getMessage().toString();
        Date eventDate = new Date(event.getTimeStamp());
        return eventLevel + " " + eventDate + " " + eventMessage;
    }
}
