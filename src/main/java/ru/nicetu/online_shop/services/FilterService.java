package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.dto.request.FilterRequest;
import ru.nicetu.online_shop.dto.response.TypeProductsDTO;

public interface FilterService {

    TypeProductsDTO buildFilteredType(int typeId, FilterRequest request);

}
