package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.cache.*;
import play.data.validation.*;
import play.Logger;

import java.util.*;

import models.*;

public class Application extends Controller {

    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
        List<Post> olderPosts = Post.find(
                "order by postedAt desc"
        ).from(1).fetch(10);
        render(frontPost,olderPosts);
        render();
    }

    public static void show(Long id) {
        Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(post, randomID);
    }

    public static void postComment(
            Long postId,
            @Required(message="Author is required") String author,
            @Required(message="A message is required") String content,
            @Required(message="Please type the code") String code,
            String randomID)
    {
        Post post = Post.findById(postId);
        validation.equals(
                code, Cache.get(randomID)
        ).message("Invalid code. Plase type it again");
        if (validation.hasErrors()) {
            render("Application/show.html", post, randomID);
        }
        post.addComment(author, content);
        flash.success("Thanks for posting %s", author);
        show(postId);
    }

    public static void captcha(String id){
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void listTagged(String tag) {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }
    
}