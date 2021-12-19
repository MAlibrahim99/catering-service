package catering.order;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * extends the {@link Repository} from Salespoint and adds new search functionality
 */
public interface CateringOrderRepository extends Repository<CateringOrder, OrderIdentifier> {

	/**
	 * @param completionDate an input date
	 * @return all Orders to be executed on the given date
	 */
	Streamable<CateringOrder> findByCompletionDate(LocalDate completionDate);

	/**
	 * @param address an input address
	 * @return all {@link CateringOrder} to be executed at that address
	 */
	Streamable<CateringOrder> findByAddress(String address);

	/**
	 * @param start a start date
	 * @param end an end date
	 * @return all {@link CateringOrder} to be executed after the start date and before the end date
	 */
	Streamable<CateringOrder> findByCompletionDateBetween(LocalDate start, LocalDate end);

	/**
	 * @param status an {@link OrderStatus}
	 * @param start a start date
	 * @param end an end date
	 * @return all {@link CateringOrder} with the given order status to be executed after the start date and before the end date
	 */
	Streamable<CateringOrder> findByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDateTime start,
																		LocalDateTime end);

	/**
	 * @param status an {@link OrderStatus}
	 * @param time an {@link TimeSegment}
	 * @return all {@link CateringOrder} with the given order status to be executed at the given time
	 */
	Streamable<CateringOrder> findByOrderStatusAndTime(OrderStatus status, TimeSegment time);

	/**
	 * @param status an {@link OrderStatus}
	 * @param start a start date
	 * @param end an end date
	 * @return the amount of orders with the given order status to be executed after the start date and before the end date
	 */
	int countByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDate start, LocalDate end);

	/**
	 * @param start a start date
	 * @param end an end date
	 * @return the amount of orders to be executed after the start date and before the end date
	 */
	int countByCompletionDateBetween(LocalDate start, LocalDate end);
}
