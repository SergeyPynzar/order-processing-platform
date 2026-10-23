package ru.com.orderplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.model.OrdersEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrdersMapper {
    @Mapping(target = "id", ignore = true)
    OrdersEntity toEntity(OrdersDto dto);

    OrdersResponse toDto(OrdersEntity entity);
}
