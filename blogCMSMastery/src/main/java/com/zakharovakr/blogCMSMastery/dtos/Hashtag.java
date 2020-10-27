package com.zakharovakr.blogCMSMastery.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Hashtag {
    private int hashtagId;
    @NotBlank(message = "Please enter category name")
    @Size(max = 30, message= "Invalid category name: Please enter category name between 1-30 characters")
    private String name;

    public int getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(int hashtagId) {
        this.hashtagId = hashtagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag)) return false;
        Hashtag hashtag = (Hashtag) o;
        return getHashtagId() == hashtag.getHashtagId() &&
                Objects.equals(getName(), hashtag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHashtagId(), getName());
    }

    @Override
    public String toString() {
        return "Hashtag{" +
                "hashtagId=" + hashtagId +
                ", name='" + name + '\'' +
                '}';
    }
}
