package catering.order;

import org.salespointframework.order.OrderIdentifier;

import org.salespointframework.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import java.time.LocalDateTime;


public interface CateringOrderRepository extends Repository<CateringOrder, OrderIdentifier> {

	Streamable<CateringOrder> findByCompletionDate(LocalDateTime completionDate);

	Streamable<CateringOrder> findByAddress(String address);

	Streamable<CateringOrder> findByCompletionDateBetween(LocalDateTime start, LocalDateTime end);

	Streamable<CateringOrder> findByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDateTime start,
																		LocalDateTime end);

	int countByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);

	int countByCompletionDateBetween(LocalDateTime start, LocalDateTime end);
}
