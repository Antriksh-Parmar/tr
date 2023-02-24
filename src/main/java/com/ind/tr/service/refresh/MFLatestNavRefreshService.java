package com.ind.tr.service.refresh;

import com.ind.tr.repository.model.utill.Nav;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Queries Latest Navs from AMFI sites
 */
@Component
public class MFLatestNavRefreshService {

    public void refresh() {
        String urlStr = "https://www.amfiindia.com/spages/NAVAll.txt";
        String regex = "^[^;]+;[^;]*;[^;]+;[^;]+;[^;]+;[^;]+$";

        try {
            URL url = new URL(urlStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Pattern pattern = Pattern.compile(regex);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String inputLine;
            List<String> lines = new ArrayList<>();
            while ((inputLine = in.readLine()) != null) {
                lines.add(inputLine);
            }
            in.close();

            List<Nav> navs = new ArrayList<>();
            for (String line : lines) {
                String[] tokens = line.split(";");
                if (pattern.matcher(line).matches() && isLatestNav(tokens, formatter)) {
                    if (!"-".equals(tokens[1])) {
                        Nav nav = new Nav(Integer.parseInt(tokens[0]), tokens[1], new BigDecimal(tokens[4]), Timestamp.valueOf(tokens[5]));
                        navs.add(nav);
                    }
                    if (!"-".equals(tokens[2])) {
                        Nav nav = new Nav(Integer.parseInt(tokens[0]), tokens[2], new BigDecimal(tokens[4]), Timestamp.valueOf(tokens[5]));
                        navs.add(nav);
                    }
                }
            }
            // TODO Write logic to store navs to the right database
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLatestNav(String[] tokens, SimpleDateFormat formatter) {
        try {
            if (tokens.length < 6) {
                return false;
            }
            String dateString = tokens[5];
            Date date = formatter.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendar.add(Calendar.DATE, -1);
            Date yesterday = calendar.getTime();
            return yesterday.equals(date);
        } catch (ParseException e) {
            return false;
        }
    }

}