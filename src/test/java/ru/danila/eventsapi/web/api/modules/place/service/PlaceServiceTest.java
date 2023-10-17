package ru.danila.eventsapi.web.api.modules.place.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.danila.eventsapi.persistense.PlaceEntity;
import ru.danila.eventsapi.persistense.dao.PlaceRepository;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceCreationRequest;
import ru.danila.eventsapi.web.api.modules.place.dto.assembler.PlaceDtoAssembler;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Управление местами")
@FieldDefaults(level = AccessLevel.PRIVATE)
class PlaceServiceTest {

    @Mock
    PlaceRepository placeRepository;

    @Mock
    PlaceDtoAssembler placeDtoAssembler;

    @InjectMocks
    PlaceService placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Найти все места")
    void findAll() {
        when(placeRepository.findAll()).thenReturn(List.of(getPlace1(), getPlace2()));

        List<PlaceEntity> places = placeService.findAll();

        int expectedListSize = 2;
        int actualListSize = places.size();

        Assertions.assertEquals(expectedListSize, actualListSize);

        verify(placeRepository, times(1)).findAll();
        verifyNoMoreInteractions(placeRepository);
        verifyNoInteractions(placeDtoAssembler);
    }

    @Test
    @DisplayName("Добавить место")
    void addPlace() {
        when(placeDtoAssembler.mapPlaceCreationRequestToPlaceEntity(any(PlaceCreationRequest.class)))
                .thenReturn(getPlace1());

        when(placeRepository.save(any(PlaceEntity.class))).thenReturn(getPlace1());

        PlaceEntity place = placeService.addPlace(getPlaceCreationRequest());

        String expectedPlaceAddress = "test1";
        String actualPlaceAddress = place.getAddress();

        Assertions.assertEquals(expectedPlaceAddress, actualPlaceAddress);

        verify(placeDtoAssembler, times(1)).mapPlaceCreationRequestToPlaceEntity(any(PlaceCreationRequest.class));
        verify(placeRepository, times(1)).save(any(PlaceEntity.class));
        verifyNoMoreInteractions(placeDtoAssembler);
        verifyNoMoreInteractions(placeRepository);
    }

    @Test
    @DisplayName("Удалить место по id")
    void deleteById() {
        when(placeRepository.findById(anyLong())).thenReturn(Optional.of(getPlace2()));
        doNothing().when(placeRepository).delete(any(PlaceEntity.class));

        placeService.deleteById(1L);

        verify(placeRepository, times(1)).findById(anyLong());
        verify(placeRepository, times(1)).delete(any(PlaceEntity.class));
        verifyNoMoreInteractions(placeRepository);
        verifyNoInteractions(placeDtoAssembler);
    }

    @Test
    @DisplayName("Удалить место по id, если не найдено")
    void deleteById_If_Place_If_Not_Found() {
        when(placeRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> placeService.deleteById(1L));

        String expectedExceptionMessage = "Место с таким id не найдено";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(placeRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(placeRepository);
        verifyNoInteractions(placeDtoAssembler);
    }

    private PlaceCreationRequest getPlaceCreationRequest() {
        return PlaceCreationRequest.builder()
                .address("test")
                .capacity(10L)
                .build();
    }

    private PlaceEntity getPlace1() {
        return PlaceEntity.builder()
                .id(1L)
                .address("test1")
                .capacity(10L)
                .build();
    }

    private PlaceEntity getPlace2() {
        return PlaceEntity.builder()
                .id(2L)
                .address("test2")
                .capacity(10L)
                .build();
    }
}