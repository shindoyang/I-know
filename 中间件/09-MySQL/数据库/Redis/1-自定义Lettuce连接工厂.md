```java
    public static void main(String[] args) {
        RedisConfig redisConfig = new RedisConfig();
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConfig.lettuceConnectionFactory());
        redisTemplate.afterPropertiesSet();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name", "shindo");
        System.out.println(valueOperations.get("name"));

    }

    public static class RedisConfig {
        public LettuceConnectionFactory lettuceConnectionFactory() {
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory("123.57.131.180", 16379);
            lettuceConnectionFactory.setDatabase(2);
            lettuceConnectionFactory.afterPropertiesSet();
            return lettuceConnectionFactory;
        }
    }
```

