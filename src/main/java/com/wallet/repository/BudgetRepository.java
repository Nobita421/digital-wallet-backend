package com.wallet.repository;

import com.wallet.entity.Budget;
import com.wallet.entity.BudgetPeriod;
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
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Page<Budget> findByUser(User user, Pageable pageable);

    Page<Budget> findByUserAndIsActive(User user, Boolean isActive, Pageable pageable);

    List<Budget> findByUserAndCategory(User user, String category);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.startDate <= :date AND b.endDate >= :date AND b.isActive = true")
    List<Budget> findActiveBudgetsForDate(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.category = :category AND b.isActive = true")
    List<Budget> findActiveBudgetsByCategory(@Param("user") User user, @Param("category") String category);

    @Query("SELECT SUM(b.spentAmount) FROM Budget b WHERE b.user = :user AND b.category = :category AND b.isActive = true")
    java.math.BigDecimal getTotalSpentByCategory(@Param("user") User user, @Param("category") String category);
}