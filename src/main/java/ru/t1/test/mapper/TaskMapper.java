package ru.t1.test.mapper;

import org.mapstruct.*;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    Task dtoToEntity(TaskDto taskDto);

    TaskDto entityToDto(Task task);
}
