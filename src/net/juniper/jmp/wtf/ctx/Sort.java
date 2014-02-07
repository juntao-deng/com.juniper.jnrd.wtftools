package net.juniper.jmp.wtf.ctx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * from spring.
 * @author Juntao
 *
 */
public class Sort implements Iterable<Sort.Order>, Serializable {
	private static final long serialVersionUID = -6136519959040695602L;

	public static final Direction DEFAULT_DIRECTION = Direction.ASC;

	private final List<Order> orders;

	public Sort(Order... orders) {
		this(Arrays.asList(orders));
	}

	public Sort(List<Order> orders) {
		if (null == orders || orders.isEmpty()) {
			throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
		}

		this.orders = orders;
	}

	public Sort(String... properties) {
		this(DEFAULT_DIRECTION, properties);
	}

	public Sort(Direction direction, String... properties) {
		this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
	}

	public Sort(Direction direction, List<String> properties) {

		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("You have to provide at least one property to sort by!");
		}

		this.orders = new ArrayList<Order>(properties.size());

		for (String property : properties) {
			this.orders.add(new Order(direction, property));
		}
	}

	public Sort and(Sort sort) {

		if (sort == null) {
			return this;
		}

		ArrayList<Order> these = new ArrayList<Order>(this.orders);

		for (Order order : sort) {
			these.add(order);
		}

		return new Sort(these);
	}

	public Order getOrderFor(String property) {

		for (Order order : this) {
			if (order.getProperty().equals(property)) {
				return order;
			}
		}

		return null;
	}

	@Override
	public Iterator<Order> iterator() {
		return this.orders.iterator();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Sort)) {
			return false;
		}

		Sort that = (Sort) obj;

		return this.orders.equals(that.orders);
	}

	@Override
	public int hashCode() {

		int result = 17;
		result = 31 * result + orders.hashCode();
		return result;
	}


	public static enum Direction {

		ASC, DESC;

		public static Direction fromString(String value) {

			try {
				return Direction.valueOf(value.toUpperCase(Locale.US));
			} 
			catch (Exception e) {
				throw new IllegalArgumentException(String.format(
						"Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
			}
		}

		public static Direction fromStringOrNull(String value) {

			try {
				return fromString(value);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class Order implements Serializable {

		private static final long serialVersionUID = 1522511010900108987L;
		private static final boolean DEFAULT_IGNORE_CASE = false;

		private final Direction direction;
		private final String property;
		private final boolean ignoreCase;

		public Order(Direction direction, String property) {
			this(direction, property, DEFAULT_IGNORE_CASE);
		}

		public Order(String property) {
			this(DEFAULT_DIRECTION, property);
		}

		private Order(Direction direction, String property, boolean ignoreCase) {

			if (StringUtils.isEmpty(property)) {
				throw new IllegalArgumentException("Property must not null or empty!");
			}

			this.direction = direction == null ? DEFAULT_DIRECTION : direction;
			this.property = property;
			this.ignoreCase = ignoreCase;
		}


		public Direction getDirection() {
			return direction;
		}

		public String getProperty() {
			return property;
		}

		public boolean isAscending() {
			return this.direction.equals(Direction.ASC);
		}

		public boolean isIgnoreCase() {
			return ignoreCase;
		}

		public Order with(Direction order) {
			return new Order(order, this.property);
		}

		public Sort withProperties(String... properties) {
			return new Sort(this.direction, properties);
		}

		public Order ignoreCase() {
			return new Order(direction, property, true);
		}

		@Override
		public int hashCode() {

			int result = 17;

			result = 31 * result + direction.hashCode();
			result = 31 * result + property.hashCode();

			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Order)) {
				return false;
			}

			Order that = (Order) obj;

			return this.direction.equals(that.direction) && this.property.equals(that.property);
		}

		@Override
		public String toString() {
			return String.format("%s: %s", property, direction);
		}
	}
}
