package net.juniper.jmp.persist.jdbc;

public class NullParamType implements SQLParamType {
    private static final long serialVersionUID = -6229083933859489148L;
    int type;

    public NullParamType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
