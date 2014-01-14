package net.juniper.jmp.persist;

import java.sql.DatabaseMetaData;
import java.util.List;

import com.thimda.persist.DbSession;
import com.thimda.persist.LfwPersistenceToolImpl;
import com.thimda.persist.ResultSetProcessor;
import com.thimda.persist.SQLParameter;
import com.thimda.persist.exception.DAOException;
import com.thimda.persist.exception.DbException;


public abstract class LfwPersistenceTool {
    protected int maxRows = 100000;
    /**
     * ����Ĭ������Դ�õ�PersistenceManagerʵ��
     * 
     * @return
     * @throws DbException
     *             ����������׳�DbException
     */
    public static LfwPersistenceTool getInstance() throws DbException {
        return new LfwPersistenceToolImpl();
    }
    

    /**
     * ���ݴ��������Դ�����õ�PersistenceManagerʵ��
     * 
     * @param dataSourceName
     *            ����Դ����
     * @return
     * @throws DbException
     *             ����������׳�DbException
     */
    public static LfwPersistenceTool getInstance(String dataSourceName) throws DbException {
        return new LfwPersistenceToolImpl(dataSourceName);

    }

    /**
     * ���ݴ����JdbcSession�����õ�PersistenceManagerʵ��
     * 
     * @param session
     *            JdbcSession����
     * @return
     * @throws DbException
     *             ����������׳�DbException
     */
    public LfwPersistenceTool getInstance(DbSession session) {
        return new LfwPersistenceToolImpl(session);

    }

    /**
     * �ͷ���Դ
     */

    public abstract void release();

    /**
     * �õ�JdbcSession
     * 
     * @return ����JdbcSession
     */
    public abstract DbSession getJdbcSession();

    /**
     * ��һ��ֵ������뵽���ݿ���
     * 
     * @param vo
     *            ֵ����
     * @throws DbException
     *             �����������з����������׳��쳣
     */
    public abstract String insert(SuperVO vo) throws DbException;

    /**
     * ��һ��ֵ���󼯺ϲ��뵽���ݿ���
     * 
     * @param vos
     *            ֵ���󼯺�
     * @throws DbException
     *             �����������з����������׳��쳣
     */
    public abstract String[] insert(final List<SuperVO> vos) throws DbException;

    /**
     * ��һ��ֵ����������뵽���ݿ���
     * 
     * @param vo
     *            ֵ��������
     * @throws DAOException
     *             �����������з����������׳��쳣
     */
    public abstract String[] insert(final SuperVO vo[]) throws DbException;

    /**
     * ����һ�������ݿ����Ѿ�����ֵ����
     * 
     * @param vo
     *            ֵ����
     * @throws DAOException
     *             �����������з����������׳��쳣
     */
    public abstract int update(final SuperVO vo) throws DbException;

    public abstract int update(final List<SuperVO> vos) throws DbException;

    public abstract int update(final SuperVO vo[]) throws DbException;

    public abstract int update(final SuperVO[] vo, String[] fieldNames) throws DbException;

    /**
     * @param vo
     * @param fieldNames
     * @param whereClause
     * @param param
     * @return
     * @throws DbException
     */
    public abstract int update(final SuperVO[] vo, String[] fieldNames, String whereClause, SQLParameter param)
            throws DbException;

    /**
     * ɾ��һ�������ݿ����Ѿ�����ֵ����
     * 
     * @param vo
     * @throws DbException
     */

    public abstract int delete(SuperVO vo) throws DbException;

    public abstract int delete(SuperVO vo[]) throws DbException;

    public abstract int delete(List<SuperVO> vos) throws DbException;

    public abstract int deleteByPK(Class<? extends SuperVO> className, String pk) throws DbException;

    public abstract int deleteByPKs(Class<? extends SuperVO> className, String[] pks) throws DbException;

    public abstract int deleteByClause(Class<? extends SuperVO> className, String wherestr) throws DbException;

    public abstract int deleteByClause(Class<? extends SuperVO> className, String wherestr, SQLParameter params) throws DbException;

    public abstract SuperVO retrieveByPK(Class<? extends SuperVO> className, String pk) throws DbException;

    public abstract SuperVO retrieveByPK(Class<? extends SuperVO> className, String pk, String[] selectedFields) throws DbException;

    /**
     * ����VO���ֶ�ֵ������ѯ����
     * 
     * @param vo
     * @return
     * @throws DbException
     */

    public abstract <M extends SuperVO>List<M> retrieve(M vo) throws DbException;

    public abstract Object retrieve(SuperVO vo, ResultSetProcessor processor) throws DbException;

    /**
     * ��ѯVO��Ӧ����������
     * 
     * @param className
     * @return
     * @throws DbException
     */

    public abstract List<? extends SuperVO> retrieveAll(Class<? extends SuperVO> className) throws DbException;

    /**
     * ����������ѯVO��Ӧ������
     * 
     * @param className
     * @param condition
     * @return
     * @throws DbException
     */
    public abstract List<? extends SuperVO> retrieveByClause(Class<? extends SuperVO> className, String condition, String[] fields) throws DbException;

    public abstract List<? extends SuperVO> retrieveByClause(Class<? extends SuperVO> className, String condition) throws DbException;


    /**
     * �õ����ݿ�����
     * 
     * @return
     */

    public abstract int getDBType();

    /**
     * �Ƿ����SQL����
     * 
     * @param isTranslator
     */
    public abstract void setSQLTranslator(boolean isTranslator);

   
    public abstract String[] insertWithPK(List<? extends SuperVO> vos) throws DbException;

    /**
     * ��һ��ֵ������뵽���ݿ���
     * 
     * @param vo
     *            ֵ����
     * @throws DbException
     *             �����������з����������׳��쳣
     */
    public abstract String insertWithPK(SuperVO vo) throws DbException;

    public abstract String[] insertWithPK(SuperVO vos[]) throws DbException;

   
    /**
     * �õ���ǰ���ݿ��DatabaseMetaData
     * 
     * @return DatabaseMetaData
     */
    public abstract DatabaseMetaData getMetaData();

    /**
     * �õ���ǰ���ݿ��Catalog
     * 
     * @return Catalog����
     */
    public abstract String getCatalog();

    /**
     * �õ���ǰ���ݿ��Schema
     * 
     * @return Schema����
     */
    public abstract String getSchema();

    /**
     * ���õ�ǰ����Ϊֻ��
     * 
     * @param isReadOnly
     *            �Ƿ�ֻ��
     * @throws DbException
     *             ������ó������׳�DbException
     */
    public abstract void setReadOnly(boolean isReadOnly) throws DbException;


    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

}