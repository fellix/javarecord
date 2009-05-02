package net.java.javarecord;

import java.util.List;
import java.util.Properties;
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
        /*Post post = new Post();
        post.setAttribute("title", "JavaRecord v1.0-SNAPSHOT with params v1");
        post.setAttribute("body", "JavaRecord was rewrited, to improve performance, and for more facility!");
        System.out.println(post.getAttribute("id"));
        post.save();
        System.out.println(post.getAttribute("id"));
        post.setAttribute("title", "Substituido");
        post.save();//update*/
        /*
        List<Post> posts = JavaRecord.find(Post.class, null);
        print(posts.size());
        Post post = posts.get(0);
        post.setAttribute("title", "Titulo Trocado");
        post.save();*/

        Properties prop = new Properties();
        prop.put("id", 24);
        List<Post> posts = JavaRecord.find(Post.class, prop);
        print(posts.size());
        Post post = posts.get(0);
        print(post.getAttribute("id"));
        post.delete();
    }
    
    public static void print(Object o){
        System.out.println(o);
    }

}

