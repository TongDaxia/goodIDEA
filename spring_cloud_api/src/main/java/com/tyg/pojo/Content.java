package com.tyg.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class Content {
    private Long id;

    private String name;

    private Integer bangId;

    private String link;

    private Long hot;

    private Integer change;

    private String statu;

    private Integer rank;


}