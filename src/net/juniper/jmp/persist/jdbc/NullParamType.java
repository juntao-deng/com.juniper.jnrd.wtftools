package net.juniper.jmp.persist.jdbc;
/**
 * 
 * @author juntaod
 *
 */
public class NullParamType implements SQLParamType {
    private static final long serialVersionUID = -6229083933859489148L;
    private int type;

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
