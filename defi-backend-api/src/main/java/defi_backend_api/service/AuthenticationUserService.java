package defi_backend_api.service;

import defi_backend_api.dto.AuthResponseToken;
import defi_backend_api.dto.LoginDto;
import defi_backend_api.dto.UserDetailsDto;

public interface AuthenticationUserService {
    public void register(UserDetailsDto request);

    public AuthResponseToken login(LoginDto request);

    public UserDetailsDto getFirstNameAndLastName(String token);


}
