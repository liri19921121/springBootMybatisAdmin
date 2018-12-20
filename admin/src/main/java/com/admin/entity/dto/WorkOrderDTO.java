package com.admin.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderDTO {
    private Long id;
    private Long orderId;
    private String state;
    private String content;
    private String picPath1;
    private String picPath2;
    private String picPath3;
    private Date createTime;
    private Long userId;
}
