package ru.sendto.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.sendto.dto.Dto;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonTypeName("ts")
public class TimeSyncDto extends Dto{
	long server;
	long client;
	long delta;
}
