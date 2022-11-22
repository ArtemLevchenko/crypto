package com.artbender.crypto.core.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Base Crypto Entity
 *
 * @author Artsiom Leuchanka
 */
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "cryptotable")
public class Crypto {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "price")
    private Double price;
    @Column(name = "timestamp")
    private Date timestamp;
}
