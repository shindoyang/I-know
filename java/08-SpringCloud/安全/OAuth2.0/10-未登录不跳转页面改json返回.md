重写loginUrlAuthenticationEntryPoint 的commence方法

> https://cloud.tencent.com/developer/article/1335454



```java
				.logoutSuccessUrl(logoutUrl)
				.and().exceptionHandling().authenticationEntryPoint(loginUrlEntryPoint());
	}

	@Bean
	public LoginUrlAuthenticationEntryPoint loginUrlEntryPoint() {
		LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint("/login");
		entryPoint.setForceHttps(true);
		return entryPoint;
	}
```

