package bk.edu.data.response.dto;

import bk.edu.data.entity.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class SegmentDto {
    private Integer segmentId;

    private String name;

    private String rule;

    private AdminEntity admin;

    private Date createdAt;

    private Date updatedAt;
}
