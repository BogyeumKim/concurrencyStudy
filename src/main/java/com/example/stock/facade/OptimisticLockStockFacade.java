package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {

    private OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);

                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                // 낙관적 락 충돌 발생 시에만 재시도
                System.out.println("Optimistic Lock 충돌 발생: " + e.getMessage());
                Thread.sleep(50);
            } catch (Exception e) {
                // 다른 예외는 다시 던짐
                System.err.println("다른 예외 발생: " + e.getMessage());
                throw e;
            }
        }
    }
}
