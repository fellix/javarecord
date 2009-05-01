/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.inflector;

/**
 * This is an base inflector for the application
 * @author Rafael Felix
 * @version 1.0
 */
public class BaseInflector implements Inflector {

    private String case1[] = {"o", "ch", "sh", "ss", "x", "z"};
    private String sub1 = "es";
    private String case2 = "y";
    private String sub2 = "ies";
    private String case3[] = {"calf", "elf", "half", "knife", "leaf", "life",
        "loaf", "self", "sheaf", "shelf", "thief", "wife", "wolf"};
    private String sub3[] = {"calves", "elves", "halves", "knives", "leaves", "lives",
        "loaves", "selves", "sheaves", "shelves", "thieves", "wives", "wolves"};
    private String strPlural;

    /**
     * Default constructor
     * @since 1.0
     */
    public BaseInflector() {
    }

    @Override
    public String pluralize(String word) {
        if (isCase1(word)) {
            return strPlural;
        }
        if (isCase2(word)) {
            return strPlural;
        }
        if (isCase3(word)) {
            return strPlural;
        }
        strPlural = word + "s";
        return strPlural;
    }

    @Override
    public String singularize(String pluralWord) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Verifies if the string is in the case1
     * @param name the word to verify
     * @return true or false
     * @since 1.0
     */
    private boolean isCase1(String name) {
        if (name.length() > 0) {
            String p1 = name.trim().toLowerCase().substring(name.length() - 1, name.length());
            String p2 = name.trim().toLowerCase().substring(name.length() - 2, name.length());

            for (String s : case1) {
                if (p1.equalsIgnoreCase(s)) {
                    strPlural = name.substring(0, name.length() - 1);
                    strPlural = name + sub1;
                    return true;
                } else if (p2.equalsIgnoreCase(s)) {
                    strPlural = name.substring(0, name.length() - 2);
                    strPlural = name + sub1;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifies if the string is in the case2
     * @param name the word to verify
     * @return true or false
     * @since 1.0
     */
    private boolean isCase2(String name) {
        if (name.length() > 0) {
            String p1 = name.trim().toLowerCase().substring(name.length() - 1, name.length());
            if (p1.equalsIgnoreCase(case2)) {
                strPlural = name.substring(0, name.length() - 1);
                strPlural += sub2;
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the string is in the case3
     * @param name
     * @return true or false
     * @since 1.0
     */
    private boolean isCase3(String name) {
        for (int i = 0; i < case3.length; i++) {
            if (name.trim().equalsIgnoreCase(case3[i])) {
                strPlural = sub3[i];
                return true;
            }
        }
        return false;
    }
}
