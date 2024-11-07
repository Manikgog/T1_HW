package ru.t1.test.mapper;

import org.mapstruct.*;
import ru.t1.test.TaskStatus;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToTaskStatus")
    Task dtoToEntity(TaskDto taskDto);

    @Mapping(target = "status", source = "status", qualifiedByName = "taskStatusToString")
    TaskDto entityToDto(Task task);

    @Named("stringToTaskStatus")
    default TaskStatus stringToTaskStatus(String status) {
        return TaskStatus.fromString(status);
    }

    @Named("taskStatusToString")
    default String taskStatusToString(TaskStatus status) {
        return status.getStatus();
    }
}
