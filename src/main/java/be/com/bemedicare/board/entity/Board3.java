package be.com.bemedicare.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Board3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;
    private String filename;
    private String filepath;

    // Getter 및 Setter 메서드들...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename2() {
        return filename;
    }

    public void setFilename2(String filename2) {
        this.filename = filename2;
    }

    public String getFilepath2() {
        return filepath;
    }

    public void setFilepath2(String filepath2) {
        this.filepath = filepath2;
    }
}
