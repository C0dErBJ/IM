package com.AlphaZ.entity;

import javax.persistence.*;
import java.util.Arrays;

/**
 * ProjectName: IM
 * PackageName: com.AlphaZ.entity
 * User: C0dEr
 * Date: 2017/5/26
 * Time: 下午2:42
 * Description:This is a class of com.AlphaZ.entity
 */
@Entity
@Table(name = "file", schema = "alphaz", catalog = "")
public class FileEntity extends BaseDTO{
    private Long id;
    private String filename;
    private byte[] file;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "file")
    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        if (id != that.id) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (!Arrays.equals(file, that.file)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }
}
