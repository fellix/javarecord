/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord;

import net.java.javarecord.annotations.HasMany;

/**
 *
 * @author Rafael Felix
 * @version 1.0
 */
@HasMany(classes={Comment.class})
public class Post extends JavaRecord {

}
