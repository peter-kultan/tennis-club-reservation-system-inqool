
# Tennis Court Reservation system

## Overview
The following repo contains spring boot rest api tennis court reservation server application.

## Requirements
For building and running this application you need:
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java18)
- [Maven 3.8.6](https://maven.apache.org/)

## Running the application locally
There are several ways to run a Spring boot application on your local machine.
One way is to execute the `main` method in the `cz.Main` class from your IDE. 

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
```shell
mvn spring-boot:run
```

## About the Service

The service is just a simple tennis court reservation REST service. It uses an in-memory database (H2) to store the data.

Here are some endpoints you can call:

### Create a surface
```
Post /api/surfaces
Accept: application/json
Content-Type: application/json

{
"name": "dirt"
}

Response: HTTP 201 (Created)
Content: Surface
```

### Get surface by id
```
Get /api/surfaces/1
Content-Type: application/json

Response: HTTP 200
Content: Surface
```

### Get all surfaces
```
Get /api/surfaces
Content-Type: application/json

Response: HTTP 200
Content: Array
```

### Update surface
```
Put /api/surfaces
Accept: application/json
Content-Type: application/json

{
"id": "1",
"name": "gravel"
}

Response: HTTP 200
Content: Surface
```

### Delete surface
```
Delete /api/surfaces/1

Response: HTTP 200
```

### Create a court
```
Post /api/courts
Accept: application/json
Content-Type: application/json

{
"surfaceId": "1",
"hourPrice": "10.25"
}

Response: HTTP 201 (Created!
Content: Court
```

### Get a court by id
```
Get /api/courts/1
Content-Type: application/json

Response: HTTP 200
Content: Court
```

### Get all courts
```
Get /api/courts
Content-Type: application/json

Response: HTTP 200
Content: Array
```

### Update a court
```
Put /api/courts
Accept: application/json
Content-Type: application/json

{
"id": "1",
"surfaceId": "1",
"hourPrice": "2.25"

Response: HTTP 200
Content: Court
}
```

### Delete a court
```
Delete /api/courts/1

Response: HTTP 200
```

### Create a reservation
```
Post /api/reservations
Accept: application/json
Content-Type: application/json

{
"courtId": "1",
"phoneNumber": "+420558445215",
"userName": "Jakub",
"startDate": "2024-10-10T10:50:00",
"duration": "180",
"reservationType": "Singles"
}

Response: HTTP 201 (Created)
Content: Double
```

### Get a reservation by id
```
Get /api/reservations/1
Content-Type: application/json

Response: HTTP 200
Content: Reservation
```

### Get reservations by phone number
```
Get /api/reservations?phoneNumber=+421584526374
Content-Type: application-json

Response: HTTP 200
Content: Array
```

### Get all reservations
```
Get /api/reservations
Content-Type: application-json

Response: HTTP 200
Content: Array
```

### Update a reservation
```
Put /api/reservations
Accepted: application/json

{
"id": "1",
"courtId": "1",
"phoneNumber": "+420558445215",
"userName": "Jakub",
"startDate": "2024-10-10T10:50:00",
"duration": "180",
"reservationType": "Singles"
}

Response: HTTP 200
```

### Delete a reservation
```
Delete /api/reservations/1

Response: HTTP 200
```
