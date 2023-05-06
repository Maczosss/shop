package org.application.model;

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

    public Client(Long id, String firstName, String lastName, BigDecimal cash) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        //todo, change float everywhere!
        this.cash = cash.floatValue();
    }

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
        return Objects.equals(id, client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(age, client.age) && Objects.equals(cash, client.cash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, cash);
    }

    //todo check if only need is id for checking equals

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Client client = (Client) o;
//        return Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(age, client.age) && Objects.equals(cash, client.cash);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(firstName, lastName, age, cash);
//    }
}
