package com.ailk.eaap.o2p.common.util;

/**
 * Represents a pool of items.
 * @author zhuangyq
 *
 */
public interface Pool<T> {

	/**
	 * Obtains an item from the pool.
	 * @return the item.
	 */
	T getItem();

	/**
	 * Releases an item back into the pool. This must be an item that
	 * was previously retrieved using {@link #getItem()}.
	 * @param t the item.
	 * @throws IllegalArgumentException when a "foreign" object
	 * is released.
	 */
	void releaseItem(T t);

	/**
	 * Removes all idle items from the pool.
	 */
	void removeAllIdleItems();

	/**
	 * Returns the current size (limit) of the pool.
	 * @return the size.
	 */
	int getPoolSize();

	/**
	 * Returns the number of items that have been allocated
	 * but are not currently in use.
	 * @return The number of items.
	 */
	int getIdleCount();

	/**
	 * Returns the number of allocated items that are currently
	 * checked out of the pool.
	 * @return The number of items.
	 */
	int getActiveCount();

	/**
	 * Returns the current count of allocated items (in use and
	 * idle). May be less than the pool size, and reflects the
	 * high water mark of pool usage.
	 * @return the number of items.
	 */
	int getAllocatedCount();

}