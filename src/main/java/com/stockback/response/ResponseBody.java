package com.stockback.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseBody {
    private String status;
    private String message;
}
