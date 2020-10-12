package capstone.oras;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    public static void main(String[] args) {
        SpringApplication.run(SpringFoxConfig.class, args);

    }

        @Bean
    public Docket talentPoolApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .paths(PathSelectors.ant("/v1/**"))
                    .build();
//                    .securitySchemes(Lists.newArrayList(apiKey()))
//                    .securityContexts(Arrays.asList(securityContext()))
//                    .apiInfo(apiEndPointsInfo());
    }


//
//    private ApiInfo apiEndPointsInfo() {
//        return new ApiInfoBuilder().title("Spring Boot REST API")
//                .description("ORAS REST API")
//                .version("1.0.0")
//                .build();
//    }
//
//    @Bean
//    SecurityConfiguration security() {
//        return new SecurityConfiguration(
//                "test-app-client-id",
//                "test-app-client-secret",
//                "test-app-realm",
//                "test-app",
//                "",
//                ApiKeyVehicle.HEADER,
//                "Authorization",
//                "," /*scope separator*/);
//    }
//
//    private AuthorizationScope[] scopes() {
//        AuthorizationScope[] scopes = {
//                new AuthorizationScope("ORAS", "Access ORAS API") };
//        return scopes;
//    }
//
//    @Bean
//    SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(
//                        Arrays.asList(new SecurityReference("token", scopes())))
//                .build();
//    }
//
//    @Bean
//    SecurityScheme apiKey() {
//        return new ApiKey("token", "Authorization", "header");
//    }
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
//    private final Logger log = LoggerFactory.getLogger(SpringFoxConfig.class);
//
//    @Bean
//    public Docket swaggerSpringfoxDocket() {
//        log.debug("Starting Swagger");
//        Contact contact = new Contact(
//                "Matyas Albert-Nagy",
//                "https://justrocket.de",
//                "matyas@justrocket.de");
//
//        List<VendorExtension> vext = new ArrayList<>();
//        ApiInfo apiInfo = new ApiInfo(
//                "Backend API",
//                "This is the best stuff since sliced bread - API",
//                "6.6.6",
//                "https://justrocket.de",
//                contact,
//                "MIT",
//                "https://justrocket.de",
//                vext);
//
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo)
//                .pathMapping("/")
//                .apiInfo(ApiInfo.DEFAULT)
//                .forCodeGeneration(true)
//                .genericModelSubstitutes(ResponseEntity.class)
//                .ignoredParameterTypes(Pageable.class)
//                .ignoredParameterTypes(java.sql.Date.class)
//                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
//                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
//                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
//                .securityContexts(Lists.newArrayList(securityContext()))
//                .securitySchemes(Lists.newArrayList(apiKey()))
//                .useDefaultResponseMessages(false);
//
//        docket = docket.select()
//                .paths(regex(DEFAULT_INCLUDE_PATTERN))
//                .build();
//        watch.stop();
//        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
//        return docket;
//    }
//
//
//    private ApiKey apiKey() {
//        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
//                .build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope
//                = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Lists.newArrayList(
//                new SecurityReference("JWT", authorizationScopes));
//    }
}
