package catering.order;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface CateringOrderRepository extends Repository<CateringOrder, OrderIdentifier> {

	Streamable<CateringOrder> findByCompletionDate(LocalDate completionDate);

	Streamable<CateringOrder> findByAddress(String address);

	Streamable<CateringOrder> findByCompletionDateBetween(LocalDate start, LocalDate end);

	Streamable<CateringOrder> findByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDateTime start,
																		LocalDateTime end);

	int countByOrderStatusAndCompletionDateBetween(OrderStatus status, LocalDate start, LocalDate end);

	int countByCompletionDateBetween(LocalDate start, LocalDate end);
}
