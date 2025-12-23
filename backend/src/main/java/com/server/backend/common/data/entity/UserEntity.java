package com.server.backend.common.data.entity;

import com.server.backend.common.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    private String userId;
    private String userPw;
    private String userEmail;
    private String userRole;
    private String nickname;

}
