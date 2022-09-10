package com.devueda.rest.webservices.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Limits {
    private Integer minimum;
    private Integer maximum;
}
