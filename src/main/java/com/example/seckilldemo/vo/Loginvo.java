package com.example.seckilldemo.vo;


import com.example.seckilldemo.validator.isMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class Loginvo {
    @NotNull
    @isMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
