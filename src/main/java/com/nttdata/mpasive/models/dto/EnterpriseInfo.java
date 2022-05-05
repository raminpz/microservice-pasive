package com.nttdata.mpasive.models.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnterpriseInfo {

    @NotBlank(message = "Field name must be required")
    private String name;
    @NotBlank(message = "Field ruc must be required")
    private String ruc;
    private ArrayList<PersonInfo> holders;
    private ArrayList<PersonInfo> signers;
}
