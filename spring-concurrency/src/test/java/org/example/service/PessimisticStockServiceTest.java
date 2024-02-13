package org.example.service;

import org.example.domain.Stock;
import org.example.repository.StockRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class PessimisticStockServiceTest {

    @Autowired
    private PessimisticStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @DisplayName("재고 감소")
    @Test
    public void decreaseQuantityTest() {
        stockService.decrease(1L, 1L);

        Stock stock = stockService.findByProductId(1L);
        Assertions.assertEquals(99, stock.getQuantity());
    }

    @DisplayName("동시에 100개의 재고 감소 요청")
    @Test
    public void decreaseQuantity100AtTheSameTimeTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Stock stock = stockService.findByProductId(1L);
        Assertions.assertEquals(0, stock.getQuantity());
    }
}