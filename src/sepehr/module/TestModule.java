package sepehr.module;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.StringResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sepehr on 10/17/2017.
 */
public class TestModule extends Module {

    public TestModule(TelegramBot bot) {
        super(bot);
    }

    private Map<Integer, Long> ideaUser = new HashMap<>();

    @Override
    public Boolean parse(Update u) {

        Message message = u.message();
        //Long chatID = u.message().chat().id();

        // Check for callback queries
        if (!(message != null && message.text() != null)) {
            if (u.callbackQuery() != null && u.callbackQuery().data() != null && u.callbackQuery().message() != null) {
                System.out.println("Log --- callback");
                if (u.callbackQuery().data().equalsIgnoreCase(Codes.STUDENT_NO)) {
                    System.out.println("Log --- not student");
                    notStudent(u.callbackQuery().from().id());
                } else if (u.callbackQuery().data().equalsIgnoreCase(Codes.STUDENT_YES)) {
                    System.out.println("Log --- student");
                    student(u.callbackQuery().from().id());
                } else if (u.callbackQuery().data().equalsIgnoreCase(Codes.SELECT_UNIVERSITY)) {
                    sendPreview(u.callbackQuery().from().id());
                }

                else {
                    System.out.println("Log --- no data");
                }
            }
        }
        // Check for messages
        else if (message != null && message.text() != null) {

            System.out.println("Log --- " + u.message().text());

            // Receive start command
            if (message.text().equalsIgnoreCase(Codes.START)) {
                start(message.chat().id());
            } else {
                System.out.println("Log --- no message");
            }

        }


        return null;
    }

    private void start(long chatID) {
        InlineKeyboardButton ikbs[][] = new InlineKeyboardButton[1][2];
        ikbs[0][0] = new InlineKeyboardButton("نه")
                .callbackData("student-no");
        ikbs[0][1] = new InlineKeyboardButton("اره")
                .callbackData("student-yes");

        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(ikbs);

        SendMessage sendMessage = new SendMessage(chatID, "دانشجو هستی؟");
        sendMessage.replyMarkup(ikm);

        bot.execute(sendMessage);
    }

    private void notStudent(long chatID) {

        SendMessage sendMessage = new SendMessage(chatID, "نیم عمرت برفنا برو تلاش کن دانشجو کرمانشاه بشی");
        bot.execute(sendMessage);
    }

    private void student(long chatID) {

        SendMessage sendMessage = new SendMessage(chatID, "دانشجو کدام دانشگاه هستی ؟");
        InlineKeyboardButton ikbs[][] = new InlineKeyboardButton[1][4];
        ikbs[0][0] = new InlineKeyboardButton("رازی")
                .callbackData("university");
        ikbs[0][1] = new InlineKeyboardButton("صنعتی")
                .callbackData("university");
        ikbs[0][2] = new InlineKeyboardButton("آزاد")
                .callbackData("university");
        ikbs[0][3] = new InlineKeyboardButton("سایر")
                .callbackData("university");

        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(ikbs);

        sendMessage.replyMarkup(ikm);

        bot.execute(sendMessage);

    }

    private void sendPreview (long chatID) {

        int randomIndex = (int) (Math.random() * 19 + 1);
        System.out.println("Log --- random index = " + randomIndex);

        ideaUser.put(randomIndex, chatID);

        String idea = "";

        for (Map.Entry<String, Integer> entry : Codes.idea.entrySet()) {
            if (entry.getValue() == randomIndex) {
                idea = entry.getKey();
            }
        }

        String message = "از نظر استاد تو یه " + idea + " هستی";
        SendMessage sendMessage = new SendMessage(chatID, message);

        InlineKeyboardButton ikbs[][] = new InlineKeyboardButton[1][2];
        ikbs[0][0] = new InlineKeyboardButton("ورود به کانال")
                .url("https://t.me/joinchat/AAAAAD_L3Q7qVJGc8HX4Tg");
        ikbs[0][1] = new InlineKeyboardButton("مشاهده فال کامل")
                .callbackData("full-image");
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(ikbs);
        sendMessage.replyMarkup(ikm);

        bot.execute(sendMessage);
    }

    private void sendFullImage (long chatID, int previewIndex) {


    }
}
