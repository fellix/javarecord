package net.java.javarecord;

import net.java.javarecord.adapter.MySQLAdapter;
import net.java.javarecord.entities.Config;
import net.java.javarecord.registry.Registry;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Registry registry = Registry.getInstance();
        Config config = new Config("mysql", "localhost", "blog_jr", "root");
        registry.setConfig(config);
        registry.addAdapter(new MySQLAdapter());
        /*Post post = new Post();
        post.setAttribute("title", "JavaRecord v1.0-SNAPSHOT with params");
        post.setAttribute("body", "JavaRecord was rewrited, to improve performance, and for more facility!");
        post.save();*/
        Post post = Post.find(Post.class, 3);
        Comment c = Comment.find(Comment.class, 1);
        System.out.println(post.getAttribute("comments"));
        System.out.println(c.getAttribute("post"));
    }
}
