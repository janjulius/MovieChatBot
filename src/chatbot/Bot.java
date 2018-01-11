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
import com.google.api.services.youtube.*;

import com.rivescript.RiveScript;

import mariadb.Mariadb;

import org.telegram.telegrambots.api.methods.GetMe;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import youtube.Search;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

        if(Settings.DEBUG_MODE) {
            Mariadb j = new Mariadb();
            String s = j.call(bot, new String[]{Settings.DB_HOST, Settings.DB_PORT, Settings.DB, Settings.USER_NAME, Settings.PASSWORD,
                    "SELECT Name FROM TestPersonTable"});
            System.out.println(s);
        }
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
            } else if(message_text.toLowerCase().startsWith("give video")){
                SendVideoMessage(reply, chat_id);
            }
            else {
                SendRegularMessage(reply, chat_id);
            }


        }
    }

    public void Say(String msg, Long cid){
        if(Settings.DEBUG_MODE)
            System.out.println("Replied to chat: " + cid + " With message: " + msg);

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

    public void SendPhotoMessage(String usermsg, Long cid){



        SendPhoto message = new SendPhoto()
                .setChatId(cid)
                //.setNewPhoto()
                ;
    }

    /**
     * sends a regular message on receiving an update
     * @param usermsg message sent by the user depends on brian.rive
     * @param cid chat id
     */
    public void SendRegularMessage(String usermsg, Long cid){
        Say(usermsg, cid);
    }

    /**
     * sends a video message on receiving update
     * @param usermsg
     * @param cid
     */
    public void SendVideoMessage(String usermsg, Long cid){
        youtube.Search yts = new youtube.Search();
        Say(yts.Search(usermsg), cid);
    }

    /**
     * send a location message to the user
     * @param usermsg message sent by the user depends on brain.rive
     * @param cid chat id
     */
    public void SendLocationMessage(String usermsg, Long cid) throws InterruptedException, ApiException, IOException {

        //TODO change to singleton
        GeoApiContext context = new GeoApiContext.Builder() //creates a geoapicontext needed for using google maps api
                .apiKey(Settings.GOOGLE_MAPS_API_TOKEN) //sets the key to token
                .build(); //builds context

        GeocodingResult[] results = GeocodingApi.geocode( //create an array of geocoding results
                context, //param conext
                usermsg) //param message
                .await(); //wait till all results are collected

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //not needed but why not
        if(results.length == 0){ //null check if the message is empty
            Say("https://en.wikipedia.org/wiki/Nothing", cid);
            return;
        }

        if(Settings.DEBUG_MODE)
            System.out.println(gson.toJson(results[0].addressComponents));

        Say("I have found " + results.length + "results, here they are", cid);

        for(int i = 0; i < results.length; i++) {
            SendLocation message = new SendLocation() // Create a locations message object
                    .setChatId(cid) //set chat id
                    .setLatitude((float) results[0].geometry.location.lat) //set latitude
                    .setLongitude((float) results[0].geometry.location.lng); //set longitude

            try {
                if (message != null)
                    execute(message); // Sending our message object to user

                //if (Settings.DEBUG_MODE)
                    //execute(new SendMessage().setChatId(cid).setText("ur msg was " + usermsg));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
