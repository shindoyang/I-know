#### 前言



#### OAuth2.0是什么



spring-security-oauth2  怎么检查token过期了：

访问资源时，资源服务器返回的token过期异常提示信息

![image-20201022175921000](01-为什么要学OAuth2.0.assets/image-20201022175921000.png)

DefaultTokenServices  --> 233

-->OAuth2AccessToken  -->  DefaultOAuth2AccessToken  109

```java
	
	DefaultOAuth2AccessToken  L109
	/**
	 * Convenience method for checking expiration
	 * 
	 * @return true if the expiration is befor ethe current time
	 */
	public boolean isExpired() {
		return expiration != null && expiration.before(new Date());
	}
```

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJzaGluZG8iLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTYwMzM1OTgwMCwiYWNjb3VudFN5c3RlbUtleSI6ImRlZmF0IiwianRpIjoiMmI5OTNlZGEtMzdkZS00NzUwLWE4MDctNzRjMmMzMjRhY2U3IiwiY2xpZW50X2lkIjoic3NvLWdhdGV3YXkiLCJ1c2VybmFtZSI6InNoaW5kbyJ9.imc4h-5EdoV1fLizv8pCWs62dHslXib9nph0uasE5AI",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJzaGluZG8iLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6IjJiOTkzZWRhLTM3ZGUtNDc1MC1hODA3LTc0YzJjMzI0YWNlNyIsImV4cCI6MTYwNTk1MTc0MCwiYWNjb3VudFN5c3RlbUtleSI6ImRlZmF0IiwianRpIjoiN2ExZTZlNjUtODMzMC00ZjRlLTkxNTctNzhmMGNjNDQ3ZjhmIiwiY2xpZW50X2lkIjoic3NvLWdhdGV3YXkiLCJ1c2VybmFtZSI6InNoaW5kbyJ9.tKvuYsiwggr-rod4F3chFRPa7v73EDeMY99VWjL4wOk",
    "expires_in": 59,
    "scope": "read",
    "accountSystemKey": "defat",
    "username": "shindo",
    "jti": "2b993eda-37de-4750-a807-74c2c324ace7"
}
```

![image-20201022175713280](01-为什么要学OAuth2.0.assets/image-20201022175713280.png)





请求路径的缓存原理：

WebSessionServerRequestCache  