package com.project.payload.request.report;

import com.project.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ReportRequest extends BaseUserRequest {

    @NotEmpty(message = "Please enter service sequence")
    @Size(min = 1, message = "Service sequence should be at least 1 chars")
    @Pattern(regexp="\\d}", message="Service sequence must consist of  0-9.")
    private String svcSeq;
}
