/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.java.javarecord.JavaRecord;
import net.java.javarecord.adapter.Adapter;
import net.java.javarecord.adapter.GeneratorType;
import net.java.javarecord.registry.Registry;

/**
 * Class for generate SQL code for comunicates the object to the database.</p>
 * Maintain the table MetaData
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class SqlForge {

    private ResultSetMetaData metaData;
    private String tableName;
    private Adapter adapter;
    private DatabaseManager database = DatabaseManager.getInstance();
    private Map<String, Object> attributes;
    private String primaryKey;

    /**
     * The default constructor
     * @param tableName the table name to load the data
     * @since 1.0
     */
    public SqlForge(String tableName) {
        this.tableName = tableName;
        adapter = Registry.getInstance().getAdapter();
        if (adapter.isUpperCase()) {
            this.tableName = this.tableName.toUpperCase();
        }
        try {
            loadAttributes();
            primaryKey = getPrimaryKeyColumn();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Load the table columns from the database
     * @throws java.sql.SQLException
     * @since 1.0
     * @see DatabaseManager#getPreparedStatement(java.lang.String)
     * @see Adapter#getLimiter() 
     */
    public void loadAttributes() throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName);
        if (adapter.getLimiter() != null) {
            sb.append(" ").append(adapter.getLimiter()).append(" 1");
        }
        PreparedStatement smt = database.getPreparedStatement(sb.toString());
        ResultSet rs = smt.executeQuery();
        metaData = rs.getMetaData();
        rs.close();
        smt.close();
        rs = null;
        smt = null;
    }

    /**
     * Get the Map containing the attributes, each value is null. this metodo use cache
     * @return attributes of this object
     * @throws java.sql.SQLException
     * @since 1.0
     * @see #reloadAttributes()
     */
    public Map<String, Object> getAttributes() throws SQLException {
        if (attributes != null) {
            return attributes;
        }
        if (metaData == null) {
            loadAttributes();
        }
        reloadAttributes();
        return attributes;
    }

    /**
     * Force the application to reload the object attributes
     * @return attributes of this object
     * @throws java.sql.SQLException
     * @see #getAttributes() 
     * @since 1.0
     */
    public Map<String, Object> reloadAttributes() throws SQLException {
        loadAttributes();
        attributes = new HashMap<String, Object>();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i + 1);
            attributes.put(columnLabel, null);
        }
        return attributes;
    }

    /**
     * Gets the Primary key of the table
     * @return columnName of the primary key
     * @since 1.0
     * @throws SQLException
     */
    private String getPrimaryKeyColumn() throws SQLException {
        ResultSet rs = database.getPrimaryKey(tableName);
        rs.next();
        return rs.getString(4);
    }

    /**
     * Define new attributes for the sql
     * @param attributes
     * @since 1.0
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the Insert SQL for the object
     * @param sequence the sequence name for the object, can be null
     * @return Sql code for insert object in the database
     * @throws java.sql.SQLException
     */
    private String getInsertSql(String sequence) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder values = new StringBuilder(" VALUES(");
        sb.append(tableName);
        sb.append("(");
        if (adapter.getGeneratorType().equals(GeneratorType.SEQUENCE)) {
            sb.append(getPrimaryKeyColumn()).append(", ");
            if (sequence == null) {
                throw new NullPointerException("Sequence can't be null!");
            }
            //values.append(seq.getInt(1));
            values.append("?, ");
        }
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            //Ignores the Primary Key
            if (!e.getKey().equals(primaryKey)) {
                Object value = e.getValue();
                //Ignores the Associantion at this moment
                if (!(value instanceof List) && !(value instanceof JavaRecord)) {
                    sb.append(e.getKey()).append(", ");
                    values.append("?, ");
                }
            }
        }
        removeLastComma(sb);
        removeLastComma(values);
        sb.append(")");
        values.append(")");
        sb.append(values);
        return sb.toString();
    }

    /**
     * Get the update SQL for update one row
     * @return sql to update this row
     * @throws java.sql.SQLException
     * @since 1.0
     */
    private String getUpdateSql() throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableName).append(" SET ");
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(primaryKey).append(" = ?");
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            String key = e.getKey();
            if (!key.equals(primaryKey)) {
                sb.append(key);
                sb.append(" = ");
                sb.append("?").append(", ");
            }
        }
        removeLastComma(sb);
        sb.append(where);
        return sb.toString();
    }
    /**
     * Make the database object update
     * @throws java.sql.SQLException
     * @since 1.0
     * @see #getUpdateSql()
     * @see SQLUtil#addParameter(java.sql.PreparedStatement, int, java.lang.Object) 
     */
    public void update() throws SQLException{
        PreparedStatement smt = database.getPreparedStatement(getUpdateSql());
        int index = 1;
        for(Map.Entry<String, Object> e : attributes.entrySet()){
            Object value = e.getValue();
            if (!(value instanceof List) && !(value instanceof JavaRecord) &&
                    !(e.getKey().equals(primaryKey))) {
                SQLUtil.addParameter(smt, index, value);
                index++;
            }
        }
        //Adds the primary key paramter
        smt.setInt(index, (Integer) attributes.get(getPrimaryKeyColumn()));
        smt.execute();
        smt.close();
        smt = null;
    }

    /**
     * Get a SELECT to the database using the id for filter
     * @param id
     * @return SQL for select in the database
     * @throws java.sql.SQLException
     * @since 1.0
     */
    private String getSelectById() throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName).append(" WHERE ");
        sb.append(getPrimaryKeyColumn()).append(" = ?");//.append(id);
        return sb.toString();
    }
    /**
     * Make an select by id in the database
     * @param id to search
     * @return the found resultset
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public ResultSet selectById(int id) throws SQLException{
        PreparedStatement smt = database.getPreparedStatement(getSelectById());
        smt.setInt(1, id);
        return smt.executeQuery();
    }
    /**
     * Make the Select to load an collection attribute
     * @param table the table to search
     * @param foreignKey the Foreign key to find
     * @return SQL String
     * @since 1.0
     */
    private String getSelectCollection(String table, String foreignKey){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(table);
        sb.append(" WHERE ");
        sb.append(table).append(".").append(foreignKey).append("_id = ?");
        return sb.toString();
    }
    /**
     * Make the SQL in the database to find the object
     * @param table to find the object
     * @param id of entity
     * @param foreignKey the key to search
     * @return the Result of the SQL
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public ResultSet selectCollection(String table, Integer id, String foreignKey) throws SQLException{
        if(id == null){
            return null;
        }
        PreparedStatement smt = database.getPreparedStatement(getSelectCollection(table, foreignKey));
        smt.setInt(1, id);
        return smt.executeQuery();
    }

    private String getObjectSql(String fatherTable){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(fatherTable);
        sb.append(" WHERE ID = ? ");
        return sb.toString();
    }

    public ResultSet selectObject(String fatherTable, Integer id) throws SQLException{
        if(id == null){
            return null;
        }
        PreparedStatement smt = database.getPreparedStatement(getObjectSql(fatherTable));
        smt.setInt(1, id);
        return smt.executeQuery();
    }

    /**
     * Execute an SQL command in the database
     * @param sql - the sql to execute
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public void execute(String sql) throws SQLException {
        PreparedStatement smt = database.getPreparedStatement(sql);
        smt.execute();
        smt.close();
    }
    /**
     * Inserts the new register to the database
     * @param sequence the sequence name if exists
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public void insertSql(String sequence) throws SQLException {
        PreparedStatement smt = database.getPreparedStatement(getInsertSql(sequence));
        int index = 1;
        if (adapter.getGeneratorType().equals(GeneratorType.SEQUENCE)) {
            String sql = adapter.getSequenceStatment().replace("{sequence}", sequence);
            ResultSet seq = database.getPreparedStatement(sql).executeQuery();
            seq.next();
            smt.setInt(index, seq.getInt(1));
            index++;
        }
        for(Map.Entry<String, Object> e : attributes.entrySet()){
            Object value = e.getValue();
            if (!(value instanceof List) && !(value instanceof JavaRecord) &&
                    !(e.getKey().equals(getPrimaryKeyColumn()))) {
                SQLUtil.addParameter(smt, index, value);
                index++;
            }
        }
        smt.execute();
        smt.close();
        smt = null;
    }

    /**
     * Make an query in the database
     * @param sql to query
     * @return the result of the query
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public ResultSet query(String sql) throws SQLException {
        PreparedStatement smt = database.getPreparedStatement(sql);
        ResultSet rs = smt.executeQuery();
        smt.close();
        return rs;
    }

    /**
     * Remove the last comma in the StringBuilder
     * @param stringBuilder
     * @since 1.0
     */
    private void removeLastComma(StringBuilder stringBuilder) {
        stringBuilder.replace(stringBuilder.toString().lastIndexOf(","),
                stringBuilder.toString().lastIndexOf(",") + 1, "");
    }

    public String getPrimaryKey() {
        return primaryKey;
    }
    
}
