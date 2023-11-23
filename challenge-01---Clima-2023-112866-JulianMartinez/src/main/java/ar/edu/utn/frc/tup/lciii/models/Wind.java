package ar.edu.utn.frc.tup.lciii.models;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wind {
    private Long id;
    private Long location_id;
    private Integer speed;
    private Integer direction;
    private LocalDateTime created_at;
}
