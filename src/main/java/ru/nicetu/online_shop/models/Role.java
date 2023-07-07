package ru.nicetu.online_shop.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int role_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private PersonRole name;
}
