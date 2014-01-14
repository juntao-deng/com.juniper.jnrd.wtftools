package net.juniper.jmp.persist.jdbc;

import java.io.Serializable;

public class CacheKey implements Serializable {

	private static final long serialVersionUID = 4667052831575962521L;

	private static final int DEFAULT_MULTIPLYER = 37;

	private static final int DEFAULT_HASHCODE = 17;

	protected Object obj;

	private int multiplier;

	private int hashcode;

	private long checksum;

	private int count;

	private int hits = 0;

	public CacheKey() {
		hashcode = DEFAULT_HASHCODE;
		multiplier = DEFAULT_MULTIPLYER;
		count = 0;

	}

	/**
	 * @param initialNonZeroOddNumber
	 *            非零奇数
	 */
	public CacheKey(int initialNonZeroOddNumber) {
		hashcode = initialNonZeroOddNumber;
		multiplier = DEFAULT_MULTIPLYER;
		count = 0;
	}

	/**
	 * 构造函数
	 * 
	 * @param initialNonZeroOddNumber -
	 *            hashcode值
	 * @param multiplierNonZeroOddNumber -
	 *            乘积因子
	 */
	public CacheKey(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
		hashcode = initialNonZeroOddNumber;
		multiplier = multiplierNonZeroOddNumber;
		count = 0;
	}

	/**
	 * 更新keyObject根据一个整数变量
	 * 
	 * @param x -
	 *            the int value
	 * @return the dbcache key
	 */
	public CacheKey update(int x) {
		count++;
		checksum += x;
		x *= count;

		hashcode = multiplier * hashcode + x;

		return this;
	}

	/**
	 * 更新keyObject根据一个对象
	 * 
	 * @param object -
	 *            the object
	 * @return the cachekey
	 */
	public CacheKey update(Object object) {
		obj = object;
		update(object.hashCode());
		return this;
	}

	public CacheKey update(String sql) {
		obj = sql;
		update(sql.hashCode());
		return this;
	}

	public Object getObject() {
		return obj;
	}

	/**
	 * @param object
	 * @return
	 */
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof CacheKey))
			return false;

		final CacheKey cacheKey = (CacheKey) object;

		if (hashcode != cacheKey.hashcode)
			return false;
		if (checksum != cacheKey.checksum)
			return false;
		if (count != cacheKey.count)
			return false;
		if (!obj.equals(cacheKey.obj))
			return false;
		return true;

	}

	/**
	 * @return
	 */
	public int hashCode() {
		return hashcode;
	}

	public int getHit() {
		return hits;
	}

	public void increaseRequest() {
		hits++;
	}

	public static CacheKey createKey(String obj) {
		CacheKey key = new CacheKey();
		key.update(obj);
		return key;
	}

	public static CacheKey createKey(Object obj) {
		CacheKey key = new CacheKey();
		key.update(obj);
		return key;
	}

	/**
	 * @return
	 */
	public String toString() {
		return new StringBuffer().append(hashcode).append('-').append(checksum)
				.toString();
	}

	public static void main(String[] args) {
		String s = "WW";
		String s1 = "WW";
		if (s.hashCode() == s1.hashCode())
			System.out.println("OK");
		CacheKey key1 = CacheKey.createKey(s);
		CacheKey key2 = CacheKey.createKey(s1);
		if (key1.equals(key2))
			System.out.println("ok");
	}
}