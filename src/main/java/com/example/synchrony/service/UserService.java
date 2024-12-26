package com.example.synchrony.service;

import com.example.synchrony.Exception.DBException;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.entity.User;

import java.util.Optional;

public interface UserService {

    void saveData(RegisterDTO registerDTO) throws DBException;


    GetUserResponseDTO getRecordById(Long id) throws DBException;
    void deleteRecord(Long id) throws DBException;

}
