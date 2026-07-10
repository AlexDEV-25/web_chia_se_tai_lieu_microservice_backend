package com.example.response;

import java.time.LocalDate;

public interface DailyCountProjection {

    LocalDate getDate();

    Long getTotal();
}