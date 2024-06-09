# Restaurant-Web-Project Backend Server

This backend server is based on Java Spring Rest Api.


## Functionality

-  Authentication - Login, Register, Forgot password with email verification.
-  User - Chane email, Change password, Delete account, owner can control users.
-  Categories - get, add, delete,edit category, controlled by owner.
-  Items - get, add, delete,edit category, controlled by owner.
-  Deliveries: User can change view his active and history of deliveries and cancel active delivery if it is in pending status. Staff can control all deliveries with changing statuses, controlled by staff.
-  Reservation: User can change view his active and history of reservations and cancel active reservation if it is in pending status. Staff can control all deliveries with changing statuses, controlled by staff.
-  Core information: Owner can change core details information.

## Core Information and used techniques
 - Using Spring Security with JwtToken.
 - Data is controlling with roles (user, staff, owner).
 - Validation tokens sending to email.
 - Using builder design patterns and modelMapper for controlling new entities or DTO models.
 - Using MySQL database with MultiLanguages via DTO models and JSON objects.
 - Validation DTO with spring validator.
 - Using BCryptPasswordEncoder for encoding passwords.
 - Schedule tasks for cleaning tokens.
 - Validations via interceptors.
 - MultiLanguages for sending email.
 - Categories and items are controlled via positions.
 - Amazon s3 uploader for categories and items images.