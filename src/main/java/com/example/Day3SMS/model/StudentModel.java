package com.example.Day3SMS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")


public class StudentModel {

    @Id  // for primary key ka lia
    private String id;

    private String name;
    private int age;
    private String email;
}


//  classes name :  capital
//@something:  annontaters
