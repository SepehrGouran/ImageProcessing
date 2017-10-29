/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sepehr.module;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

/**
 *
 * @author Azhir
 */
 public abstract class Module {

    protected TelegramBot bot;

    public Module(TelegramBot bot) {
        this.bot = bot;
    }
    
    public abstract Boolean parse(Update u);
}
