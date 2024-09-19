## Technologies
Spring Boot and React are used to develop this project.

## Documentaion
Using OpenAPI, via Spring-doc, the API is documented.
<img width="946" alt="{CA77D22B-37A3-4431-9982-3C8641278022}" src="https://github.com/user-attachments/assets/54c94367-59b9-4253-b277-c23e524a2636">
<img width="918" alt="{3F9D64FC-4C70-4571-B07A-F7858EBB017E}" src="https://github.com/user-attachments/assets/e654dc8c-1c77-492b-b923-329067f77545">

## Authentication and Authorization
JWT token is used for authentication operations. A role based authorization is applied. The JWT token is stored as cookie on web browser to increase the security.
<img width="526" alt="{A206FA66-5B71-4461-8094-654D92D9E7DF}" src="https://github.com/user-attachments/assets/ab20763f-789f-4e93-a784-49b4aac1205a">

If the client is not a browser the token is returned as it is to respond the login requests.

## Pages
### Login Page
<img width="960" alt="{66B560F4-51DD-43CE-96BA-0D294F5DF354}" src="https://github.com/user-attachments/assets/5b07c28a-231b-451c-ba3c-ce736de4d915">

### Admin Dashboard

#### List, update, and delete users
<img width="960" alt="{8CF38444-9034-429D-9390-77F0FE652B44}" src="https://github.com/user-attachments/assets/e6d14491-890f-4057-858d-0db0140ea1d2">

#### Create a user
<img width="960" alt="{348CA995-A6BD-4DAE-839E-140B3A048795}" src="https://github.com/user-attachments/assets/d3584f99-1657-4c2b-9f09-35f31d6b3d7a">

### User Dashboard
#### Filter Customers with Stream
Customers are filtered by using Java Stream API.
<img width="960" alt="{0C174D68-5F0B-4513-84B9-6639A721797B}" src="https://github.com/user-attachments/assets/b3d85801-cccf-43cb-a777-5d68d77973d7">

#### Filter Customers With Spring JPA's Specification
As an alternative the filtering operation takes place on DB, but not the memory.
<img width="960" alt="{6BF9E8F7-1C82-4160-BBC5-D8C6653BD6A5}" src="https://github.com/user-attachments/assets/f8153809-2a9c-43fe-8c86-5ab252b94bba">

#### Create Customer
<img width="960" alt="{0A5CE8A0-6F4D-4A8A-BB34-0B551DA93F27}" src="https://github.com/user-attachments/assets/4d48c2ed-51b5-4ffb-a302-3fb05b1c7a93">

There are form validations too:
<img width="960" alt="{42055495-CFD1-4793-B38C-BBF28490F4DE}" src="https://github.com/user-attachments/assets/21f0d3a7-7590-458b-af03-51dc22482163">

#### Responsive design
<img width="370" alt="{5B2917F4-9579-45C8-865A-2CC39DEAD755}" src="https://github.com/user-attachments/assets/9a9f9d90-feb1-4650-b688-b6db4a8eb870">
<img width="367" alt="{A7528B6B-2A05-425A-A787-597177AADAC3}" src="https://github.com/user-attachments/assets/ce92e2b3-9565-4701-aa83-4622bed0b441">

