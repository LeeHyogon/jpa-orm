package model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private Date createdDate;      //등록일
    private Date lastModifiedDate; //수정일
}
