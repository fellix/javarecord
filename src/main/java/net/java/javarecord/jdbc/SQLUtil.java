/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.jdbc;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Utilities for SQL code
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class SQLUtil {
    
    public static String verifyDataType(Object value) {
        StringBuilder sb = new StringBuilder();
        String use = "";
        if ((value instanceof String) || (value instanceof Date)) {
            sb.append("'");
            use = "'";
        }
        sb.append(value).append(use).append(", ");
        return sb.toString();
    }
    /**
     * Add a paramenter (object) in the index position to an prepared stament
     * @param smt the statment with the params
     * @param index the index of params
     * @param value the param value
     * @throws java.sql.SQLException
     */
    public static void addParameter(PreparedStatement smt, int index, Object value) throws SQLException{
        if(value instanceof String){
            smt.setString(index, value+"");
        }else if(value instanceof Integer){
            smt.setInt(index, (Integer) value);
        }else if(value instanceof Double){
            smt.setDouble(index, (Double) value);
        }else if(value instanceof Float){
            smt.setFloat(index, (Float) value);
        }else if(value instanceof BigDecimal){
            smt.setBigDecimal(index, (BigDecimal)value);
        }else if(value instanceof Date){
            smt.setDate(index, (java.sql.Date) value);
        }else if(value instanceof Long){
            smt.setLong(index, (Long) value);
        }else if( value instanceof Short){
            smt.setShort(index, (Short) value);
        }else if( value instanceof Blob){
            smt.setBlob(index, (Blob) value);
        }else if (value instanceof byte[]){
            smt.setBytes(index, (byte[])value);
        }else if( value instanceof Byte){
            smt.setByte(index, (Byte) value);
        }else if( value instanceof Clob){
            smt.setClob(index, (Clob) value);
        }else{
            smt.setObject(index, value);
        }
    }

}
