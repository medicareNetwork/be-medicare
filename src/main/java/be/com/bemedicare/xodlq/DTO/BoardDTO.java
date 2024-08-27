package be.com.bemedicare.xodlq.DTO;

import lombok.Data;

@Data
public class BoardDTO {
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
}
