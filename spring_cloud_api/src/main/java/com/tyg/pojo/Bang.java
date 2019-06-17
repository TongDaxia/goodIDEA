package com.tyg.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class Bang {
    private Integer id;

    private String type;

    private String source;

    private Date creactTime;

    private String name;

    private Integer num;

    private String statu;

    private Integer version;


}