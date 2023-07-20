package com.example.knowthisdog.api.dto

import com.example.knowthisdog.auth.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO): User =
    User(userDTO.id, userDTO.email, userDTO.authenticationToken)


}
