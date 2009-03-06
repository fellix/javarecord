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
        Comment c = Comment.find(Comment.class, 1);
        //c.setAttribute("post", Post.find(Post.class, 3));
        //c.save();
        Post post = c.getAttribute("post");
        Post p1 = new Post();
        //p1.setAttribute("id", null);
        p1.setAttribute("title", "testing");
        p1.setAttribute("body", "finding the id");
        print(p1.getAttribute("id"));
        p1.save();
        print(post.getAttribute("id"));
        print(p1.getAttribute("id"));
        //System.out.println(c.getAttribute("post_id"));
        //Comment c1 = new Comment();
        //c1.setAttribute("body", "The commentary body.");
        //post.setAttribute("comments", c1);
        //post.save();
    }
    
    public static void print(Object o){
        System.out.println(o);
    }

}
