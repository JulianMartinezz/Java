package ar.edu.utn.frc.tup.lciii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temperature {
    private Long id;
    private Long location_id;
    private Double temperature;
    private String unit;
    private LocalDateTime created_at;
}
