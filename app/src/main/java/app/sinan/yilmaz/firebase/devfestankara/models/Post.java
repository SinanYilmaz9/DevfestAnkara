package app.sinan.yilmaz.firebase.devfestankara.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String title;
    public String body;
    public String imageUrl;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {

    }

    public Post(String uid, String author, String title, String body, String imageUrl) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("imageUrl", imageUrl);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
