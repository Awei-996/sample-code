package online.k12code.consumer.entity;

import java.util.List;

/**
 * @author Carl
 * @date 2023/11/17
 **/
public class CanalSynDto {

    private List<?> data;
    private String database;
    private String table;
    private String type;

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
