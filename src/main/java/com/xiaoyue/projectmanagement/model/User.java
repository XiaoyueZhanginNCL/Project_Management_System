package com.xiaoyue.projectmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String email;
    private String fullName;
    private String password;

    @OneToMany(mappedBy = "assignee",cascade = CascadeType.ALL)
    private List<Issue> assignedIssues =new ArrayList<>();

    private int projectSize;
}
