package ru.klishin.springcourse.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "Age should be greater than 0")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    // Страна, Город, Индекс(6 цифр)
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this format: Country, City, Postal Code(6 digits)")
    @Column(name = "address")
    private String address;

    public Person(int id, String name, int age, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }
}
