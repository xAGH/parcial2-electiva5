package xagh.eam.taskmanager.dto;

import lombok.*;
import xagh.eam.taskmanager.model.TaskStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateRequest {
    private String title;
    private String description;
    private TaskStatus status;
}