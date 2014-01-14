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
     * 根据默认数据源得到PersistenceManager实例
     * 
     * @return
     * @throws DbException
     *             如果出错则抛出DbException
     */
    public static LfwPersistenceTool getInstance() throws DbException {
        return new LfwPersistenceToolImpl();
    }
    

    /**
     * 根据传入的数据源参数得到PersistenceManager实例
     * 
     * @param dataSourceName
     *            数据源名称
     * @return
     * @throws DbException
     *             如果出错则抛出DbException
     */
    public static LfwPersistenceTool getInstance(String dataSourceName) throws DbException {
        return new LfwPersistenceToolImpl(dataSourceName);

    }

    /**
     * 根据传入的JdbcSession参数得到PersistenceManager实例
     * 
     * @param session
     *            JdbcSession参数
     * @return
     * @throws DbException
     *             如果出错则抛出DbException
     */
    public LfwPersistenceTool getInstance(DbSession session) {
        return new LfwPersistenceToolImpl(session);

    }

    /**
     * 释放资源
     */

    public abstract void release();

    /**
     * 得到JdbcSession
     * 
     * @return 返回JdbcSession
     */
    public abstract DbSession getJdbcSession();

    /**
     * 把一个值对象插入到数据库中
     * 
     * @param vo
     *            值对象
     * @throws DbException
     *             如果插入过程中发生错误则抛出异常
     */
    public abstract String insert(SuperVO vo) throws DbException;

    /**
     * 把一个值对象集合插入到数据库中
     * 
     * @param vos
     *            值对象集合
     * @throws DbException
     *             如果插入过程中发生错误则抛出异常
     */
    public abstract String[] insert(final List<SuperVO> vos) throws DbException;

    /**
     * 把一个值对象数组插入到数据库中
     * 
     * @param vo
     *            值对象数组
     * @throws DAOException
     *             如果插入过程中发生错误则抛出异常
     */
    public abstract String[] insert(final SuperVO vo[]) throws DbException;

    /**
     * 更新一个在数据库中已经存在值对象
     * 
     * @param vo
     *            值对象
     * @throws DAOException
     *             如果插入过程中发生错误则抛出异常
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
     * 删除一个在数据库中已经存在值对象
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
     * 根据VO的字段值条件查询数据
     * 
     * @param vo
     * @return
     * @throws DbException
     */

    public abstract <M extends SuperVO>List<M> retrieve(M vo) throws DbException;

    public abstract Object retrieve(SuperVO vo, ResultSetProcessor processor) throws DbException;

    /**
     * 查询VO对应表所有数据
     * 
     * @param className
     * @return
     * @throws DbException
     */

    public abstract List<? extends SuperVO> retrieveAll(Class<? extends SuperVO> className) throws DbException;

    /**
     * 根据条件查询VO对应表数据
     * 
     * @param className
     * @param condition
     * @return
     * @throws DbException
     */
    public abstract List<? extends SuperVO> retrieveByClause(Class<? extends SuperVO> className, String condition, String[] fields) throws DbException;

    public abstract List<? extends SuperVO> retrieveByClause(Class<? extends SuperVO> className, String condition) throws DbException;


    /**
     * 得到数据库类型
     * 
     * @return
     */

    public abstract int getDBType();

    /**
     * 是否进行SQL翻译
     * 
     * @param isTranslator
     */
    public abstract void setSQLTranslator(boolean isTranslator);

   
    public abstract String[] insertWithPK(List<? extends SuperVO> vos) throws DbException;

    /**
     * 把一个值对象插入到数据库中
     * 
     * @param vo
     *            值对象
     * @throws DbException
     *             如果插入过程中发生错误则抛出异常
     */
    public abstract String insertWithPK(SuperVO vo) throws DbException;

    public abstract String[] insertWithPK(SuperVO vos[]) throws DbException;

   
    /**
     * 得到当前数据库的DatabaseMetaData
     * 
     * @return DatabaseMetaData
     */
    public abstract DatabaseMetaData getMetaData();

    /**
     * 得到当前数据库的Catalog
     * 
     * @return Catalog名称
     */
    public abstract String getCatalog();

    /**
     * 得到当前数据库的Schema
     * 
     * @return Schema名称
     */
    public abstract String getSchema();

    /**
     * 设置当前操作为只读
     * 
     * @param isReadOnly
     *            是否只读
     * @throws DbException
     *             如果设置出错则抛出DbException
     */
    public abstract void setReadOnly(boolean isReadOnly) throws DbException;


    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

}