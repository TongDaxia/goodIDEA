package com.tyg.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Bang {
    private String name;

    private String type;

    private String source;

    private Date creactTime;

    private String id;

    private Integer num;

    private String statu;

    private Integer version;

    private List<Content> contents ;

}