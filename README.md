# JWT Auth API #
Purpose of this application is to create and verify JSON Web Tokens for authentication for various services.

### Credits ###
https://developer.okta.com/blog/2018/10/31/jwts-with-java

### Requirements ###
The following variables need to be set in order to run
- **JWT_SECRET_KEY**: should NOT be in source control
- **S3_ACCESS_API_BASE_URL**: base url for s3 bucket api; can be in source control
- **S3_ACCESS_API_LIST_ENDPOINT**: endpoint to get existing list of user credentials in s3; can be in source control
- **S3_ACCESS_API_UPDATE_ENDPOINT**: endpoint for s3 bucket to update user credentials list; can be in source control
- **S3_ACCESS_API_TOKEN**: should NOT be in source control
- **CREDENTIAL_TOKEN**: should NNOT be in source control

## TODO ##
- [ ] Deployment Steps
- [x] S3 Repository
- [x] Change to POC Repo
- [ ] Outline deployment steps
- [ ] Proper versioning/tagging
- [ ] Unit Tests
- [ ] General Code Cleanup
- [ ] Encrypt passwords for S3 repo
- [ ] Set create user/password process for repository
