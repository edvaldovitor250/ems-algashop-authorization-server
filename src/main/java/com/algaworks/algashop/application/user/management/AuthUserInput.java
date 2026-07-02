@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserInput {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private AuthUserType type;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserOutput {

    private UUID id;
    private String name;
    private String email;
    private AuthUserType type;
    private boolean enabled;

    public static AuthUserOutput from(AuthUser user) {
        return AuthUserOutput.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .type(user.getType())
                .enabled(user.isEnabled())
                .build();
    }
}