## Description
- Basic Boilerplate with Spring Security with JWT Authentication

## Init
```
1. clone project
2. load maven dependemcies
3. edit yml file(your database configs)
4. run main class
5. test API with Postman
```

## API 
```
@Post("/user/signup)
{
	"email": "kelly@naver.com",
	"firstName": "YouJin",
	"lastName": "Kim",
	"password": "123123",
	"confirmPassword": "123123",
	"address": "Somewhere in Cali"
}

@Post("/user/login")
{
	"email": "kelly@naver.com",
	"password": "123123"
}

@PreAuthorized
@Post("/user/me")
{
    header(name = "Authentication": "Bearer <Received JWT>"
}
```
