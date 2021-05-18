package zad2;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyWindow extends Application {
    public void launchWindow(){
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Service service = new Service("Poland");
        String weatherJson = convertJsonWeather(service.getWeather("Warsaw"));
        Double rate1 = service.getRateFor("EUR");
        Double rate2 = service.getNBPRate();

        SplitPane root = new SplitPane();

        //LEFT PANEL
        Pane left = new Pane();
        SplitPane.setResizableWithParent(left, false);

        Text weather = new Text("Weather: ");
        weather.setLayoutX(15);
        weather.setLayoutY(20);
        weather.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        Text weatherInfo = new Text(weatherJson);
        weatherInfo.setLayoutX(20);
        weatherInfo.setLayoutY(40);
        weatherInfo.setWrappingWidth(170);

        Text rate1Text = new Text("Exchange rate(EUR): ");
        rate1Text.setLayoutX(15);
        rate1Text.setLayoutY(110);
        rate1Text.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        Text rate1Info = new Text();
        rate1Info.setLayoutX(20);
        rate1Info.setLayoutY(130);
        rate1Info.setText(String.valueOf(rate1));

        Text rate2Text = new Text("NBP exchange rate: ");
        rate2Text.setLayoutX(15);
        rate2Text.setLayoutY(170);
        rate2Text.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
        Text rate2Info = new Text();
        rate2Info.setLayoutX(20);
        rate2Info.setLayoutY(190);
        rate2Info.setText(String.valueOf(rate2));

        Text country = new Text("Country:");
        country.setLayoutX(15);
        country.setLayoutY(525);
        TextField countryGetter = new TextField();
        countryGetter.setLayoutX(15);
        countryGetter.setLayoutY(530);

        Text city = new Text("City:");
        city.setLayoutX(15);
        city.setLayoutY(570);
        TextField cityGetter = new TextField();
        cityGetter.setLayoutX(15);
        cityGetter.setLayoutY(575);

        Text currency = new Text("Currency");
        currency.setLayoutX(15);
        currency.setLayoutY(615);
        TextField currencyGetter = new TextField();
        currencyGetter.setLayoutX(15);
        currencyGetter.setLayoutY(620);

        //BROWSER PANEL
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        String url  = "http://javatongue.blogspot.com";
        //String url  = "https://en.wikipedia.org/wiki/Warsaw"; //unexpected crash after the page is loaded
        webEngine.load(url);

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(countryGetter.textProperty(),
                        cityGetter.textProperty(),
                        currencyGetter.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (countryGetter.getText().isEmpty()
                        || cityGetter.getText().isEmpty()
                        || currencyGetter.getText().isEmpty());
            }
        };
        //REFRESH BUTTON
        Button refresh = new Button();
        refresh.disableProperty().bind(bb);
        refresh.setText("REFRESH");
        refresh.setLayoutX(15);
        refresh.setLayoutY(650);
        refresh.setPrefSize(170, 50);
        refresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String w = "";
                Double r1 = -1.0;
                Double r2 = -1.0;
                String wiki = "";
                boolean succes = false;
                try{
                    Service s = new Service(countryGetter.getText());
                    w = convertJsonWeather(s.getWeather(cityGetter.getText()));
                    r1 = s.getRateFor(currencyGetter.getText());
                    r2 = s.getNBPRate();
                    wiki = s.getWikiUrl();
                    succes = true;
                }catch(Exception exc){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid data!", ButtonType.OK);
                    alert.showAndWait();
                }
                if(succes) {
                    weatherInfo.setText(w);
                    rate1Info.setText(String.valueOf(r1));
                    rate2Info.setText(String.valueOf(r2));
                    webEngine.load(wiki);
                }

            }
        });

        left.getChildren().add(weather);
        left.getChildren().add(weatherInfo);
        left.getChildren().add(rate1Text);
        left.getChildren().add(rate1Info);
        left.getChildren().add(rate2Text);
        left.getChildren().add(rate2Info);
        left.getChildren().add(country);
        left.getChildren().add(countryGetter);
        left.getChildren().add(city);
        left.getChildren().add(cityGetter);
        left.getChildren().add(currency);
        left.getChildren().add(currencyGetter);
        left.getChildren().add(refresh);
        left.setMinWidth(200);
        left.setMaxWidth(200);

        root.getItems().addAll(left, browser);

        Scene scene = new Scene(root, 1280,720);

        primaryStage.setResizable(false);
        primaryStage.setTitle("City Info");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String convertJsonWeather(String weatherJson)throws Exception{
        JSONObject jsonWeather = new JSONObject(weatherJson).getJSONObject("main");
        double temp = BigDecimal.valueOf(Double.parseDouble(jsonWeather.get("temp").toString()) - 273.15).setScale(1, RoundingMode.HALF_UP).doubleValue();
        String output = "Temerature: " + temp + "\n" +
                "Humidity: " + jsonWeather.get("humidity") + "%\n" +
                "Pressure: " + jsonWeather.get("pressure");
        return output;
    }
}
