package com.algaworks.algashop.presentation;

@RestController
@RequestMapping("/api/v1/users/me")
public class MyUserController {

    private  final SecurityCheckApplicationSerivice securityCheckApplicationSerivice;
    private final AuthUserQueryService authUserQueryService;

    @GetMapping
    public AuthUserOutput getMe(){
        return authUserQueryService.findById(securityCheckApplicationSerivice.getAuthenticatedUserId());
    }
    
}
