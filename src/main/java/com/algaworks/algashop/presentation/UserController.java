package com.algaworks.algashop.presentation;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUserManagementService authUserManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserOutput create(@RequestBody @Valid CreateAuthUserInput input) {
        return authUserManagementService.create(input);
    }

}
