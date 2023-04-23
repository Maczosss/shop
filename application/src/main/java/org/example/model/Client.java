package org.example.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client{

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Float cash;

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", cash=" + cash +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(age, client.age) && Objects.equals(cash, client.cash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, cash);
    }
}
