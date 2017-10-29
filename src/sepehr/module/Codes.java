package sepehr.module;

import sepehr.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Sepehr on 10/17/2017.
 */
public class Codes {

    // Commands
    public static String START = "/start";
    public static String STUDENT_NO = "student-no";
    public static String STUDENT_YES = "student-yes";
    public static String SELECT_UNIVERSITY = "university";
    public static String FULL_IMAGE = "full-image";

    public static Map<String, Integer> idea = new HashMap<>();

    public void initIdeaMap () {
        idea.put("خر خوان", 1);
        idea.put("اوشکول", 2);
        idea.put("باقالی", 3);
        idea.put("پاچه خوار", 4);
        idea.put("چاپلوس", 5);
        idea.put("حمال جزوه", 6);
        idea.put("خنگ", 7);
        idea.put("سرخوش", 8);
        idea.put("سوسول", 9);
        idea.put("شاسکول", 10);
        idea.put("گوسفند متکلم", 11);
        idea.put("گوشکوب", 12);
        idea.put("گیج سازمانی", 13);
        idea.put("گیج مادر زاد", 14);
        idea.put("مغز فندقی", 15);
        idea.put("منگ", 16);
        idea.put("هویج", 17);
        idea.put("وراج", 18);
        idea.put("یخ و ماست", 19);
    }

}
