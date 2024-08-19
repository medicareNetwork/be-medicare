package be.com.bemedicare.xodlq.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String filename;

    private String filepath;

    private String name;

    private int price;

    private String category;

    private int views;

    //상품 재고
    private int stockAmount;

    //== 재고관리 로직 ==//
    // 보드수정 에서 addStock 꺼내서 재고관리
    public void addStock(int amount) {
        stockAmount += amount;
    }

    // Cart에서 이거불러다가 재고관리
    public void removeStock(int amount) {
        int restStock = this.stockAmount - amount;
        if(restStock <= 0) {
            throw new IllegalArgumentException("재고가부족합니다");
        }
        this.stockAmount = restStock;
    }
}
