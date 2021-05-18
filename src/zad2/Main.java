/**
 *
 * @author Dylewski Mateusz s18737
 *
 */

package zad2;

public class Main {



    public static void main(String[] args){
        try {
            Service s = new Service("Poland");
            String weatherJson = s.getWeather("Warsaw");
            Double rate1 = s.getRateFor("EUR");
            Double rate2 = s.getNBPRate();
            System.out.println(weatherJson);
            System.out.println(rate1);
            System.out.println(rate2);
            System.out.println(s.getWikiUrl());
        } catch (Exception ex){
            System.out.println("Invalid start data" + ex);
        }
        // ...
        // część uruchamiająca GUI
        MyWindow window = new MyWindow();
        window.launchWindow();
    }
}
