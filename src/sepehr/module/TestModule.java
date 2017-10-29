package sepehr.module;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.StringResponse;
import com.sun.xml.internal.bind.v2.TODO;
import sepehr.ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sepehr on 10/17/2017.
 */
public class TestModule extends Module {

    public TestModule(TelegramBot bot) {
        super(bot);
    }

    private Map<Long, Integer> ideaUser = new HashMap<>();

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
                } else if (u.callbackQuery().data().equalsIgnoreCase(Codes.FULL_IMAGE)) {
                    editInlineKeyboard(u);
                    System.out.println(getPreviewIndex(u.callbackQuery().from().id()) + " LOOOL");
                    try {
                        sendFullImage(u.callbackQuery().from().id(),
                                getPreviewIndex(u.callbackQuery().from().id()), u.callbackQuery().from().id());
                    } catch (NullPointerException e) {

                        // TODO: No previous image
                        System.out.println("Log --- null");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (u.callbackQuery().data().equalsIgnoreCase(Codes.START)) {
                    start(u.callbackQuery().from().id());
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

        SendMessage sendMessage = new SendMessage(chatID, " آیا دانشجو کرمانشاه هستی؟");
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

        int randomIndex = getRandom(19);

        boolean contains = true;
        for (Map.Entry<Long, Integer> entry : ideaUser.entrySet()) {
            if (entry.getKey() == chatID) {
                entry.setValue(randomIndex);
                contains = false;
            }
        }

        if (contains) {
            ideaUser.put(chatID, randomIndex);
        }


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

    private Integer getPreviewIndex (long chatID) {

        Integer index = null;

        for (Map.Entry<Long, Integer> entry : ideaUser.entrySet()) {
            if (entry.getKey() == chatID) {
                 index = entry.getValue();
            }
        }

        return index;
    }

    private void sendFullImage (long chatID, Integer previewIndex, int userID) throws IOException {

        GetChatMember getChatMember = new GetChatMember("@asanbesan", userID);
        GetChatMemberResponse response = bot.execute(getChatMember);


        if (response != null
                && response.chatMember().status() != ChatMember.Status.kicked
                && response.chatMember().status() != ChatMember.Status.left
                && response.chatMember().status() != ChatMember.Status.restricted) {
            System.out.println(response);

            if (previewIndex != null) {
                System.out.println("Log --- index = " + previewIndex);

                ImageProcessing imageProcessing = new ImageProcessing();

                java.io.File template = new java.io.File("image/template.png");
                java.io.File ideaFile = new java.io.File("image/teacher/" + String.valueOf(previewIndex) + ".png");
                java.io.File dieFile = new java.io.File("image/die/" + String.valueOf(getRandom(16)) + ".png");
                java.io.File luckFile = new java.io.File("image/luck/" + String.valueOf(getRandom(12)) + ".png");
                java.io.File loveFile = new java.io.File("image/love/" + String.valueOf(getRandom(9)) + ".png");
                BufferedImage templateImage = imageProcessing.resolveTransparency(ImageIO.read(template));
                BufferedImage ideaImage = imageProcessing.resolveTransparency(ImageIO.read(ideaFile));
                BufferedImage dieImage = imageProcessing.resolveTransparency(ImageIO.read(dieFile));
                BufferedImage luckImage = imageProcessing.resolveTransparency(ImageIO.read(luckFile));
                BufferedImage loveImage = imageProcessing.resolveTransparency(ImageIO.read(loveFile));

                templateImage = imageProcessing.mapImage(templateImage, 410, 170, ideaImage);
                templateImage = imageProcessing.mapImage(templateImage, 270, 340, dieImage);
                templateImage = imageProcessing.mapImage(templateImage, 270, 460, luckImage);
                templateImage = imageProcessing.mapImage(templateImage, 270, 640, loveImage);

                SendPhoto sendPhoto = new SendPhoto(chatID, imageToByte(templateImage));
                InlineKeyboardButton ikbs[][] = new InlineKeyboardButton[1][1];
                ikbs[0][0] = new InlineKeyboardButton("فال جدید")
                        .callbackData("/start");
                InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(ikbs);
                sendPhoto.replyMarkup(ikm);
                bot.execute(sendPhoto);

                SendPhoto sendAdd = new SendPhoto(chatID, imageToByte(ImageIO.read(new java.io.File("image/add.png"))));
                sendAdd.caption("لینک اندروید آسان بسان\n" +
                        "https://goo.gl/F4qaCx\n" +
                        "لینک IOS آسان بسان\n" +
                        "http://goo.gl/X4xRSx");
                bot.execute(sendAdd);
            } else {
                System.out.println("Log -- no image");
            }
        } else {
            SendMessage sendMessage = new SendMessage(chatID, "برای مشاهده فال کامل باید عضو کانال شوید");
            bot.execute(sendMessage);
        }
    }

    private byte[] imageToByte (BufferedImage image) {

        byte[] imageInByte = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( image, "png", baos );
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageInByte;
    }

    private int getRandom (int min) {

        int randomIndex = (int) (Math.random() * min + 1);
        System.out.println("Log --- random index = " + randomIndex);
        return randomIndex;
    }

    private void editInlineKeyboard(Update u) {

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(u.callbackQuery().message().chat().id(),
                u.callbackQuery().message().messageId(), u.callbackQuery().message().text());


        InlineKeyboardButton ikbs[][] = new InlineKeyboardButton[1][1];

        ikbs[0][0] = new InlineKeyboardButton("ورود به کانال")
                .url("https://t.me/joinchat/AAAAAD_L3Q7qVJGc8HX4Tg");

        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(ikbs);
        editMessageReplyMarkup.replyMarkup(ikm);

        bot.execute(editMessageReplyMarkup);
    }

}
