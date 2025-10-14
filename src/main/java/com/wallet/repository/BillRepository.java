package com.wallet.repository;

import com.wallet.entity.Bill;
import com.wallet.entity.BillStatus;
import com.wallet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findByUser(User user, Pageable pageable);

    Page<Bill> findByUserAndStatus(User user, BillStatus status, Pageable pageable);

    List<Bill> findByUserAndDueDateBeforeAndStatus(User user, LocalDate date, BillStatus status);

    @Query("SELECT b FROM Bill b WHERE b.user = :user AND b.dueDate BETWEEN :startDate AND :endDate")
    List<Bill> findByUserAndDueDateBetween(@Param("user") User user,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM Bill b WHERE b.user = :user AND b.isRecurring = true AND b.status = :status")
    List<Bill> findRecurringBillsByUserAndStatus(@Param("user") User user, @Param("status") BillStatus status);
}