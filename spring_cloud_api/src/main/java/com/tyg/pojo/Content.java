package com.tyg.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Content {
    private Long id;

    private String name;

    private String bangId;

    private String link;

    private Long hot;

    private Integer change;

    private String statu;

    private Integer rank;


}