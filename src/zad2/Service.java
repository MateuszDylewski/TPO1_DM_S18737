package zad2;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Service {
    String country;
    String countryCode;
    String currency;
    String city;
    Map<String, String> countries = new HashMap<>();


    public Service(String country){

        this.country = country;
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(Locale.UK), iso);
        }
        countryCode = countries.get(country);
        currency = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }

    private String getHtmlForUrl(String url) throws Exception{
        String html = "";

        URL oracle = new URL(url);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            html += inputLine;
        in.close();

        return html;
    }

    public String getWeather(String city) throws Exception{// mateusz.klosowski69@gmail.com/qweasd123
        this.city = city;
        String location = city + "," + countryCode;
        String html = getHtmlForUrl("http://api.openweathermap.org/data/2.5/weather?q="+
                location +
                "&appid=24ade944796ba4903ba31c2c7f0e4104");
        return html;
    }

    public double getRateFor(String currency) throws Exception{
        String html = getHtmlForUrl("https://api.exchangeratesapi.io/latest?base=" +
                this.currency +
                "&symbols=" +
                currency);

        Double rate = 0.0;

        JSONObject json = new JSONObject(html);
        rate = json.getJSONObject("rates").getDouble(currency);

        return rate;
    }

    public double getNBPRate() throws Exception{
        if(country.equals("Poland")) return 1.0;

        String html = getHtmlForUrl("http://www.nbp.pl/kursy/kursya.html");

        int currencyCodeIdx = html.indexOf(currency);
        int currencyRateIdx = html.indexOf(">", currencyCodeIdx + 14) + 1;
        Double currencyRate = Double.valueOf(
                html.substring(currencyRateIdx, currencyRateIdx + 6).replace(",", ".")
        );
        return currencyRate;
    }

    public String getWikiUrl(){

        return city == null ? "https://en.wikipedia.org/wiki/Main_Page" : "https://en.wikipedia.org/wiki/" + city;

    }
}