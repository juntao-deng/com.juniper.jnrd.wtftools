package net.juniper.jmp.persist.datasource;

import java.io.Serializable;

public class DBMetaInfo implements Serializable{

    private static final long serialVersionUID = 1230267680871744361L;
    private int type;
    private String userName;
    private String catalog;
    public DBMetaInfo(int type) {
        super();
        this.type = type;
    }
    
    public String getCatalog() {
        return catalog;
    }
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    protected int getType() {
        return type;
    }
    protected void setType(int type) {
        this.type = type;
    }
}
