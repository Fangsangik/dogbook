package com.db.dogbook.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private int age;
    private String email;
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return age == member.age && Objects.equals(id, member.id) && Objects.equals(userId, member.userId) && Objects.equals(password, member.password) && Objects.equals(email, member.email) && Objects.equals(phoneNumber, member.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, age, email, phoneNumber);
    }
}
