/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import Util.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.rivescript.RiveScript;
import org.telegram.telegrambots.api.methods.GetMe;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

/**
 *
 * @author Lars Schipper
 */
public class Bot extends TelegramLongPollingBot {
    public String location_asking_string = "where is";

    RiveScript bot = new RiveScript();

    public Bot() {
        super();
        ContextSingleton c = new ContextSingleton();
        //bot.setSubroutine("system", new SystemSubroutine());
        //bot.setSubroutine("jdbc", new JdbcSubroutine());
        bot.setSubroutine("send", new SendSubroutine(this));
        bot.loadDirectory("resources/rivescript");
        bot.sortReplies();
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            // Get reply
            String reply = bot.reply(String.valueOf(chat_id), message_text);

            if(message_text.toLowerCase().startsWith(location_asking_string)){ //cheap solution for now :)
                try {
                    SendLocationMessage(reply, chat_id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                SendRegularMessage(reply, chat_id);
            }


        }
    }

    public void Say(String msg, Long cid){
        SendMessage message = new SendMessage() // Create a message object object
            .setChatId(cid)
            .setText(msg);
        if(message != null)
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    /**
     * sends a regular message on receiving an update
     * @param usermsg message sent by the user depends on brian.rive
     * @param cid chat id
     */
    public void SendRegularMessage(String usermsg, Long cid){
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(cid)
                .setText(usermsg);


        try {
            if(message != null)
                execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * send a location message to the user
     * @param usermsg message sent by the user depends on brain.rive
     * @param cid chat id
     */
    public void SendLocationMessage(String usermsg, Long cid) throws InterruptedException, ApiException, IOException {

        GeoApiContext context = new GeoApiContext.Builder() //TODO change to singleton
                .apiKey(Settings.GOOGLE_MAPS_API_TOKEN)
                .build();

        GeocodingResult[] results = GeocodingApi.geocode(
                context,
                usermsg).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(results.length == 0){
            Say("https://en.wikipedia.org/wiki/Nothing", cid);
            return;
        }
        if(Settings.DEBUG_MODE)
            System.out.println(gson.toJson(results[0].addressComponents));

        SendLocation message = new SendLocation() // Create a message object object
                .setChatId(cid)
                .setLatitude((float) results[0].geometry.location.lat)
                .setLongitude((float)results[0].geometry.location.lng);

        try {
            if(message != null)
                execute(message); // Sending our message object to user

            if(Settings.DEBUG_MODE)
                execute(new SendMessage().setChatId(cid).setText("ur msg was " + usermsg));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        return Settings.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return Settings.BOT_TOKEN;
    }

}
